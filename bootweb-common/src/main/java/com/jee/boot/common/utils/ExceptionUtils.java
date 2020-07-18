package com.jee.boot.common.utils;

import com.jee.boot.common.utils.text.JeeStringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 错误信息处理类
 *
 * @author jeeLearner
 * @version V1.0
 */
public class ExceptionUtils {

    /**
     * 获取exception的详细错误信息。
     */
    public static String getExceptionMessage(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String str = sw.toString();
        return str;
    }

    public static String getRootErrorMseeage(Exception e) {
        Throwable root = org.apache.commons.lang3.exception.ExceptionUtils.getRootCause(e);
        root = (root == null ? e : root);
        if (root == null) {
            return "";
        }
        String msg = root.getMessage();
        if (msg == null) {
            return "null";
        }
        return JeeStringUtils.defaultString(msg);
    }
}

