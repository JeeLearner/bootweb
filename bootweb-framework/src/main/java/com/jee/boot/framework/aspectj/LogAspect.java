package com.jee.boot.framework.aspectj;

import com.alibaba.fastjson.JSON;
import com.jee.boot.common.annotation.Log;
import com.jee.boot.common.enums.BusinessStatus;
import com.jee.boot.common.utils.spring.ServletUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.framework.manager.AsyncManager;
import com.jee.boot.framework.manager.factory.AsyncFactory;
import com.jee.boot.framework.service.ISysService;
import com.jee.boot.system.dto.SysLogOperDTO;
import com.jee.boot.system.dto.SysUserDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * 操作日志记录Aspect
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
@Aspect
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private ISysService sysService;

    // 配置织入点
    @Pointcut("@annotation(com.jee.boot.common.annotation.Log)")
    public void logPointCut(){
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturn(JoinPoint joinPoint, Object jsonResult){
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e){
        handleLog(joinPoint, e, null);
    }



    protected void handleLog(JoinPoint joinPoint, Exception e, Object jsonResult){
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null){
                return;
            }
            //获取当前用户
            SysUserDTO currentUser = sysService.getCurrentUser();

            // *========数据库日志=========*//
            SysLogOperDTO operLog = new SysLogOperDTO();
            operLog.setOperTime(LocalDateTime.now());
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            operLog.setOperIp(sysService.getCurrentUserIp());
            // 返回参数
            operLog.setJsonResult(JSON.toJSONString(jsonResult));
            operLog.setOperUrl(ServletUtils.getRequest().getRequestURI());

            if (currentUser != null){
                operLog.setOperName(currentUser.getLoginName());
                String deptName = Optional.ofNullable(currentUser.getDept())
                        .map(d -> d.getDeptName())
                        .orElse("");
                operLog.setDeptName(deptName);
            }
            if (e != null) {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(JeeStringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operLog);
            // 保存数据库
            AsyncManager.me().execute(AsyncFactory.recordOperLog(operLog));
        } catch (Exception ex){
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param controllerLog 日志
     * @param operLog 操作日志
     * @throws Exception
     */
    private void getControllerMethodDescription(JoinPoint joinPoint, Log controllerLog, SysLogOperDTO operLog) throws Exception {
        // 设置action动作
        operLog.setBusinessType(controllerLog.businessType().ordinal());
        // 设置标题
        operLog.setTitle(controllerLog.title());
        // 设置操作人类别
        operLog.setOperatorType(controllerLog.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (controllerLog.isSaveRequestData()){
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operLog);
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
//    private void setRequestValue(SysLogOperDTO operLog) {
//        Map<String, String[]> map = ServletUtils.getRequest().getParameterMap();
//        String params = JSON.toJSONString(map);
//        operLog.setOperParam(JeeStringUtils.substring(params, 0, 2000));
//    }
    private void setRequestValue(JoinPoint joinPoint, SysLogOperDTO operLog) throws Exception {
        String requestMethod = operLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            operLog.setOperParam(JeeStringUtils.substring(params, 0, 2000));
        } else {
            Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            operLog.setOperParam(JeeStringUtils.substring(paramsMap.toString(), 0, 2000));
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null){
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    Object jsonObj = JSON.toJSON(paramsArray[i]);
                    params += jsonObj.toString() + " ";
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }
}

