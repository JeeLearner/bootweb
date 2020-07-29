package com.jee.boot.common.core.result;

import com.jee.boot.common.constant.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一结果的一般形式：
 *      是否响应成功；
 *      响应状态码；
 *      状态码描述；
 *      响应数据
 *      其他标识符
 * @author jeeLearner
 *
 */
public class R {
    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 响应码
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String msg;
    /**
     * 响应数据
     */
    private Map<String, Object> data = new HashMap<>();

    /** 构造器私有 */
    private R(){}

    /** 通用返回成功 */
    public static R ok() {
        R r = new R();
        r.setSuccess(RCodeEnum.SUCCESS.getSuccess());
        r.setCode(RCodeEnum.SUCCESS.getCode());
        r.setMsg(RCodeEnum.SUCCESS.getMessage());
        return r;
    }

    /** 通用返回失败，未知错误 */
    public static R error() {
        R r = new R();
        r.setSuccess(RCodeEnum.UNKNOWN_ERROR.getSuccess());
        r.setCode(RCodeEnum.UNKNOWN_ERROR.getCode());
        r.setMsg(RCodeEnum.UNKNOWN_ERROR.getMessage());
        return r;
    }

    /**
     * 错误返回消息
     */
    public static R error(String msg) {
        R r = new R();
        r.setSuccess(false);
        r.setCode(HttpStatus.ERROR);
        r.setMsg(msg);
        return r;
    }

    /**
     * 错误返回消息
     */
    public static R error(int code, String msg) {
        R r = new R();
        r.setSuccess(false);
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    /** 设置结果，形参为结果枚举 */
    public static R setResult(RCodeEnum result) {
        R r = new R();
        r.setSuccess(result.getSuccess());
        r.setCode(result.getCode());
        r.setMsg(result.getMessage());
        return r;
    }

    /**------------使用链式编程，返回类本身-----------**/

    // 自定义返回数据
    public R data(Map<String,Object> map) {
        this.setData(map);
        return this;
    }

    // 通用设置data
    public R data(String key,Object value) {
        this.data.put(key, value);
        return this;
    }

    // 自定义状态信息
    public R msg(String msg) {
        this.setMsg(msg);
        return this;
    }

    // 自定义状态码
    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    // 自定义返回结果
    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }






    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}

