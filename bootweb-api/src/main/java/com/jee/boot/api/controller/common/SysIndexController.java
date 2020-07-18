package com.jee.boot.api.controller.common;

import com.jee.boot.common.core.result.R;
import com.jee.boot.security.entity.LoginBody;
import com.jee.boot.security.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 首页 控制器
 *
 * @author jeeLearner
 * @version V1.0
 */
@Controller
public class SysIndexController {


    /**
     * 系统首页
     */
    @PostMapping("/index")
    public String index(ModelMap mmap){
        return "index";
    }

}

