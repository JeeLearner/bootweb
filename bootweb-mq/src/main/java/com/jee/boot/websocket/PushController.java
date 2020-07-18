package com.jee.boot.websocket;

/**
 * @author jeeLearner
 * @version V1.0
 */

import com.jee.boot.common.core.result.R;
import com.jee.boot.websocket.endpoint.WSVisitInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WebSocket 消息客户端
 *
 * @author jeeLearner
 * @version V1.0
 */
@RestController
@RequestMapping("/ws/push")
public class PushController {

    @Autowired
    private WSVisitInfo wsService;

    /**
     * 推送首页访问信息
     * @param message
     * @return
     * @throws Exception
     */
    @RequestMapping("/visit/info")
    public R pushToWeb(String message) throws Exception {
        wsService.pushMessage(message, null);
        return R.ok();
    }
}

