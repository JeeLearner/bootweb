package com.jee.boot.security.handle;

import com.jee.boot.common.constant.Constants;
import com.jee.boot.common.constant.HttpStatus;
import com.jee.boot.common.core.result.R;
import com.jee.boot.common.utils.JsonUtils;
import com.jee.boot.common.utils.spring.ServletUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.framework.manager.AsyncManager;
import com.jee.boot.framework.manager.factory.AsyncFactory;
import com.jee.boot.security.entity.LoginUser;
import com.jee.boot.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    TokenService tokenService;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (JeeStringUtils.isNotNull(loginUser)){
            String username = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.deleteLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGOUT, "退出成功"));
        }
        ServletUtils.renderString(response, JsonUtils.toString(R.error(HttpStatus.SUCCESS, "退出成功")));
    }
}

