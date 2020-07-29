package com.jee.boot.shiro.exception;

import com.jee.boot.common.core.result.R;
import com.jee.boot.common.core.result.RCodeEnum;
import com.jee.boot.common.exception.base.BusinessException;
import com.jee.boot.common.utils.spring.ServletUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.dao.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jeeLearner
 * @version V1.0
 */
@RestControllerAdvice
public class ShiroExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ShiroExceptionHandler.class);

    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public R error(AuthorizationException e){
        log.error(e.getMessage(), e);
        return R.setResult(RCodeEnum.PERMISSION_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    public R error(DataAccessException e){
        log.error(e.getMessage(), e);
        return R.setResult(RCodeEnum.SQL_DATA_ACCESS_ERROR);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Object businessException(HttpServletRequest request, BusinessException e){
        log.error(e.getMessage(), e);
        if (ServletUtils.isAjaxRequest(request)){
            // return R.error().msg(PermissionUtils.getMsg(e.getMessage()));
            return R.error().msg(e.getMessage());
        } else {
            ModelAndView view = new ModelAndView();
            view.setViewName("error/unauth");
            return view;
        }
    }
}

