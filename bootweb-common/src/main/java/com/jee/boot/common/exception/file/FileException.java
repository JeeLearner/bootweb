package com.jee.boot.common.exception.file;

import com.jee.boot.common.exception.base.BaseException;

/**
 * 文件信息异常类
 *
 * @author jeeLearner
 * @version V1.0
 */
public class FileException extends BaseException {
    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }
}

