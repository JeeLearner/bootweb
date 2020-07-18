package com.jee.boot.websocket.support;

import javax.websocket.Session;

/**
 * 自定义CustomSession
 *
 * @author jeeLearner
 * @version V1.0
 */
public class CustomSession {

    /** 考虑到同一用户可能在不同设备在线，保存为list结构 */
    private Session session;
    private String user;

    public CustomSession() {
    }

    public CustomSession(Session session, String user) {
        this.session = session;
        this.user = user;
    }


    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

