package com.jee.boot.security.service;

import com.jee.boot.api.redis.RedisCache;
import com.jee.boot.api.redis.constant.RedisConstants;
import com.jee.boot.common.constant.Constants;
import com.jee.boot.common.exception.CustomException;
import com.jee.boot.common.exception.user.UserPasswordNotMatchException;
import com.jee.boot.common.exception.user.VerifyCodeException;
import com.jee.boot.common.exception.user.VerifyCodeExpireException;
import com.jee.boot.common.utils.MessageUtils;
import com.jee.boot.framework.config.properties.SysProperties;
import com.jee.boot.framework.manager.AsyncManager;
import com.jee.boot.framework.manager.factory.AsyncFactory;
import com.jee.boot.security.SecurityUtils;
import com.jee.boot.security.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录 服务层
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class SysLoginService {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private RedisCache redisCache;
    @Resource
    private AuthenticationManager authenticationManager;
    @Autowired
    private SysProperties sysProperties;


    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        if (sysProperties.isVerifyCodeEnabled()){
            String verifyKey = RedisConstants.VERIFY_CODE_KEY + uuid;
            String dbCode = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (dbCode == null){
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.verifycode.expire")));
                throw new VerifyCodeExpireException();
            }
            if (!code.equalsIgnoreCase(dbCode)) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.verifycode.error")));
                throw new VerifyCodeException();
            }
        }
        // 用户验证
        Authentication authentication = null;
        try{
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e){
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        LoginUser loginUser2 = (LoginUser) SecurityUtils.getAuthentication().getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }
}

