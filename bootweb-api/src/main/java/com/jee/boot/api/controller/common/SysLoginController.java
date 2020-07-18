package com.jee.boot.api.controller.common;

import com.jee.boot.common.core.result.R;
import com.jee.boot.security.constant.TokenConstants;
import com.jee.boot.security.entity.LoginBody;
import com.jee.boot.security.service.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录 控制器
 *
 * @author jeeLearner
 * @version V1.0
 */
@RestController
public class SysLoginController {

    @Autowired
    private SysLoginService loginService;

    /**
     * 登录
     */
    @PostMapping("/login")
    @ResponseBody
    public R login(@RequestBody LoginBody loginBody){ System.out.println(loginBody);
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        return R.ok().data(TokenConstants.TOKEN, token);
    }

    /**
     * 退出
     */
    @PostMapping("/logout")
    public String logout(ModelMap mmap){
        return "logout";
    }


}

