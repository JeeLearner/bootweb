package com.jee.boot.common.exception.user;

/**
 * 验证码错误异常类
 * 
 * @author jeeLearner
 */
public class VerifyCodeException extends UserException {
    private static final long serialVersionUID = 1L;

    public VerifyCodeException() {
        super("user.verifycode.error", null);
    }
}
