package com.team127.atom.service.impl;

import com.team127.atom.mapper.NoteMapper;
import com.team127.atom.model.Note;
import com.team127.atom.model.NoteContent;
import com.team127.atom.model.NoteImage;
import com.team127.atom.service.NoteService;
import com.team127.atom.util.MongoUtil;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    NoteMapper noteMapper;
    @Resource
    MongoUtil mongoUtil;
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public Note getNoteById(String noteId) {
        Note res;
        String key = "Note" + noteId;
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            res = noteMapper.selectByPrimaryKey(noteId);
            if (res != null) {
                redisTemplate.opsForValue().set(key, res, 1, TimeUnit.HOURS);
            }
        } else {
            res = (Note) redisTemplate.opsForValue().get(key);
        }
        return res;
    }

    @Override
    public List<Note> getRepoNotes(String repoId) {
        return noteMapper.selectRepoNotes(repoId);
    }

    @Override
    public List<Note> getStarNotes(String userId) {
        return noteMapper.selectStarNotes(userId);
    }

    @Override
    public boolean starNote(String userId, String noteId) {
        int res = noteMapper.starNote(noteId, userId);
        return res == 1;
    }

    @Override
    public boolean unStarNote(String userId, String noteId) {
        int res = noteMapper.unStarNote(noteId, userId);
        return res == 1;
    }

    @Override
    public boolean removeNoteById(String noteId) {
        int signal = noteMapper.deleteByPrimaryKey(noteId);
        redisTemplate.delete("Note"+noteId);
        return signal == 1;
    }

    @Override
    public boolean updateNoteInfoById(Note updated) {
        int signal = noteMapper.updateByPrimaryKeySelective(updated);
        redisTemplate.delete("Note"+updated.getId());
        return signal == 1;
    }

    @Override
    public boolean addNewNote(Note note) {
        return noteMapper.insert(note) == 1;
    }

    @Override
    public void saveOrUpdateContent(NoteContent content) {
        if (mongoUtil.exists(content.getId(), content.getClass())){
            Query query = new Query(Criteria.where("id").is(content.getId()));
            Update update = new Update().set("content", content.getContent());
            mongoUtil.update(query, update, content.getClass());
        } else {
            mongoUtil.save(content);
        }
    }

    @Override
    public void removeContent(NoteContent content) {
        if (mongoUtil.exists(content.getId(), content.getClass())){
            mongoUtil.delete(content);
        }
    }

    @Override
    public String getContent(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        List<?> list = mongoUtil.find(query, NoteContent.class);
        if (list.size() != 0) {
            NoteContent content = (NoteContent) list.get(0);
            return content.getContent();
        } else {
            return "";
        }
    }

    @Override
    public int storeImage(NoteImage image) {
        if (mongoUtil.exists(image.getImageId(), image.getClass())){
            return 1;
        } else {
            mongoUtil.save(image);
            return 0;
        }
    }

    @Override
    public void removeImage(NoteImage image) {
        if (mongoUtil.exists(image.getImageId(), image.getClass())){
            mongoUtil.delete(image);
        }
    }

    @Override
    public List<Note> searchNoteByWord(String input) {
        return noteMapper.searchNotes(input);
    }

    @Override
    public List<Note> recommendNotes(String userId) {
        return noteMapper.recommendNotes(userId);
    }

    @Override
    public byte[] getImage(String imageId) {
        Query query = new Query(Criteria.where("imageId").is(imageId));
        List<?> list = mongoUtil.find(query, NoteImage.class);
        if (list.size() != 0) {
            NoteImage content = (NoteImage) list.get(0);
            return content.getImage();
        } else {
            return null;
        }
    }
}
