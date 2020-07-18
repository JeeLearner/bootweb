package com.jee.boot.framework.verifycode.kaptcha;

import com.google.code.kaptcha.Constants;

/**
 * @author jeeLearner
 * @version V1.0
 */
public class VerifyCodeContants {

    public static final String KAPTCHA_SESSION_KEY = Constants.KAPTCHA_SESSION_KEY;

    /**
     * 验证码开关
     */
    public static final String VERIFY_CODE_ENABLED = "verifyCodeEnabled";

    /**
     * 验证码类型
     */
    public static final String VERIFY_CODE__TYPE = "verifyCodeType";
    /**
     * 验证码
     */
    public static final String VERIFY_CODE = "verifyCode";
    /**
     * 验证码key（VerifyCodeFilter失败后将这个字段添加到request，login时如果这个字段存在于request中则代表验证码验证失败）
     */
    public static final String CURRENT_CAPTCHA = "captcha";
}

