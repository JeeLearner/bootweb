package com.jee.boot.common.exception;

/**
 * 工具类异常
 *
 * @author jeeLearner
 * @version V1.0
 */
public class UtilException extends RuntimeException {
    private static final long serialVersionUID = -1404981908010678367L;

    public UtilException(Throwable e) {
        super(e.getMessage(), e);
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

