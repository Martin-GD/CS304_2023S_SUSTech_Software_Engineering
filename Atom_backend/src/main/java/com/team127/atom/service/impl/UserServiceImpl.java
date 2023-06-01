package com.team127.atom.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.team127.atom.model.Avatar;
import com.team127.atom.model.Notification;
import com.team127.atom.model.User;
import com.team127.atom.service.UserService;
import com.team127.atom.util.MongoUtil;
import com.team127.atom.mapper.UserMapper;
import javax.annotation.Resource;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Resource
    MongoUtil mongoUtil;
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public User getUserById(String id) {
        User user;
        String key = "User" + id;
        if(redisTemplate.hasKey(key) != null && Boolean.FALSE.equals(redisTemplate.hasKey(key))){
            user = userMapper.selectUserById(id);
            if (user != null) {
                redisTemplate.opsForValue().set(key, user, 1, TimeUnit.HOURS);
            }
        } else {
            user = (User) redisTemplate.opsForValue().get(key);
        }
        return user;
    }


    @Override
    public User getUserByName(String name) {
         User user;
         String key = "User" + name;
         user = userMapper.selectUserByName(name);
         return user;
    }

    @Override
    public boolean insertUser(User user) {
        int signal = userMapper.insert(user);
        redisTemplate.delete("Collection"+user.getId());
        return signal == 1;
    }

    @Override
    public boolean saveUser(User user) {
        int signal = userMapper.updateUser(user);
        redisTemplate.delete("Collection"+user.getId());
        redisTemplate.delete("User"+user.getId());
        return signal==1;
    }

    @Override
    public boolean modifyUserOnPasswordById(User user) {
        int signal = userMapper.updatePassword(user);
        redisTemplate.delete("Collection"+user.getId());
        redisTemplate.delete("User"+user.getId());
        return signal==1;
    }

    @Override
    public boolean deleteUserById(String id) {
        int signal = userMapper.deleteById(id);
        redisTemplate.delete("Collection"+id);
        redisTemplate.delete("User"+id);
        return signal == 1;
    }


    @Override
    public void uploadAvatar(String userId, byte[] avatar) throws RuntimeException{
        Query query = new Query(Criteria.where("userId").is(userId));
        List<?> list = mongoUtil.find(query, Avatar.class);
        if(list.size() == 0 && avatar != null){
            Avatar newAvatar = new Avatar();
            newAvatar.setAvatar(new Binary(avatar));
            newAvatar.setUserId(userId);
            mongoUtil.save(newAvatar);
        } else if (list.size() != 0 && avatar != null) {
            Update update = new Update().set("avatar", new Binary(avatar));
            mongoUtil.update(query, update, Avatar.class);
        }else {
            throw new RuntimeException("avatar is null");
        }
    }

    @Override
    public byte[] getAvatar(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        List<?> list = mongoUtil.find(query, Avatar.class);
        if(list.size() != 0){
            Avatar avatar = (Avatar) list.get(0);
            Binary binary = avatar.getAvatar();
            if(binary != null){
                return binary.getData();
            }
        }

        Query query1 = new Query(Criteria.where("userId").is("default"));
        List<?> list1 = mongoUtil.find(query1, Avatar.class);
        Avatar avatar = (Avatar) list1.get(0);
        if(avatar != null){
            return avatar.getAvatar().getData();
        }
        throw new RuntimeException("avatar is null");
    }


    @Override
    public boolean likeUser(String userId, String likeId) {
        int signal = userMapper.likeUser(userId, likeId);
        return signal == 1;
    }


    @Override
    public boolean unlikeUser(String userId, String likeId) {
        int signal = userMapper.unlikeUser(userId, likeId);
        return signal == 1;
    }


    @Override
    public List<User> getLikeUser(String id) {
        List<User> list = userMapper.selectLikeUser(id);
        return list;
    }
  
    @Override
    public List<User> getFriends(String id) {
        List<User> list1 = userMapper.selectLikeUser(id);
        List<User> list2 = userMapper.selectBeLikedUser(id);
//        System.out.println("list1:" + list1);
//        System.out.println("list2:" + list2);
        list1.retainAll(list2);
        return list1;
    }

    @Override
    public boolean isLike(String userId, String likeId) {
        return userMapper.isLike(userId, likeId)==1;
    }

    @Override

    public int getLikeUserCount(String id) {
        return userMapper.selectLikeUserCount(id);
    }

    @Override
    public int getFollowerCount(String id) {
        return userMapper.selectFollowerCount(id);
    }

    public boolean publishNotification(String title, String notification) {
        userMapper.publishNotification(title, notification, System.currentTimeMillis());
        return true;
    }

    @Override
    public List<Notification> getNotifications() {
        return userMapper.getNotifications();
    }


    @Override
    public List<User> getRecommendUsers(String id){
        return userMapper.selectRecommendUsers(id);
    }
}
