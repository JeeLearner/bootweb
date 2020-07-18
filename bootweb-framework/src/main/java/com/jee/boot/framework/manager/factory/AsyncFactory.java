package com.jee.boot.framework.manager.factory;

import com.jee.boot.common.constant.Constants;
import com.jee.boot.common.utils.LogUtils;
import com.jee.boot.common.utils.spring.ServletUtils;
import com.jee.boot.common.utils.spring.SpringUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.framework.service.ISysService;
import com.jee.boot.framework.utils.AddressUtils;
import com.jee.boot.system.dto.SysLogLoginDTO;
import com.jee.boot.system.dto.SysLogOperDTO;
import com.jee.boot.system.service.ISysLogLoginService;
import com.jee.boot.system.service.ISysLogOperService;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.TabableView;
import java.time.LocalDateTime;
import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author jeeLearner
 * @version V1.0
 */
public abstract class AsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");



    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOperLog(final SysLogOperDTO operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysLogOperService.class).insertLogOper(operLog);
            }
        };
    }

    /**
     * 记录登陆信息
     *
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message, final Object... args){
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));

        final String ip = SpringUtils.getBean(ISysService.class).getCurrentUserIp();
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLogLoginDTO logininfor = new SysLogLoginDTO();
                logininfor.setLoginName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                logininfor.setLoginTime(LocalDateTime.now());
                // 日志状态
                if (JeeStringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
                    logininfor.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(ISysLogLoginService.class).insertLogLogin(logininfor);
            }
        };
    }
}

