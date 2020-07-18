package com.jee.boot.websocketdemo;

import com.jee.boot.common.core.result.R;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * WebSocket 消息控制器
 *
 * @author jeeLearner
 * @version V1.0
 */
@RestController
@RequestMapping("/ws2")
public class WsController {

    @RequestMapping("/push/{toUserId}")
    public R pushToWeb(String message, @PathVariable String toUserId) throws IOException {
        //WebSocketServer.sendInfo(message,toUserId);
        return R.ok();
    }
}

