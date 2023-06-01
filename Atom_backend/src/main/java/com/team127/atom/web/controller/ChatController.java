package com.team127.atom.web.controller;
import com.team127.atom.model.*;
import com.team127.atom.service.ChatService;
import com.team127.atom.service.ForumService;
import com.team127.atom.service.UserService;
import com.team127.atom.service.impl.UserServiceImpl;
import com.team127.atom.util.HashGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class ChatController {


    @Autowired
    UserService userService;

    @Autowired
    ChatService chatService;

//    @Autowired
//    UserService userService = new UserServiceImpl();

    private final List<String> onlineUsers = new ArrayList<>();



    @PostMapping("/getUserList")
    public List<User> getOnlineUsers(@RequestParam("username") String username){
        onlineUsers.add(username);
        return getUsers(username, chatService, onlineUsers);
    }

    @PostMapping("/offline")
    public Response offline(@RequestParam("username") String username){
        onlineUsers.remove(username);

        return Response.success("成功离线");
    }

    public List<User> getUsers(String username, ChatService chatService, List<String> onlineUsers) {
//        List<User> userList= chatService.getFromUserChatList(username);
        User user1 = userService.getUserByName(username);
//        System.out.println(user1);
        List<User> userList = userService.getFriends(user1.getId());
        for( User user:userList){
            if(onlineUsers.contains(user.getName())){
                user.setOnline(true);
                user.setRecentTime(chatService.getOffLineTime(user.getName()));
                user.setUnread(chatService.getUnread(user.getName(), username));
            }
        }
        return userList;
    }


    @PostMapping("/inChat")
    public Response stepInChat(@RequestParam("from_user") String fromUser,
                               @RequestParam("to_user")String toUser){
        chatService.isFirstChat(fromUser, toUser);
        List<ChatMessage> chatMessageList = chatService.getRecentChatRecords(fromUser, toUser, 10);
        chatService.changeWindow(fromUser);
        chatService.clearUnread(fromUser, toUser);
        return Response.success(chatMessageList);
    }

    @PostMapping("/send")
    public Response send(@RequestParam("message") ChatMessage message){
        chatService.saveMessage(message);
        return Response.success("发送成功。数据已保存");
    }
}
