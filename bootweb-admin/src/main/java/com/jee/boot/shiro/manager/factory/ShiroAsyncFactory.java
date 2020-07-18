package com.jee.boot.shiro.manager.factory;

import com.jee.boot.common.utils.DateUtils;
import com.jee.boot.framework.manager.factory.AsyncFactory;
import com.jee.boot.shiro.session.OnlineSession;
import com.jee.boot.shiro.utils.ShiroUtils;
import com.jee.boot.common.constant.Constants;
import com.jee.boot.common.utils.LogUtils;
import com.jee.boot.common.utils.spring.ServletUtils;
import com.jee.boot.common.utils.spring.SpringUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.framework.utils.AddressUtils;
import com.jee.boot.system.dto.SysLogLoginDTO;
import com.jee.boot.system.dto.SysUserOnlineDTO;
import com.jee.boot.system.service.ISysLogLoginService;
import com.jee.boot.system.service.ISysUserOnlineService;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.TimerTask;

/**
 * Shiro 异步工厂
 *
 * @author jeeLearner
 * @version V1.0
 */
public class ShiroAsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 同步session到数据库
     *
     * @param session 在线用户会话
     * @return 任务task
     */
    public static TimerTask syncSessionToDb(OnlineSession session){
        return new TimerTask() {
            @Override
            public void run() {
                SysUserOnlineDTO online = new SysUserOnlineDTO();
                online.setSessionId(String.valueOf(session.getId()));
                online.setDeptName(session.getDeptName());
                online.setLoginName(session.getLoginName());
                online.setStartTimestamp(DateUtils.format(session.getStartTimestamp()));
                online.setLastAccessTime(DateUtils.format(session.getLastAccessTime()));
                online.setExpireTime(session.getTimeout());
                online.setIpaddr(session.getHost());
                online.setLoginLocation(AddressUtils.getRealAddressByIP(session.getHost()));
                online.setBrowser(session.getBrowser());
                online.setOs(session.getOs());
                online.setStatus(session.getStatus());
                SpringUtils.getBean(ISysUserOnlineService.class).insertUserOnline(online);
            }
        };
    }

}

