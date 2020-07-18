//package com.jee.boot.websocketdemo;
//
//import com.alibaba.fastjson.JSONObject;
//import com.jee.boot.common.utils.JsonUtils;
//import com.jee.boot.common.utils.text.JeeStringUtils;
//import com.jee.boot.websocketdemo.config.GlobalSocket;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.*;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * WebSocket服务端
// *
// * @author jeeLearner
// * @version V1.0
// */
//@Component
//@ServerEndpoint("/ws/{userId}")
//public class WebSocketServer {
//
//    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
//
//    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
//    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
//    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
//    private Session session;
//    /**接收userId*/
//    private String userId="";
//
//
//    /**
//     * 连接建立成功调用的方法
//     */
//    @OnOpen
//    public void onOpen(Session session, @PathParam("userId") String userId) {
//        this.session = session;
//        this.userId = userId;
//        if (webSocketMap.containsKey(userId)){
//            webSocketMap.remove(userId);
//            GlobalSocket.minusAndGetCount(1);
//            log.info("用户:"+userId+"，下线！");
//        }
//        webSocketMap.put(userId, this);
//        log.info("用户连接:"+userId+",当前在线人数为:" + GlobalSocket.addAndGetCount(1));
//
//        try {
//            sendMessage("连接成功");
//        } catch (IOException e) {
//            log.error("用户:"+userId+",连接失败!!!!!!");
//        }
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose() {
//        if (webSocketMap.containsKey(userId)) {
//            webSocketMap.remove(userId);
//            GlobalSocket.minusAndGetCount(1);
//            log.info("用户:"+userId+"，成功关闭连接！");
//        }
//        log.info("用户退出:" + userId + ",当前在线人数为:" + GlobalSocket.getCount());
//    }
//
//    @OnError
//    public void onError(Session session, Throwable error) {
//        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
//        error.printStackTrace();
//    }
//
//
//    /**
//     * 收到客户端消息后调用的方法
//     *
//     * @param message 客户端发送过来的消息
//     */
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        log.info("用户消息:" + userId + ",报文:" + message);
//        //可以群发消息
//        //消息保存到数据库、redis
//        if (JeeStringUtils.isNotBlank(message)){
//            try {
//                //解析发送的报文
//                JSONObject jsonObject = JsonUtils.toJSONObj(message);
//                //追加发送人(防止串改)
//                jsonObject.put("fromUserId", this.userId);
//                String toUserId = jsonObject.getString("toUserId");
//                //传送给对应toUserId用户的websocket
//                if (JeeStringUtils.isNotBlank(toUserId) && webSocketMap.containsKey(toUserId)){
//                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
//                } else {
//                    log.error("请求的userId:"+toUserId+"不在该服务器上,目前无法接收消息");
//                    //否则不在这个服务器上，发送到mysql或者redis
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 发送自定义消息
//     * */
//    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
//        log.info("发送消息到:"+userId+"，报文:"+message);
//        if(JeeStringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
//            webSocketMap.get(userId).sendMessage(message);
//        }else{
//            log.error("用户"+userId+",不在线！");
//        }
//    }
//
//
//    /**
//     * 实现服务器主动推送
//     */
//    private void sendMessage(String message) throws IOException {
//        this.session.getBasicRemote().sendText(message);
//    }
//
//
//}
//
