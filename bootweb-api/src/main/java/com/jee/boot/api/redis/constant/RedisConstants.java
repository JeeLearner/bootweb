package com.jee.boot.api.redis.constant;

/**
 * redis 常量
 * @author jeeLearner
 * @version V1.0
 */
public class RedisConstants {

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";
    /**
     * 验证码 redis key
     */
    public static final String VERIFY_CODE_KEY = "login_codes:";
}

