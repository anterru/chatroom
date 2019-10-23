package edu.udacity.java.nano.chat;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint("/chat/{username}")
public class WebSocketChatServer {

    private class User{
        Session session;
        String userName;

        public User(Session session, String userName) {
            this.session = session;
            this.userName = userName;
        }

        public Session getSession() {
            return session;
        }

        public void setSession(Session session) {
            this.session = session;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
    /**
     * All chat sessions.
     */
    private static Map<String, User> onlineSessions = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(WebSocketChatServer.class);

    private static void sendMessageToAll(String msg) {
        //TODO: add send message method.
        logger.debug("sending message to all");
        for (String id : onlineSessions.keySet()){
            try {
                onlineSessions.get(id).getSession().getBasicRemote().sendText(msg);
            }catch(Exception e){
                logger.error(String.valueOf(e));
            }
        }
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String user) {
        //TODO: add on open connection.
        try {
            onlineSessions.put(session.getId(), new User(session, user));
            sendMessageToAll(Message.strToJson("Joined the chat", user, onlineSessions.size()));
        }catch(Exception e){
            logger.error(String.valueOf(e));
        }
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        //TODO: add send message.
        try {
            Message message = JSON.parseObject(jsonStr, Message.class);
            sendMessageToAll(Message.strToJson(message.getMsg(), message.getUsername(), onlineSessions.size()));
        }catch(Exception e){
            logger.error(String.valueOf(e));
        }
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        //TODO: add close connection.
        String userName = onlineSessions.get(session.getId()).getUserName();
        onlineSessions.remove(session.getId());
        try {
            session.close();
        }catch(Exception e){
            logger.error(String.valueOf(e));
        }
        sendMessageToAll(Message.strToJson("Left the chat", userName, onlineSessions.size()));
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
