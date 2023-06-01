package com.team127.atom.service;

import com.team127.atom.model.ChatMessage;
import com.team127.atom.model.Response;
import com.team127.atom.model.User;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public interface ChatService {

    /**
     * 查询聊天双方的关联id
     * @param fromUser
     * @param toUser
     * @return
     */
    String selectAssociation(String fromUser, String toUser);

    /**
     * 是否第一次聊天
     * @param fromUser
     * @param toUser
     * @return
     */
    void isFirstChat(String fromUser, String toUser);

    /**
     * 保存聊天记录
     * @param chatMessage
     * @return
     */
    void saveMessage(ChatMessage chatMessage);

    /**
     * 获取当前用户的聊天列表
     * @param fromUser
     * @return
     */
    List<User> getFromUserChatList(String fromUser);

    /**
     * 获取发送者与接收者的最近的聊天记录
     * @param fromUser
     * @param toUser
     * @param currentIndex
     * @return
     */
    List<ChatMessage> getRecentChatRecords(String fromUser, String toUser, int currentIndex);

    /**
     * 更新是否在同一窗口值
     * @param fromUser
     * @param toUser
     */
    void updateWindows(String fromUser, String toUser);


    /**
     * 获取当前用户的未读数
     * @param username
     * @return
     */
    Integer getUnreadTotalNumber(String username);
    
    Integer getUnread(String fromUser, String toUser);

    /**
     *
     * @param username
     */
    void resetWindows(String username);

    void changeWindow(String fromUser);

    Date getOffLineTime(String name);

    void clearUnread(String fromUser, String toUser);

    void updateUnread(String username, String toUser);

//
//    List<User> onlineUsers = new ArrayList<>();
//
//    List<User> getOnlineUsers();
//
//    void setOnlineUsers(List<User> users);
}


