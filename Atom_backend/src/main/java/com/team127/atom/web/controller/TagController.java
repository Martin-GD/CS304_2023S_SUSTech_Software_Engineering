package com.team127.atom.web.controller;

import com.team127.atom.model.Response;
import com.team127.atom.model.Tag;
import com.team127.atom.model.User;
import com.team127.atom.service.NoteService;
import com.team127.atom.service.RepoService;
import com.team127.atom.service.TagService;
import com.team127.atom.service.UserService;
import com.team127.atom.util.HashGenerator;
import com.team127.atom.util.JWTUtil;
import com.team127.atom.model.Note;
import com.team127.atom.model.NoteContent;
import com.team127.atom.model.Repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
public class TagController {

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    private NoteService noteService;

    @Autowired 
    private RepoService repoService;

    @PostMapping("/addTag")
    public Response addTag(@RequestBody String name,
            @RequestBody String color,
            @RequestBody String description,
            @RequestBody String token) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (JWTUtil.verify(token, user.getId(), user.getPassword()) == false) {
            return Response.error(1004, "Token verification failed");
        }
        try {
            Tag tag = new Tag(HashGenerator.hash(name, user.getId()), name, color,
                    description, user.getId());
            tagService.createTag(tag, user.getId());
            return Response.success(null);
        } catch (Exception e) {
            return Response.error(1005, "Tag already exists");
        }
    }

    @PostMapping("/deleteTag")
    public Response deleteTag(@RequestBody String tagId,
            @RequestBody String token) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (JWTUtil.verify(token, user.getId(), user.getPassword()) == false) {
            return Response.error(1004, "Token verification failed");
        }
        try {
            Tag tag = tagService.getTag(tagId);
            if (tag == null || tag.getCreator() == user.getId()) {
                return Response.error(1006, "Delete tag failed");
            }
            tagService.removeTag(tagId);
            return Response.success(null);
        } catch (Exception e) {
            return Response.error(1005, "Delete tag failed");
        }
    }

    @GetMapping("/getTagList")
    public Response getTagList(@RequestBody String token) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (JWTUtil.verify(token, user.getId(), user.getPassword()) == false) {
            return Response.error(1004, "Token verification failed");
        }
        try{
            return Response.success(tagService.getAllTags(user.getId()));
        } catch (Exception e) {
            return Response.error(1005, "Get tag list failed");
        }
    }

    @GetMapping("/getRepoTagList")
    public Response getRepoTagList(@RequestBody String repoId,
            @RequestBody String token) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (JWTUtil.verify(token, user.getId(), user.getPassword()) == false) {
            return Response.error(1004, "Token verification failed");
        }
        try{
            return Response.success(tagService.getRepoTags(repoId));
        } catch (Exception e) {
            return Response.error(1005, "Get repo tag list failed");
        }
    }

    @GetMapping("/getNoteTagList")
    public Response getNoteTagList(@RequestBody String noteId,
            @RequestBody String token) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (JWTUtil.verify(token, user.getId(), user.getPassword()) == false) {
            return Response.error(1004, "Token verification failed");
        }
        try{
            return Response.success(tagService.getNoteTags(noteId));
        } catch (Exception e) {
            return Response.error(1005, "Get note tag list failed");
        }
    }

    @PostMapping("/addRepoTag")
    public Response addRepoTag(@RequestBody String repoId,
            @RequestBody String tagId,
            @RequestBody String token) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (JWTUtil.verify(token, user.getId(), user.getPassword()) == false) {
            return Response.error(1004, "Token verification failed");
        }
        try{
            Repository repo = repoService.getRepoById(repoId);
            if (repo == null || repo.getOwnerId() != user.getId()) {
                return Response.error(1006, "Add repo tag failed");
            }
            tagService.addRepoTag(repoId, tagId);
            return Response.success(null);
        } catch (Exception e) {
            return Response.error(1005, "Add repo tag failed");
        }
    }

    @PostMapping("/deleteRepoTag")
    public Response deleteRepoTag(@RequestBody String repoId,
            @RequestBody String tagId,
            @RequestBody String token) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (JWTUtil.verify(token, user.getId(), user.getPassword()) == false) {
            return Response.error(1004, "Token verification failed");
        }
        try{
            Repository repo = repoService.getRepoById(repoId);
            if (repo == null || repo.getOwnerId() != user.getId()) {
                return Response.error(1006, "Delete repo tag failed");
            }
            tagService.removeRepoTag(repoId, tagId);
            return Response.success(null);
        } catch (Exception e) {
            return Response.error(1005, "Delete repo tag failed");
        }
    }

    @PostMapping("/addNoteTag")
    public Response addNoteTag(@RequestBody String noteId,
            @RequestBody String tagId,
            @RequestBody String token) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (JWTUtil.verify(token, user.getId(), user.getPassword()) == false) {
            return Response.error(1004, "Token verification failed");
        }
        try{
            Note note = noteService.getNoteById(noteId);
            if (note == null || note.getOwner() != user.getId()) {
                return Response.error(1006, "Add note tag failed");
            }
            tagService.addNoteTag(noteId, tagId);
            return Response.success(null);
        } catch (Exception e) {
            return Response.error(1005, "Add note tag failed");
        }
    }

    @PostMapping("/deleteNoteTag")
    public Response deleteNoteTag(@RequestBody String noteId,
            @RequestBody String tagId,
            @RequestBody String token) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (JWTUtil.verify(token, user.getId(), user.getPassword()) == false) {
            return Response.error(1004, "Token verification failed");
        }
        try{
            Note note = noteService.getNoteById(noteId);
            if (note == null || note.getOwner() != user.getId()) {
                return Response.error(1006, "Delete note tag failed");
            }
            tagService.removeNoteTag(noteId, tagId);
            return Response.success(null);
        } catch (Exception e) {
            return Response.error(1005, "Delete note tag failed");
        }
    }

}
