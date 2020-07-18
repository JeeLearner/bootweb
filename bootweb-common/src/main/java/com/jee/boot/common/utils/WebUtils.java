package com.jee.boot.common.utils;

import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;

import static sun.net.www.protocol.http.HttpURLConnection.userAgent;

/**
 * 获取web浏览器工具类
 *
 * @author jeeLearner
 */
public class WebUtils {

    /**
     * 获取客户端操作系统
     */
    public static String getOs(HttpServletRequest request){
        return getUserAgent(request).getOperatingSystem().getName();
    }

    /**
     * 获取客户端浏览器
     */
    public static String getBrowser(HttpServletRequest request){
        return getUserAgent(request).getBrowser().getName();
    }

    public static UserAgent getUserAgent(HttpServletRequest request){
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        return userAgent;
    }

    /**
     * 获取系统的地址
     */
    public static String getSystemHost(HttpServletRequest request) {
        return request.getLocalAddr() + ":" + request.getLocalPort();
    }
}

