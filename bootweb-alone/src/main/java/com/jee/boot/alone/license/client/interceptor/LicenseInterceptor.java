package com.jee.boot.alone.license.client.interceptor;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jee.boot.alone.license.client.manager.ClientLicenseManager;
import com.jee.boot.alone.license.client.manager.LicenseVerify;
import com.jee.boot.common.core.result.R;
import de.schlichtherle.license.LicenseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 校验License证书有效性
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class LicenseInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(LicenseInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LicenseVerify licenseVerify = new LicenseVerify();
        boolean flag = licenseVerify.verify();
        if (!flag){
            //证书无效
            logger.error("证书验证==>证书无效");
            response.setCharacterEncoding("utf-8");
            R r = R.error().msg("您未安装证书或证书无效，请核查服务器是否取得授权或重新申请证书！");
            response.getWriter().write(JSON.toJSONString(r));
            return false;
        }
        return true;
    }

}

