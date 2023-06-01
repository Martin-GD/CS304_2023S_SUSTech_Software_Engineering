package com.team127.atom.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoUtil {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(Object object) {
        mongoTemplate.save(object);
    }

    public void delete(Object object) {
        mongoTemplate.remove(object);
    }

    public void update(Query query, Update update, Class<?> clazz) {
        mongoTemplate.updateFirst(query, update, clazz);
    }

    public List<?> find(Query query, Class<?> clazz) {
        return mongoTemplate.find(query, clazz);
    }

    public boolean exists(String id, Class<?> clazz) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.exists(query, clazz);
    }
}
