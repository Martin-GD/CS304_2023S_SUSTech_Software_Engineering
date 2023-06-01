package com.team127.atom.service;

import com.team127.atom.model.Note;
import com.team127.atom.model.NoteContent;
import com.team127.atom.model.NoteImage;
import com.team127.atom.model.Tag;
import org.bson.types.Binary;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public interface NoteService {
    Note getNoteById(String noteId);

    List<Note> getRepoNotes(String repoId);

    List<Note> getStarNotes(String userId);

    boolean starNote(String userId, String noteId);

    boolean unStarNote(String userId, String noteId);

    boolean removeNoteById(String noteId);

    boolean updateNoteInfoById(Note updated);

    boolean addNewNote(Note note);

    void saveOrUpdateContent(NoteContent content);

    void removeContent(NoteContent content);

    String getContent(String id);

    int storeImage(NoteImage image);

    void removeImage(NoteImage image);

    List<Note> searchNoteByWord(String input);

    List<Note> recommendNotes(String userId);

    byte[] getImage(String imageId);
}
