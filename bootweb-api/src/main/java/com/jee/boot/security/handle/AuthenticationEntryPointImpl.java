package com.jee.boot.security.handle;

import com.jee.boot.common.constant.HttpStatus;
import com.jee.boot.common.core.result.R;
import com.jee.boot.common.utils.JsonUtils;
import com.jee.boot.common.utils.spring.ServletUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        int code = HttpStatus.UNAUTHORIZED;
        String msg = JeeStringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        ServletUtils.renderString(response, JsonUtils.toString(R.error(code, msg)));
    }
}

