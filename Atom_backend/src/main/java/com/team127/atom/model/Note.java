package com.team127.atom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
public class Note {

    private String id;

    private String owner;

    private String repository;

    private String name;

    private Integer auth;

    private Long createdAt;

    private Long updatedAt;

    private String description;

    public Note() {}
}