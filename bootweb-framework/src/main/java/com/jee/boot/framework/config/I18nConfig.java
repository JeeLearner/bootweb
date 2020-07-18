package com.jee.boot.framework.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 国际化资源配置
 *
 * @author jeeLearner
 */
@Configuration
public class I18nConfig {

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // 参数名
        lci.setParamName("lang");
        return lci;
    }

//    @Bean
//    public LocaleResolver localeResolver() {
//        SessionLocaleResolver slr = new SessionLocaleResolver();
//        // 默认语言
//        slr.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
//        return slr;
//    }

    @Bean
    public LocaleResolver localeResolver() {
        return new MyLocaleResolver();
    }
}

class MyLocaleResolver implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        // 从参数中取l的值作为语言和国家代码
        String lang = httpServletRequest.getParameter("lang");

        if (!StringUtils.isEmpty(lang)) {
            String[] splits = lang.split("_");
            return new Locale(splits[0], splits[1]);
        } else {
            // 从请求头中取Accept-Language的首个语言和国家代码
//            String acceptLanguage = httpServletRequest.getHeader("Accept-Language").split(",")[0];
//            String[] splits = acceptLanguage.split("-");
//            return new Locale(splits[0], splits[1]);
            return Locale.getDefault();
        }
        // 如果想使用当前系统的语言，则使用Locale.getDefault()
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}