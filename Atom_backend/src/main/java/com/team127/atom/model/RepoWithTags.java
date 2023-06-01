package com.team127.atom.model;

import com.team127.atom.service.UserService;
import com.team127.atom.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Data
@AllArgsConstructor
public class RepoWithTags {
    private String id;

    private String ownerName;

    private String name;

    private Integer auth;
    List<Tag> tags;

    public RepoWithTags(Repository repository, List<Tag> tags){
        this.id = repository.getId();
        this.ownerName = repository.getOwnerId();
        this.name = repository.getName();
        this.auth = repository.getAuth();
        this.tags = tags;
    }
}
