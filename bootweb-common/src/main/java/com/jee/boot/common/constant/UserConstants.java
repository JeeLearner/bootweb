package com.jee.boot.common.constant;

/**
 * 用户常量信息
 * 
 * @author jeeLearner
 */
public class UserConstants {
    /**
     * 平台内系统用户的唯一标志
     */
    public static final String SYS_USER = "SYS_USER";

    /** 正常状态 */
    public static final String STATUS_NORMAL = "0";

    /** 异常状态 */
    public static final String STATUS_EXCEPTION = "1";

    /** 封禁/停用状态 */
    public static final String STATUS_DISABLE = "1";

    /** 是否为系统默认（是） */
    public static final String YES = "Y";

    /**
     * 用户名长度限制
     */
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码长度限制
     */
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 20;

    /**
     * 用户类型
     */
    public static final String SYSTEM_USER_TYPE = "00";
    public static final String REGISTER_USER_TYPE = "01";
}