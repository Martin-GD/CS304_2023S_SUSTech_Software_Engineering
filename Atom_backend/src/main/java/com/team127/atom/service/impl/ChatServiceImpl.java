package com.team127.atom.service.impl;


import com.team127.atom.mapper.ChatMapper;
import com.team127.atom.model.ChatMessage;
import com.team127.atom.model.Response;
import com.team127.atom.model.User;
import com.team127.atom.service.ChatService;
import com.team127.atom.util.HashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    ChatMapper chatMapper;

    List<User> onlineUsers;



    @Override
    public String selectAssociation(String fromUser, String toUser) {
        return chatMapper.selectLinkId(fromUser, toUser);
    }

    @Override
    public void isFirstChat(String fromUser, String toUser) {
        Date date =new Date(System.currentTimeMillis());
        if (chatMapper.isFirst(fromUser, toUser)==0){
            String linkId = HashGenerator.hash(fromUser, toUser);
            String linkId2 = HashGenerator.hash(toUser, fromUser);
            chatMapper.insertChatUserLink(linkId, fromUser, toUser, date);
            chatMapper.insertChatUserLink(linkId2, toUser, fromUser, date);
            chatMapper.insertChatList(linkId, fromUser, toUser);
            chatMapper.insertChatList(linkId2, toUser, fromUser);
        }

    }

    @Override
    public void saveMessage(ChatMessage chatMessage) {
        chatMapper.saveMessage(chatMessage);

    }

    @Override
    public List<User> getFromUserChatList(String fromUser) {
        return chatMapper.selectUserChatList(fromUser);
    }

    @Override
    public List<ChatMessage> getRecentChatRecords(String fromUser, String toUser, int currentIndex) {
        List<ChatMessage> chatMessageList = chatMapper.selectRecentChat(fromUser, toUser, currentIndex);
        return chatMessageList;
    }

    @Override
    public void updateWindows(String fromUser, String toUser) {


    }

    @Override
    public Integer getUnreadTotalNumber(String username) {
        return chatMapper.selectUnreadTotalNumber(username);
    }

    @Override
    public Integer getUnread(String fromUser, String toUser) {
        return chatMapper.selectUnread(fromUser, toUser);
    }

    @Override
    public void resetWindows(String username) {
        chatMapper.resetWindows(username);

    }

    @Override
    public void changeWindow(String fromUser) {
        chatMapper.changeWindow(fromUser);
    }

    @Override
    public Date getOffLineTime(String name) {
        return chatMapper.selectOfflineTime(name);
    }

    @Override
    public void clearUnread(String fromUser, String toUser) {
        chatMapper.clearUnread(fromUser, toUser);
    }

    @Override
    public void updateUnread(String username, String toUser) {
        chatMapper.updateUnread(username, toUser);
    }

}
