package com.jee.boot.websocket.support;

import com.jee.boot.common.utils.text.JeeStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocketServer抽象类
 *
 * @author jeeLearner
 * @version V1.0
 */
public abstract class AbstractWebSocketServer implements IWebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(AbstractWebSocketServer.class);

    /** 推送消息实现（暂时，后期用mq或者redis） */
    public abstract void pushMessage(String message, String currentUser) throws Exception;
    /** 获取生产者名称 */
    public abstract String getProducer();
    /** 用户首次连接，发送的默认消息 */
    public abstract void sendDefaultMessage(Session session, String user);

    /**
     * 连接建立成功调用的方法
     */
    @Override
    @OnOpen
    public void onOpen(Session session, @PathParam("user") String user){
        addOnlineCount();
        log.info("新连接加入！用户名==>" + user + ",当前在线人数为:" + getOnlineCount());
        if (JeeStringUtils.isNotEmpty(user)){
            CopyOnWriteArraySet<CustomSession> set = WEB_SOCKET_MAP.get(user);
            if (JeeStringUtils.isEmpty(set)){
                set = new CopyOnWriteArraySet<>();
                set.add(new CustomSession(session, user));
                WEB_SOCKET_MAP.put(user, set);
            } else {
                set.add(new CustomSession(session, user));
            }
        }
        //首次连接，发送默认消息
        sendDefaultMessage(session, user);
    }

    /**
     * 连接关闭调用的方法
     */
    @Override
    @OnClose
    public void onClose(Session session) {
        //TODO
        WEB_SOCKET_SET.remove(this);
        System.out.println(WEB_SOCKET_MAP);
        WEB_SOCKET_MAP.forEach((key, value) -> {
            Iterator<CustomSession> iterator = value.iterator();
            while (iterator.hasNext()){
                if (iterator.next().getSession().getId().equals(session.getId())){
                    iterator.remove();
                }
            }
            if (JeeStringUtils.isEmpty(value)){
                WEB_SOCKET_MAP.remove(key);
            }
        });
        minusOnlineCount();
        log.info("有一连接退出！“,当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @Override
    @OnMessage
    public void onMessage(String message, Session session) {
        for (IWebSocketServer item : WEB_SOCKET_SET) {
            try {
                item.sendMessage(session, null, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接报错调用的方法
     *
     * @param session
     * @param error
     */
    @Override
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误：{}，Session ID：{}", error.getMessage(), session.getId());
        error.printStackTrace();
    }


    @Override
    public int getOnlineCount(){
        return ONLINE_COUNT.get();
    }

    @Override
    public void addOnlineCount(){
        ONLINE_COUNT.addAndGet(1);
    }

    @Override
    public void minusOnlineCount(){
        if (ONLINE_COUNT.get() > 0) {
            ONLINE_COUNT.decrementAndGet();
        }
    }
}

