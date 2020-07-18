package com.jee.boot.framework.interceptor;

import com.jee.boot.common.annotation.RepeatSubmit;
import com.jee.boot.common.exception.base.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 防止重复提交拦截器
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
public abstract class AbstractRepeatSubmitInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null){
                if (this.isRepeatSubmit(request)){
//                    R r = R.error().msg("不允许重复提交，请稍后再试");
//                    ServletUtils.renderString(response, JsonUtils.toString(r));
                    throw new BusinessException("不允许重复提交，请稍后再试");
                }
            }
            return true;
        } else {
            super.preHandle(request, response, handler);
        }

        return true;
    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     *
     * @param request
     * @return
     * @throws Exception
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request) throws Exception;
}

