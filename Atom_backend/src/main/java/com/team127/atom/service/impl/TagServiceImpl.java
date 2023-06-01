package com.team127.atom.service.impl;

import com.team127.atom.mapper.TagMapper;
import com.team127.atom.model.Note;
import com.team127.atom.model.Repository;
import com.team127.atom.model.Tag;
import com.team127.atom.model.User;
import com.team127.atom.service.TagService;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagMapper tagMapper;
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean createTag(Tag tag, String userId) {
        int res = tagMapper.insert(tag, userId);
        return res == 1;
    }

    @Override
    public boolean removeTag(String tagId) {
        int res = tagMapper.delete(tagId);
        return res == 1;
    }

    @Override
    public List<Tag> getAllTags(String userId) {
        return tagMapper.selectAllTags(userId);
    }

    @Override
    public List<Tag> getRepoTags(String repoId) {
        return tagMapper.selectRepoTags(repoId);
    }

    @Override
    public List<Tag> getNoteTags(String noteId) {
        return tagMapper.selectNoteTags(noteId);
    }

    @Override
    public boolean addRepoTag(String repoId, String tagId) {
        int res = tagMapper.addRepoTag(repoId, tagId);
        return res == 1;
    }


    @Override
    public boolean removeRepoTag(String repoId, String tagId) {
        int res = tagMapper.removeRepoTag(repoId, tagId);
        return res == 1;
    }

    @Override
    public boolean addNoteTag(String noteId, String tagId) {
        int res = tagMapper.addNoteTag(noteId, tagId);
        return res == 1;
    }

    @Override
    public boolean removeNoteTag(String noteId, String tagId) {
        int res = tagMapper.removeNoteTag(noteId, tagId);
        return res == 1;
    }

    @Override
    public Tag getTag(String tagId) {
        return tagMapper.selectByPrimaryKey(tagId);
    }
}
