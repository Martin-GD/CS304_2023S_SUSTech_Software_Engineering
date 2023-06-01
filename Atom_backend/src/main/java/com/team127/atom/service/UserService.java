package com.team127.atom.service;

import java.util.List;

import com.team127.atom.model.Notification;
import com.team127.atom.model.User;

//import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService  {
    User getUserById(String id);

    User getUserByName(String name);

    boolean insertUser(User user);

    boolean saveUser(User user);

    boolean modifyUserOnPasswordById(User user);

    boolean deleteUserById(String id);


    void uploadAvatar(String userId, byte[] avatar);

    byte[] getAvatar(String userId);

    boolean likeUser(String userId, String likeId);

    boolean unlikeUser(String userId, String likeId);

    List<User> getLikeUser(String id);

    List<User> getFriends(String id);

    boolean isLike(String userId, String likeId);

    boolean publishNotification(String title, String notification);

    int getLikeUserCount(String id);

    int getFollowerCount(String id);

    List<Notification> getNotifications();

    List<User> getRecommendUsers(String id);
}
