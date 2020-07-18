package com.jee.boot.common.exception.user;

import com.jee.boot.common.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author jeeLearner
 */
public class UserException extends BaseException{

    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}

