package com.jee.boot.framework.aspectj;

import com.jee.boot.common.annotation.DataSource;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.framework.datasource.DynamicDataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 多数据源处理 Aspect
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
@Aspect
@Order(1)
public class DataSourceAspect {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.jee.boot.common.annotation.DataSource)"
        + "|| @within(com.jee.boot.common.annotation.DataSource)")
    public void dsPointCut(){
    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        DataSource dataSource = getDataSource(point);
        if (JeeStringUtils.isNotNull(dataSource)){
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().name());
        }
        try {
            return point.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    private DataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        //TODO
        Method method = signature.getMethod();
        System.out.println(method.getName());
        Class declaringType = signature.getDeclaringType();
        System.out.println(declaringType);



        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (JeeStringUtils.isNotNull(dataSource)){
            return dataSource;
        }
        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);

    }

}

