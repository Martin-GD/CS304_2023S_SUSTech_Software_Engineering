package com.team127.atom.ServiceTests;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.mongodb.internal.connection.Time;
import com.team127.atom.mapper.UserMapper;
import com.team127.atom.model.User;
import com.team127.atom.service.UserService;
import com.team127.atom.service.impl.UserServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceImplTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("testuser.getId()", "testUserName", "testUserBio", "testUserLocation", "testUserCompany", "testUserCreatedAt", "testUserUpdatedAt", "testUserEmail", "testUserPassword", 1, false, new Date(0), 0);
        // clear redis cache & sql data
        redisTemplate.delete("User" + user.getName());
        redisTemplate.delete("User"+ user.getId());
        userMapper.deleteById(user.getId());
    }

    @AfterEach
    public void tearDown() {
        // clear redis cache & sql data
        redisTemplate.delete("User" + user.getName());
        redisTemplate.delete("User"+ user.getId());
        userMapper.deleteById(user.getId());
    }

    @Test
    public void testInsertUser() {
        when(userService.insertUser(user)).thenReturn(true);
    }


    @Test
    public void testGetUserByName() {
        userService.insertUser(user);
        when(userService.getUserByName(user.getName())).thenReturn(user);
    }

    @Test
    public void testSaveUser() {
        userService.insertUser(user);
        user.setPassword("newPassword");
        when(userService.saveUser(user)).thenReturn(true);
        when(userService.getUserById(user.getId())).thenReturn(user);
        user.setPassword("testUserPassword");
    }

    @Test
    public void testModifyUserOnPasswordById() {
        userService.insertUser(user);
        user.setPassword("newPassword");
        when(userService.modifyUserOnPasswordById(user)).thenReturn(true);
        when(userService.getUserById(user.getId())).thenReturn(user);
        user.setPassword("testUserPassword");
    }

    @Test
    public void testDeleteUserById() {
        userService.insertUser(user);
        when(userService.deleteUserById(user.getId())).thenReturn(true);
        when(userService.getUserById(user.getId())).thenReturn(null);
    }

    @Test
    public void testLikeuser(){
        userService.insertUser(user);
        User user2 = new User("testuser2.getId()", "testUserName2", "testUserBio2", "testUserLocation2", "testUserCompany2", "testUserCreatedAt2", "testUserUpdatedAt2", "testUserEmail2", "testUserPassword2", 1, false, new Date(0), 0);
        userService.insertUser(user2);
        when(userService.likeUser(user.getId(), user2.getId())).thenReturn(true);
        when(userService.isLike(user.getId(), user2.getId())).thenReturn(true);
        when(userService.getLikeUserCount(user.getId())).thenReturn(1);
        when(userService.getLikeUser(user.getId())).thenReturn(null);
        when(userService.getFollowerCount(user2.getId())).thenReturn(1);
        when(userService.getFriends(user.getId())).thenReturn(null);
        when(userService.unlikeUser(user.getId(), user2.getId())).thenReturn(true);
        when(userService.isLike(user.getId(), user2.getId())).thenReturn(false);
        when(userService.getLikeUserCount(user.getId())).thenReturn(0);
        when(userService.getLikeUser(user.getId())).thenReturn(null);
        when(userService.getFollowerCount(user2.getId())).thenReturn(0);
        when(userService.getFriends(user.getId())).thenReturn(null);
    }

    @Test
    public void testUnlikeUser(){
        userService.insertUser(user);
        User user2 = new User("testuser2.getId()", "testUserName2", "testUserBio2", "testUserLocation2", "testUserCompany2", "testUserCreatedAt2", "testUserUpdatedAt2", "testUserEmail2", "testUserPassword2", 1, false, new Date(0), 0);
        userService.insertUser(user2);
        when(userService.unlikeUser(user.getId(), user2.getId())).thenReturn(true);
        when(userService.isLike(user.getId(), user2.getId())).thenReturn(false);
        when(userService.getLikeUserCount(user.getId())).thenReturn(0);
        when(userService.getLikeUser(user.getId())).thenReturn(null);
        when(userService.getFollowerCount(user2.getId())).thenReturn(0);
        when(userService.getFriends(user.getId())).thenReturn(null);
    } 

    @Test
    public void testGetLikeUser(){
        userService.insertUser(user);
        when(userService.getLikeUser(user.getId())).thenReturn(null);
    }

    @Test
    public void testGetFriends(){
        userService.insertUser(user);
        when(userService.getFriends(user.getId())).thenReturn(null);
    }

    @Test
    public void testGetLikeUserCount(){
        userService.insertUser(user);
        when(userService.getLikeUserCount(user.getId())).thenReturn(0);
    }

    @Test
    public void testGetFollowerCount(){
        userService.insertUser(user);
        when(userService.getFollowerCount(user.getId())).thenReturn(0);
    }

    @Test
    public void testIsLike(){
        userService.insertUser(user);
        User user2 = new User("testuser2.getId()", "testUserName2", "testUserBio2", "testUserLocation2", "testUserCompany2", "testUserCreatedAt2", "testUserUpdatedAt2", "testUserEmail2", "testUserPassword2", 1, false, new Date(0), 0);
        userService.insertUser(user2);
        when(userService.isLike(user.getId(), user2.getId())).thenReturn(false);
    }

    @Test
    public void testPublishNotification(){
        userService.insertUser(user);
        when(userService.publishNotification(user.getId(), "testNotification")).thenReturn(true);
        when(userService.getNotifications()).thenReturn(null);
    }

}
