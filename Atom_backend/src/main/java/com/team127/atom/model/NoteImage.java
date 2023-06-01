package com.team127.atom.model;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import java.awt.*;

@Data
@Component
public class NoteImage {
    @Id
    String imageId;

    @Field("image")
    byte[] image;
}
