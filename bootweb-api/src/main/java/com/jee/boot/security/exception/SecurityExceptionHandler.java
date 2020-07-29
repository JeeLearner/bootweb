package com.jee.boot.security.exception;

import com.jee.boot.common.constant.HttpStatus;
import com.jee.boot.common.core.result.R;
import com.jee.boot.common.exception.CustomException;
import com.jee.boot.common.utils.MessageUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author jeeLearner
 * @version V1.0
 */
@RestControllerAdvice
public class SecurityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(SecurityExceptionHandler.class);

    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public R businessException(CustomException e){
        log.error(e.getMessage(), e);
        if (JeeStringUtils.isNull(e.getCode())){
            return R.error(e.getMessage());
        }
        return R.error(e.getCode(), e.getMessage());
    }

    /**
     * 权限异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public R handleAuthorizationException(AccessDeniedException e) {
        log.error(e.getMessage());
        return R.error(HttpStatus.FORBIDDEN, MessageUtils.message("permission.forbidden"));
    }

    /**
     * 账号过期异常
     */
    @ExceptionHandler(AccountExpiredException.class)
    public R handleAccountExpiredException(AccountExpiredException e) {
        log.error(e.getMessage(), e);
        return R.error(e.getMessage());
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public R handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error(e.getMessage(), e);
        return R.error(e.getMessage());
    }
}

