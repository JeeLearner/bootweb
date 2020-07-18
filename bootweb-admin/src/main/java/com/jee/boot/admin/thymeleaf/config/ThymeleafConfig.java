package com.jee.boot.admin.thymeleaf.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jeeLearner
 * @version V1.0
 */
@Configuration
public class ThymeleafConfig {

    /**
     * thymeleaf模板引擎和shiro框架的整合
     */
    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
}

