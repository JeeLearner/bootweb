package com.jee.boot.common.exception.user;

/**
 * 角色锁定异常类
 * 
 * @author jeeLearner
 */
public class RoleBlockedException extends UserException {
    private static final long serialVersionUID = 1L;

    public RoleBlockedException() {
        super("user.role.blocked", null);
    }
}
