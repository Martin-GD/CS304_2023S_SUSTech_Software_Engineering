package com.team127.atom.web.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.team127.atom.model.*;
import com.team127.atom.service.NoteService;
import com.team127.atom.service.TagService;
import com.team127.atom.service.UserService;
import com.team127.atom.util.HashGenerator;
import com.team127.atom.util.JWTUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@CrossOrigin
@WebServlet
@Slf4j
public class NoteController {

    @Autowired
    NoteService noteService;

    @Autowired
    TagService tagService;

    @Autowired
    UserService userService;

    @PostMapping("/addNote")
    public Response addNote(@RequestParam("token") String token,
                            @RequestParam("repoId") String repoId,
                            @RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("auth") int auth,
                            @RequestBody List<String> tags) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, user.getId(), user.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        Note note = new Note();
        note.setId(HashGenerator.hash(user.getId(), repoId, title));
        note.setAuth(auth);
        note.setRepository(repoId);
        note.setName(title);
        note.setOwner(user.getId());
        note.setDescription(description);
        Long time = System.currentTimeMillis();
        note.setCreatedAt(time);
        note.setUpdatedAt(time);
        Note check = noteService.getNoteById(note.getId());
        if (check != null && !check.getRepository().equals(repoId)) {
            return Response.error(400, "This note has been exited in this repository.");
        }
        boolean res = noteService.addNewNote(note);
        if (!res) {
            return Response.error(500, "Internal error.");
        }
        for (String tag : tags) {
            String tagId = HashGenerator.hash(tag, user.getId());
            Tag tagDB = tagService.getTag(tagId);
            if (tagDB == null) {
                Tag newTag = new Tag(tagId, tag, "0x0000", "0000", user.getId());
                tagService.createTag(newTag, user.getId());
                tagService.addNoteTag(note.getId(), newTag.getId());
            }else{
                tagService.addNoteTag(note.getId(), tagId);
            }
        }
        return Response.success(null);
    }

    @GetMapping("/getNote")
    public Response getNote(@RequestParam("noteId") String noteId,
                            @RequestParam("token") String token){
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, user.getId(), user.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        Note note = noteService.getNoteById(noteId);
        List<Note> list = noteService.getStarNotes(user.getId());
        Long count = list.stream().filter(o->o.getId().equals(noteId)).count();
        JSONObject jsonObject;
        if (count == 0){
            jsonObject = JSONUtil.createObj().putOnce("note", note).putOnce("star", false);
        } else {
            jsonObject = JSONUtil.createObj().putOnce("note", note).putOnce("star", true);
        }
        return Response.success(jsonObject);
    }

    @PostMapping("/modifyNoteSetting")
    public Response modifySetting(@RequestParam("token") String token,
                                  @RequestParam("noteId") String noteId,
                                  @RequestParam("title") String title,
                                  @RequestParam("description") String description,
                                  @RequestParam("auth") int auth) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        Note old = noteService.getNoteById(noteId);
        if (!JWTUtil.verify(token, user.getId(), user.getPassword()) ||
            !old.getOwner().equals(user.getId())) {
            return Response.error(1004, "Token verification failed");
        }
        old.setAuth(auth);
        old.setName(title);
        old.setDescription(description);
        noteService.updateNoteInfoById(old);
        return Response.success(null);
    }

    @PostMapping("/deleteNote")
    public Response deleteNote(@RequestParam("token") String token, @RequestParam("noteId") String noteId) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        Note note = noteService.getNoteById(noteId);
        if (!JWTUtil.verify(token, user.getId(), user.getPassword()) ||
            !note.getOwner().equals(user.getId())) {
            return Response.error(1004, "Token verification failed");
        }
        noteService.removeNoteById(noteId);
        NoteContent noteContent = new NoteContent();
        noteContent.setId(noteId);
        noteService.removeContent(noteContent);
        return Response.success(null);
    }

    @GetMapping("/getNoteContent")
    public Response getNoteContent(@RequestParam("note") String noteId) {
        String content = noteService.getContent(noteId);
        return Response.success(content);
    }

    @GetMapping("/getNoteList")
    public Response getNoteList(@RequestParam("token") String token, @RequestParam("repo") String repoId) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        List<Note> list = noteService.getRepoNotes(repoId);
        list.removeIf(note -> !note.getOwner().equals(user.getId()) && note.getAuth() == 2);
        List<NoteWithTags> result = new ArrayList<>();
        for (Note note: list){
            List<Tag> tags = tagService.getNoteTags(note.getId());
            NoteWithTags tem = new NoteWithTags(note, tags);
            tem.setOwner(userService.getUserById(tem.getOwner()).getName());
            result.add(tem);
        }
        return Response.success(result);
    }


    @PostMapping("/updateNoteContent")
    public Response updateNoteContent(@RequestParam("token") String token,
                                      @RequestParam("note") String noteId,
                                      @RequestParam("content") String content) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        Note note = noteService.getNoteById(noteId);
        if (!JWTUtil.verify(token, user.getId(), user.getPassword()) ||
            !note.getOwner().equals(user.getId())) {
            return Response.error(1004, "Token verification failed");
        }
        NoteContent c = new NoteContent();
        c.setId(noteId);
        c.setContent(content);
        noteService.saveOrUpdateContent(c);
        return Response.success(null);
    }

    @PostMapping("/uploadNoteImage")
    public Response uploadNoteImage(@RequestParam("token") String token,
                                    @RequestParam("note") String noteId,
                                    @RequestParam("imageName") String imageName,
                                    @RequestParam("image") MultipartFile image) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        Note note = noteService.getNoteById(noteId);
        if (!JWTUtil.verify(token, user.getId(), user.getPassword()) ||
            !note.getOwner().equals(user.getId())) {
            return Response.error(1004, "Token verification failed");
        }
        NoteImage storeImage = new NoteImage();
        storeImage.setImageId(noteId + imageName);
        try {
            storeImage.setImage(image.getBytes());
            int res = noteService.storeImage(storeImage);
            if (res == 1) {
                return Response.error(400, "The picture name has exist");
            }
        } catch (Exception e) {
            return Response.error(400, "Upload image failed");
        }
        return Response.success(
            "http://10.26.11.88:8080/getNoteImage/" + noteId + "/" + imageName + ".png");
    }


    @GetMapping(value = "/getNoteImage/{noteId}/{imageName}.png")
    @ResponseBody
    public void getNoteImage(@PathVariable("noteId") String noteId,
                             @PathVariable("imageName") String imageName,
                             HttpServletResponse response) {
        NoteImage image = new NoteImage();
        image.setImageId(noteId + imageName);
        byte[] res = noteService.getImage(image.getImageId());
        System.out.println("get note Image");
        response.setContentType("image/png");
        response.setStatus(200);
        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        return Response.success(Base64.getEncoder().encodeToString(res));
    }


    @GetMapping("/getStarNotes")
    public Response getStarNotes(@RequestParam("token") String token) {
        String s = JWTUtil.getUserId(token);
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, user.getId(), user.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        List<Note> list = noteService.getStarNotes(user.getId());
        list.removeIf(note -> !note.getOwner().equals(user.getId()) && note.getAuth() == 2);
        List<NoteWithTags> result = new ArrayList<>();
        for (Note note: list){
            List<Tag> tags = tagService.getNoteTags(note.getId());
            NoteWithTags tem = new NoteWithTags(note, tags);
            tem.setOwner(userService.getUserById(tem.getOwner()).getName());
            result.add(tem);
        }
        return Response.success(result);
    }

    @PostMapping("/starNote")
    public Response starNote(@RequestParam("note") String noteId, @RequestParam("token") String token) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, user.getId(), user.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        boolean res = noteService.starNote(user.getId(), noteId);
        if (res) {
            return Response.success(null);
        } else {
            return Response.error(400, "Fail to star the note.");
        }
    }

    @PostMapping("/unstarNote")
    public Response unstarNote(@RequestParam("note") String noteId, @RequestParam("token") String token) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, user.getId(), user.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        boolean res = noteService.unStarNote(user.getId(), noteId);
        if (res) {
            return Response.success(null);
        } else {
            return Response.error(400, "Fail to unsubscribe the note.");
        }
    }

    @GetMapping("/searchNotes")
    public Response searchNotes(@RequestParam("input") String input){
        List<Note> result = noteService.searchNoteByWord(input);
        return Response.success(result);
    }

    @GetMapping("/recommendNotes")
    public Response recommendNotes(@RequestParam("token") String token){
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, user.getId(), user.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        List<Note> res = noteService.recommendNotes(user.getId());
        return Response.success(res);
    }
}
