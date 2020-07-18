package com.jee.boot.websocket.endpoint;

import com.jee.boot.common.utils.DateUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.websocket.support.AbstractWebSocketServer;
import com.jee.boot.websocket.support.CustomSession;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 访问信息 服务端
 *      此服务端功能：首页推送访问信息，为系统推送消息，不关注发送者
 *          后期优化点：根据数据权限发送相应消息
 *      {user}==> 当前连接的用户，由前端传值
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
@ServerEndpoint("/ws/{user}")
public class WSVisitInfo extends AbstractWebSocketServer {
    private static final Logger log = LoggerFactory.getLogger(WSVisitInfo.class);

    /**
     * 消息推送 入口
     * @param session
     * @param message
     * @param postUser
     * @throws IOException
     */
    @Override
    public void sendMessage(Session session, String message, String postUser) throws IOException {
        log.info("[WSVisitInfo.sendMessage] session:==>{}, message:==>{},postUser:==>{}",session, message, postUser);
            if (session == null) {
                //群发
                if (message != null){
                    for (CopyOnWriteArraySet<CustomSession> item : WEB_SOCKET_MAP.values()) {
                        for (CustomSession custom : item) {
                            try {
                                sendMessage(custom.getSession(), message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else {
                //单发
                if (message == null) {
                    //连接与断开消息
                    //session.getBasicRemote().sendText("系统默认数据");
                } else {
                    //正常发送
                    sendMessage(session, message);
                }
            }
    }

    /**
     * 发送消息 具体实现
     * @param session
     * @param message
     * @throws IOException
     */
    private void sendMessage(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    /**
     * 推送消息（生产者推送器）
     * @param message
     * @param postUser
     * @throws Exception
     */
    @Override
    public void pushMessage(String message, String postUser) throws Exception {
        //此场景无需关注postUser
        sendMessage(null, message, postUser);
    }

    @Override
    public String getProducer() {
        return "system";
    }

    @Override
    public void sendDefaultMessage(Session session, String user) {
        List<String> message = createMessage();
        for (String s : message) {
            try {
                sendMessage(session, s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> createMessage(){
        List<String> messages = new ArrayList<>();
        String[] emps = new String[]{"【人事部门】-张三", "【研发部门】-李四", "【测试部门】-王五"};
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 5; i++) {
            String emp = emps[RandomUtils.nextInt(0, emps.length)];
            messages.add(emp + "访问了系统  " + DateUtils.formatDateTime(now.plusSeconds(i)));
        }
        return messages;
    }

//    /**
//     * 实现服务器主动推送
//     */
//    private void pushMessage(String message) throws IOException {
//        this.session.getBasicRemote().sendText(message);
//    }
}

