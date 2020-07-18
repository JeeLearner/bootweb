package com.jee.boot.shiro.web.filter.verifycode;

import com.jee.boot.framework.config.properties.SysProperties;
import com.jee.boot.framework.verifycode.kaptcha.VerifyCodeContants;
import com.jee.boot.shiro.utils.ShiroUtils;
import com.jee.boot.common.core.result.RCodeEnum;
import com.jee.boot.common.utils.text.JeeStringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 验证码过滤器
 *
 * @author jeeLearner
 * @version V1.0
 */
public class VerifyCodeFilter extends AccessControlFilter {

    @Autowired
    private SysProperties sysProperties;

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        request.setAttribute(VerifyCodeContants.VERIFY_CODE__TYPE, sysProperties.getVerifyCodeType());
        request.setAttribute(VerifyCodeContants.VERIFY_CODE_ENABLED, sysProperties.isVerifyCodeEnabled());
        return super.onPreHandle(request, response, mappedValue);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // 验证码禁用 或不是表单提交 允许访问
        if (sysProperties.isVerifyCodeEnabled() == false || !"post".equals(httpServletRequest.getMethod().toLowerCase())){
            return true;
        }
        return validateResponse(httpServletRequest, httpServletRequest.getParameter(VerifyCodeContants.VERIFY_CODE));
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        request.setAttribute(VerifyCodeContants.CURRENT_CAPTCHA, RCodeEnum.VERIFY_CODE_ERROR.getMessage());
        return true;
    }

    public boolean validateResponse(HttpServletRequest request, String validateCode){
        String code = ShiroUtils.getVerifyCode();
        if (JeeStringUtils.isEmpty(validateCode) || !validateCode.equalsIgnoreCase(code)){
            return false;
        }
        return true;

    }
}

