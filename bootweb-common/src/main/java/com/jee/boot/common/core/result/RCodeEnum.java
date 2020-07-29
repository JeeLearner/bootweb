package com.jee.boot.common.core.result;

public enum RCodeEnum {
    /** 通用 */
    SUCCESS(true, 0, "操作成功"),
    WARN(true, 1, "操作成功，警告:"),

    UNKNOWN_ERROR(false, -1,"未知错误"),
    MY_ERROR(false, -2,"自定义异常：%s"),

    /** 本系统错误 */
    PARAM_ERROR(false, 40000,"参数错误"),
    REQUEST_TYPE_ERROR(false, 40001, "请求类型错误"),
    PERMISSION_ERROR(false, 40002, "请求权限不足"),
    COMMON_NOFOUND_HANDLER(false, 40003, "请求路径错误"),
    COMMON_CHAR_ILLEGAL(false, 40004, "输入字符非法"),
    VERIFY_CODE_ERROR(false, 40005, "验证码错误"),
    USER_NOT_MATCH(false, 40006, "用户不存在/密码错误"),

    /** 调用第三方服务错误 */
    SQL_DATA_ACCESS_ERROR(false, 50001, "SQL数据验证失败"),
    ;

    // 响应是否成功
    private Boolean success;
    // 响应状态码
    private Integer code;
    // 响应信息
    private String message;

    RCodeEnum(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public RCodeEnum fillArgs(Object... args){
        this.message = String.format(this.message, args);
        return this;
    }



    public Boolean getSuccess() {
        return success;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
