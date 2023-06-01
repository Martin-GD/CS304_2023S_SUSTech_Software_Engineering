package com.team127.atom.web.controller;

import com.team127.atom.model.Notification;
import com.team127.atom.model.User;
import com.team127.atom.model.Response;
import com.team127.atom.service.UserService;
import com.team127.atom.util.HashGenerator;
import com.team127.atom.util.JWTUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "Login", notes = "Login")
    @PostMapping("/login")
    public Response login(@RequestBody User user) {
        User userDB = userService.getUserByName(user.getName());
        if (userDB == null) {
            return Response.error(1000, "User does not exist");
        } else if (userDB.getPassword().equals(user.getPassword())) {
            return Response.success(JWTUtil.sign(userDB.getId(), userDB.getPassword()));
        } else {
            return Response.error(1001, "Wrong password");
        }
    }

    @ApiOperation(value = "Register", notes = "Register")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "user", required = true, dataType = "User")
    })
    @PostMapping("/register")
    public Response register(@RequestBody User user) {
        User userDB = userService.getUserByName(user.getName());
        if (userDB != null) {
            System.out.println("bug");
            return Response.error(1002, "User already exists");
        }
        String timeStamp = String.valueOf(System.currentTimeMillis());
        user.setCreatedAt(timeStamp);
        user.setUpdatedAt(timeStamp);
        user.setId(HashGenerator.hash(user.getName(), user.getCreatedAt()));
        user.setAuth(1);
        if (userService.insertUser(user)) {
            return Response.success(null);
        } else {
            System.out.println("bug");
            return Response.error(400, "Register failed");
        }

    }

    @PostMapping("/modifyProfile")
    public Response modifyProfile(@RequestParam("token") String token,
                                  @RequestParam("company") String com,
                                  @RequestParam("location") String location,
                                  @RequestParam("bio") String bio) {
        User userDB = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, userDB.getId(), userDB.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        userDB.setCompany(com);
        userDB.setLocation(location);
        userDB.setBio(bio);
//        userDB.setUpdatedAt(System.currentTimeMillis());
        if (userService.saveUser(userDB)) {
            return Response.success(null);
        } else {
            return Response.error(400, "Modify profile failed");
        }
    }

    @PostMapping("/modifyPassword")
    public Response modifyPassword(@RequestParam("token") String token,
            @RequestBody String password) {
        User userDB = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, userDB.getId(), userDB.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        userDB.setPassword(password);
        userDB.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
        if (userService.saveUser(userDB)) {
            return Response.success(null);
        } else {
            return Response.error(400, "Modify password failed");
        }
    }

    @PostMapping("/logout")
    public Response logout(@RequestParam("username") String username) {
        // It's unnecessary to do anything here, frontend should handle this
        return Response.success(null);
    }

    @PostMapping("/uploadAvatar")
    public Response changeAvatar(@RequestParam("token") String token,
            @RequestBody MultipartFile avatar) {
        User userDB = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, userDB.getId(), userDB.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        try {
            userService.uploadAvatar(userDB.getId(), avatar.getBytes());
            String url = "http://10.26.11.88:8080/avatar/" + userDB.getId();
            return Response.success(url);
        } catch (Exception e) {
            return Response.error(400, "Upload avatar failed");
        }
    }

    @PostMapping("/uploadAvatar1")
    public Response changeAvatar1(@RequestParam("user") String userid,
                                 @RequestBody MultipartFile avatar) {
        User userDB = userService.getUserById(userid);
        try {
            userService.uploadAvatar(userDB.getId(), avatar.getBytes());
            String url = "http://10.26.11.88:8080/avatar/" + userDB.getId();
            return Response.success(url);
        } catch (Exception e) {
            return Response.error(400, "Upload avatar failed");
        }
    }

    @GetMapping("/getProfile")
    public Response getProfile(@RequestParam("userId") String userId) {
        try {
            User userDB = userService.getUserById(userId);
            return Response.success(userDB);
        } catch (Exception e) {
            return Response.error(1005, "Get profile failed");
        }
    }

    @GetMapping("/getIdByName")
    public Response getIdByName(@RequestParam("name") String name) {
        try {
            User user = userService.getUserByName(name);
            return Response.success(user);
        } catch (Exception e) {
            return Response.error(505, "Get user fail.");
        }
    }

    @GetMapping(value = "/avatar/{userId}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    @ResponseBody
    public byte[] avatar(@PathVariable String userId) {
        byte[] data = null;
        data = userService.getAvatar(userId);
        return data;
    }

    @GetMapping("/getAvatar")
    public Response getAvatar(@RequestParam("token") String token) {
        User userDB = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, userDB.getId(), userDB.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        try {
            byte[] avatar = userService.getAvatar(userDB.getId());
            return Response.success(avatar);
        } catch (Exception e) {
            return Response.error(400, "Get avatar failed");
        }
    }

    @GetMapping("/getlikeUser")
    public Response getlikeUser(@RequestParam("userId") String userId) {
        try {
            List<User> userList = userService.getLikeUser(userId);
            return Response.success(userList);
        } catch (Exception e) {
            return Response.error(1005, "Get like user failed");
        }
    }

    @GetMapping("/getLikeUserCount")
    public Response getLikeUserCount(@RequestParam("userId") String userId) {
        try{
            int count = userService.getLikeUserCount(userId);
            return Response.success(count);
        }catch(Exception e){
            return Response.error(1005, "Get like user count failed");
        }
    }

    @GetMapping("/getFollowerCount")
    public Response getFollowerCount(@RequestParam("userId") String userId) {
        try{
            int count = userService.getFollowerCount(userId);
            return Response.success(count);
        }catch(Exception e){
            return Response.error(1005, "Get like follower count failed");
        }
    }

    @PostMapping("/likeUser")
    public Response likeUser(@RequestParam("token") String token,
            @RequestParam("likeUserId") String likeUserId) {
        User userDB = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, userDB.getId(), userDB.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        try {
            if (!userService.likeUser(userDB.getId(), likeUserId)) {
                return Response.error(400, "Like user failed");
            }
            return Response.success(null);
        } catch (Exception e) {
            return Response.error(400, "Like user failed");
        }
    }

    @PostMapping("/unlikeUser")
    public Response unlikeUser(@RequestParam("token") String token,
            @RequestParam("likeUserId") String likeUserId) {
        User userDB = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, userDB.getId(), userDB.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        try {
            if (!userService.unlikeUser(userDB.getId(), likeUserId)) {
                return Response.error(400, "Unlike user failed");
            }
            return Response.success(null);
        } catch (Exception e) {
            return Response.error(400, "Unlike user failed");
        }
    }

    @PostMapping("/publishNotification")
    public Response publish(@RequestParam("title") String title,
                            @RequestParam("content") String content){
        userService.publishNotification(title, content);
        return Response.success(null);
    }

    @GetMapping("/getNotifications")
    public Response getNotifications() {
        List<Notification> list = userService.getNotifications();
        return Response.success(list);
    }

    @GetMapping("/getRecommendUser")
    public Response getRecommendUser(@RequestParam("userId") String userId){
        try{
            List<User> users = userService.getRecommendUsers(userId);
            return Response.success(users);
        }catch(Exception e){
            return Response.error(1005, "Get recommand user failed");
        }
    }

    @GetMapping("/isLike")
    public Response isLike(@RequestParam("name") String username, @RequestParam("token") String token){
        User userDB = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, userDB.getId(), userDB.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        return Response.success(userService.isLike(userDB.getId(), userService.getUserByName(username).getId()));
    }
}