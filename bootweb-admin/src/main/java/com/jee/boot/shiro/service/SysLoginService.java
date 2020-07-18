package com.jee.boot.shiro.service;

import com.jee.boot.framework.manager.factory.AsyncFactory;
import com.jee.boot.framework.verifycode.kaptcha.VerifyCodeContants;
import com.jee.boot.shiro.utils.ShiroUtils;
import com.jee.boot.common.constant.Constants;
import com.jee.boot.common.constant.UserConstants;
import com.jee.boot.common.enums.UserStatus;
import com.jee.boot.common.exception.user.*;
import com.jee.boot.common.utils.MessageUtils;
import com.jee.boot.common.utils.RegUtils;
import com.jee.boot.common.utils.spring.ServletUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.framework.manager.AsyncManager;
import com.jee.boot.system.dto.SysUserDTO;
import com.jee.boot.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 登录校验方法
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class SysLoginService {

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private ISysUserService userService;

    public SysUserDTO login(String username, String password) {
        // 验证码校验
        if (JeeStringUtils.isNotEmpty(ServletUtils.getRequest().getAttribute(VerifyCodeContants.CURRENT_CAPTCHA))){
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new VerifyCodeException();
        }
        // 用户名或密码为空 错误
        if (JeeStringUtils.isEmpty(username) || JeeStringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH){
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 查询用户信息
        SysUserDTO user = userService.getUserByLoginName(username);

        if (user == null && maybeMobilePhoneNumber(username)){
            user = userService.getUserByPhoneNumber(username);
        }
        if (user == null && maybeEmail(username)) {
            user = userService.getUserByEmail(username);
        }
        if (user == null){
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
            throw new UserNotExistsException();
        }
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.delete")));
            throw new UserDeleteException();
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked", user.getRemark())));
            throw new UserBlockedException();
        }
        //密码匹配
        passwordService.validate(user, password);

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        recordLoginInfo(user);
        return user;
    }

    /**
     * 记录登录信息
     */
    public void recordLoginInfo(SysUserDTO user) {
        user.setLoginIp(ShiroUtils.getIp());
        user.setLoginDate(LocalDateTime.now());
        userService.updateUserInfo(user);
    }

    private boolean maybeEmail(String username) {
        return username.matches(RegUtils.EMAIL_PATTERN);
    }

    private boolean maybeMobilePhoneNumber(String username) {
        return username.matches(RegUtils.MOBILE_PHONE_NUMBER_PATTERN);
    }
}

