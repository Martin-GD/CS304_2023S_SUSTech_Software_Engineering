package com.team127.atom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
public class Notification {
    String title;
    String content;
    Long time;

    public Notification(){}
}
