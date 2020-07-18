package com.jee.boot.websocket.support;

import javax.websocket.*;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocketServer接口
 *
 * @author jeeLearner
 * @version V1.0
 */
public interface IWebSocketServer {
    /** 记录当前在线连接数 */
    AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    /** 存放每个客户端对应的WebSocketServer对象 */
    CopyOnWriteArraySet<IWebSocketServer> WEB_SOCKET_SET = new CopyOnWriteArraySet<>();
    /** 存放Session对应员工，用于单对单发送消息 */
    ConcurrentHashMap<String, CopyOnWriteArraySet<CustomSession>> WEB_SOCKET_MAP = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    void onOpen(Session session, String user);

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    void onClose(Session session);

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    void onMessage(String message, Session session);

    /**
     * 连接报错调用的方法
     * @param session
     * @param error
     */
    @OnError
    void onError(Session session, Throwable error);

    /**
     * 发送消息
     * @param session
     * @param message
     * @param currentUser
     */
    void sendMessage(Session session, String message, String currentUser) throws IOException;

    int getOnlineCount();

    void addOnlineCount();

    void minusOnlineCount();
}
