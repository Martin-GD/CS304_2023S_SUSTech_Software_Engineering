package com.team127.atom.mapper;

import com.team127.atom.model.ChatMessage;
import com.team127.atom.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;

@Mapper
public interface ChatMapper {

    String selectLinkId(String fromUser, String toUser);

    int isFirst(String fromUser, String toUser);

    void insertChatList(String linkId, String fromUser, String toUser);

    void insertChatUserLink(String linkId, String fromUser, String toUser, java.util.Date date);

    void resetWindows(String username);

    List<ChatMessage> selectRecentChat(String fromUser, String toUser, int currentIndex);

    void saveMessage(ChatMessage chatMessage);

    void changeWindow(String fromUser);

    List<User> selectUserChatList(String fromUser);

    Date selectOfflineTime(String name);

    Integer selectUnreadTotalNumber(String username);
    
    Integer selectUnread(String fromUser, String toUser);

    void clearUnread(String fromUser, String toUser);

    void updateUnread(String username, String toUser);
}