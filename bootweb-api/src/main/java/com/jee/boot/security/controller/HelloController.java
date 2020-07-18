package com.jee.boot.security.controller;

import com.jee.boot.common.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jeeLearner
 * @version V1.0
 */
@RestController
@RequestMapping("/")
public class HelloController {

    /**
     * 通用访问
     * @return
     */
    @GetMapping("/hello")
    public R hello(){
        return R.ok().data("test", "hello");
    }

    /**
     * 只有admin角色可以访问
     * @return
     */
    @GetMapping("/admin/hello")
    public String admin() {
        return "admin";
    }

    /**
     * 只有user角色可以访问
     *      并且admin可以访问
     * @return
     */
    @GetMapping("/user/hello")
    public String user() {
        return "user";
    }
}

