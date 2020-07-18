package com.jee.boot.common.exception.user;

/**
 * 验证码失效异常类
 *
 * @author jeeLearner
 * @version V1.0
 */
public class VerifyCodeExpireException extends UserException {
    private static final long serialVersionUID = 1L;

    public VerifyCodeExpireException() {
        super("user.verifycode.expire", null);
    }
}

