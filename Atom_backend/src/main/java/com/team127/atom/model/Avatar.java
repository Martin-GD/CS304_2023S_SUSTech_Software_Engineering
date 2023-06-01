package com.team127.atom.model;
import lombok.Data;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

@Data
@Component
public class Avatar {
    @Id
    String id;

    @Field("userId")
    String userId;

    @Field("avatar")
    Binary avatar;
}
