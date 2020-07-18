package com.jee.boot.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理并记录日志文件
 *
 * @author jeeLearner
 */
public class LogUtils {

    public static final Logger ERROR_LOG = LoggerFactory.getLogger("sys-error");
    public static final Logger ACCESS_LOG = LoggerFactory.getLogger("sys-access");

    public static String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}

