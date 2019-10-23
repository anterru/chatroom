package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * WebSocket message model
 */
public class Message {
    // TODO: add message model.
    // TODO: add message type variable which set type === "SPEAK" ||
    private String username;
    private String msg;
    private int numUsers;

    public Message(String msg, String username, int numUsers) {
        this.msg = msg;
        this.username = username;
        this.numUsers = numUsers;
    }

    public static String strToJson(String msg, String username, int numUsers) {
        return JSON.toJSONString(new Message(msg, username, numUsers));
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumUsers() {
        return numUsers;
    }

    public void setNumUsers(int numUsers) {
        this.numUsers = numUsers;
    }
}
