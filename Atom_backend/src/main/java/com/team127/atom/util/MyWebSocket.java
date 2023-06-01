package com.team127.atom.util;

//import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team127.atom.configurations.WebSocketConfig;
import com.team127.atom.mapper.ChatMapper;
import com.team127.atom.model.ChatMessage;
import com.team127.atom.model.User;
import com.team127.atom.service.ChatService;
//import org.apache.catalina.core.ApplicationContext;
import com.team127.atom.service.impl.ChatServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

//import static com.team127.atom.web.controller.ChatController.getUsers;

@ServerEndpoint(value = "/websocket/{openid}",configurator= WebSocketConfig.class)
@Component
@Slf4j
@CrossOrigin
@RestController
public class MyWebSocket {

    //用来存放每个客户端对应的MyWebSocket对象。
    public void sendMessage(Session session, String message) throws IOException{
        if(session != null){
            synchronized (session) {
                log.info("发送数据:{}", message);
                session.getBasicRemote().sendText(message.toString());
            }
        }
    }

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
    //用来记录username和该session进行绑定
    private static Map<String, Session> map = new HashMap<String, Session>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //用户名
    private String username;

    private List<String> onlineUsers = new ArrayList<>();
    //获取全局容器
//    private ApplicationContext applicationContext;
    //聊天逻辑层service

    @Autowired
    ChatService chatService = new ChatServiceImpl();




    /**
     * 连接建立成功调用的方法，初始化昵称、session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "openid")String openid,EndpointConfig config) {


        //获取登录时存放httpSession的用户数据
        HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(httpSession.getServletContext());

//        User user = (User) httpSession.getAttribute("user");
        User user = new User();
        user.setName(openid);
        onlineUsers.add(openid);

//        this.applicationContext = applicationContext;
        this.session = session;
        this.username = user.getName();
        chatService = applicationContext.getBean(ChatService.class);
//        this.chatService = chatService ;

//        chatService

        //绑定username与session
        map.put(username, session);

//        try {
//            sendMessage(session, "你好");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        webSocketSet.add(this);     //加入set中

    }

//    public List<User> getOnlineUsers(){
//        return getUsers(username, chatService, onlineUsers);
//    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {

        //断开连接，重置窗口值
//        chatService.resetWindows(username);
        onlineUsers.remove(username);
        webSocketSet.remove(this);  //从set中删除
        for (String key: map.keySet()) {
            if(onlineUsers.contains(key)){
                map.get(key).getAsyncRemote().sendText(username + "离线了");
            }
        }

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {

        //从客户端传过来的数据是json数据，所以这里使用jackson进行转换为chatMsg对象，
        ObjectMapper objectMapper = new ObjectMapper();
        ChatMessage chatMsg;

        System.out.println(map);
            try {
//                sendMessage(session, message);
                chatMsg = objectMapper.readValue(message, ChatMessage.class);

                //对chatMsg进行装箱
                chatMsg.setFromUser(username);
                chatMsg.setSendTime(new java.sql.Date(System.currentTimeMillis()));
                chatMsg.setLatest(true);

                Session fromSession = map.get(chatMsg.getFromUser());
                System.out.println("fromSession" + fromSession);
                Session toSession = map.get(chatMsg.getToUser());
                System.out.println("toSession" + toSession);

                //发送给接收者.
//            fromSession.getAsyncRemote().sendObject(chatMsg);
                System.out.println(JSONObject.toJSONString(chatMsg));
//                fromSession.getAsyncRemote().sendText(JSONObject.toJSONString(chatMsg));

                if (toSession != null) {
                    //发送给发送者.
                    toSession.getAsyncRemote().sendText(JSONObject.toJSONString(chatMsg));
//                fromSession.getAsyncRemote().sendObject(chatMsg);
                }

                //判断两者是否第一次聊天，进行关系表、聊天列表、空白信息的初始化
//            chatService.isFirstChat(chatMsg.getFromUser(), chatMsg.getToUser());

                //查询聊天两者的联系id
                String linkId = chatService.selectAssociation(username, chatMsg.getToUser());

                chatMsg.setLinkId(linkId);

                System.out.println("chatMsg: " + chatMsg);

                //保存聊天记录信息
                chatService.saveMessage(chatMsg);

                if (!onlineUsers.contains(chatMsg.getToUser())) {
                    chatService.updateUnread(username, chatMsg.getToUser());
                }

            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 群发自定义消息
     */
    public void broadcast(String message) {
        for (MyWebSocket item : webSocketSet) {

            //异步发送消息.
            item.session.getAsyncRemote().sendText(message);
        }
    }
}

