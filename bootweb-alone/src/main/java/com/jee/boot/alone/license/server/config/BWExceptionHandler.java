package com.jee.boot.alone.license.server.config;

import com.jee.boot.common.core.result.R;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.NoLicenseInstalledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.UnsupportedEncodingException;

/**
 * 异常配置
 *
 * @author jeeLearner
 * @version V1.0
 */
@RestControllerAdvice
public class BWExceptionHandler {

    /**-------- 指定异常处理方法 --------**/
    @ExceptionHandler(NoLicenseInstalledException.class)
    public R error(NoLicenseInstalledException e) {
        e.printStackTrace();
        return R.error().msg(e.getMessage());
    }

    /**-------- 指定异常处理方法 --------**/
    @ExceptionHandler(LicenseContentException.class)
    public R error(LicenseContentException e) {
        e.printStackTrace();
        return R.error().msg(e.getMessage());
    }

    /**-------- 指定异常处理方法 --------**/
    @ExceptionHandler(UnsupportedEncodingException.class)
    public R error(UnsupportedEncodingException e) {
        e.printStackTrace();
        return R.error().msg(e.getMessage());
    }

    /**-------- 通用异常处理方法 --------**/
    @ExceptionHandler(Exception.class)
    public R error(Exception e) {
        if (e instanceof LicenseContentException){
            return R.error().msg(e.getMessage());
        }
        e.printStackTrace();
        return R.error().msg(e.getMessage());
    }

}

