package com.jee.boot.shiro.exception;

import com.jee.boot.common.core.result.R;
import com.jee.boot.common.core.result.RCodeEnum;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author jeeLearner
 * @version V1.0
 */
@RestControllerAdvice
public class ShiroExceptionHandler {


    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public R error(AuthorizationException e){
        e.printStackTrace();
        return R.setResult(RCodeEnum.PERMISSION_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    public R error(DataAccessException e){
        e.printStackTrace();
        return R.setResult(RCodeEnum.SQL_DATA_ACCESS_ERROR);
    }
}

