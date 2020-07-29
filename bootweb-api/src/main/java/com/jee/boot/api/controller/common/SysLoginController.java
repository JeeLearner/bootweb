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
     *    request.header==> Authorization:eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjRiZmIzZTRmLTM0YjktNDk2NS1iZDRiLTNjMDkyOGJlYmRmNCJ9.2QLngblIWogAqEC_JB84V0WHKNBORLdIHzFisq6kUN5X4N2ig3WPPdZkEgbGNG3J0vsT9dMdmJaQ9b4nJHSn1w
     *    request.body==>
     *      {
     * 	        "username":"jee",
     * 	        "password":"admin"
     * 	    }
     */
    @PostMapping("/login")
    @ResponseBody
    public R login(@RequestBody LoginBody loginBody){ System.out.println(loginBody);
        // 生成令牌
        //eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6Ijk3OTRmM2EzLTczZmItNDY2MC1iZjM4LTA4OWI5ZjI4Zjc5YSJ9.pFtVwSfjP998akOBXBP0DT3PZr1lJY6REguNB7nAgNEwl9AWSzVSMVg3N0bSbV8vquWxOSpFQHnFEv8GZXAWtQ
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

