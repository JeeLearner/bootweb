package com.jee.boot.framework.config;

import com.jee.boot.common.config.BWProp;
import com.jee.boot.common.utils.spring.SpringUtils;
import com.jee.boot.framework.interceptor.SameUrlDataInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置
 *
 * @author jeeLearner
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger/**").addResourceLocations("classpath:/static/swagger3/");
        /** 文件上传路径 */
        registry.addResourceHandler("/profile/**").addResourceLocations("file:" + BWProp.getProfile());

        /** swagger配置 */
        //registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        //registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //国际化
        registry.addInterceptor(SpringUtils.getBean(I18nConfig.class).localeChangeInterceptor());
        //可重复提交
        registry.addInterceptor(SpringUtils.getBean(SameUrlDataInterceptor.class));
    }


}

