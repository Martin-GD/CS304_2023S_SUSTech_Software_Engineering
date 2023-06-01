package com.team127.atom.service;


import com.team127.atom.model.Tag;


import java.util.List;

public interface TagService {
    boolean createTag(Tag tag, String userId);

    boolean removeTag(String tagId);

    Tag getTag(String tagId);

    List<Tag> getAllTags(String userId);

    List<Tag> getRepoTags(String repoId);

    List<Tag> getNoteTags(String noteId);

    boolean addRepoTag(String repoId, String tagId);

    boolean removeRepoTag(String repoId, String tagId);

    boolean addNoteTag(String noteId, String tagId);

    boolean removeNoteTag(String noteId, String tagId);
}
