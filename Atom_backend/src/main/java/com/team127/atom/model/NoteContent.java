package com.team127.atom.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

@Data
@Component
public class NoteContent {
    @Id
    String id;
    @Field("content")
    String content;
}
