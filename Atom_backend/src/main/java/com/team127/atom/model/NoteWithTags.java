package com.team127.atom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
public class NoteWithTags {
    private String id;

    private String owner;

    private String repository;

    private String name;

    private Integer auth;

    private Long createdAt;

    private Long updatedAt;

    private String description;

    private List<Tag> tag;

    public NoteWithTags(Note note, List<Tag> tags){
        this.id = note.getId();
        this.owner = note.getOwner();
        this.repository = note.getRepository();
        this.name = note.getName();
        this.auth = note.getAuth();
        this.createdAt = note.getCreatedAt();
        this.updatedAt = note.getUpdatedAt();
        this.description = note.getDescription();
        this.tag = tags;
    }
}
