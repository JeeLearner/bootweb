package com.jee.boot.shiro.service;

import com.jee.boot.common.utils.DateUtils;
import com.jee.boot.shiro.session.OnlineSession;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.system.dto.SysUserOnlineDTO;
import com.jee.boot.system.service.ISysUserOnlineService;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 会话db操作处理
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class SysShiroService {

    @Autowired
    private ISysUserOnlineService userOnlineService;

    /**
     * 删除会话
     *
     * @param onlineSession 会话信息
     */
    public void deleteSession(OnlineSession onlineSession) {
        userOnlineService.deleteUserOnlineById(String.valueOf(onlineSession.getId()));
    }

    /**
     * 获取会话信息
     *
     * @param sessionId
     * @return
     */
    public Session getSession(Serializable sessionId){
        SysUserOnlineDTO userOnline = userOnlineService.getUserOnlineById(String.valueOf(sessionId));
        return JeeStringUtils.isNull(userOnline) ? null : createSession(userOnline);
    }

    private Session createSession(SysUserOnlineDTO userOnline) {
        OnlineSession onlineSession = new OnlineSession();
        onlineSession.setId(userOnline.getSessionId());
        onlineSession.setHost(userOnline.getIpaddr());
        onlineSession.setBrowser(userOnline.getBrowser());
        onlineSession.setOs(userOnline.getOs());
        onlineSession.setDeptName(userOnline.getDeptName());
        onlineSession.setLoginName(userOnline.getLoginName());
        onlineSession.setStartTimestamp(DateUtils.parse(userOnline.getStartTimestamp()));
        onlineSession.setLastAccessTime(DateUtils.parse(userOnline.getLastAccessTime()));
        onlineSession.setTimeout(userOnline.getExpireTime());
        return onlineSession;
    }
}

