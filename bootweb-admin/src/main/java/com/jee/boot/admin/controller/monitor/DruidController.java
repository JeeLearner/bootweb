package com.jee.boot.admin.controller.monitor;

import com.jee.boot.common.core.web.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * druid 监控
 *
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/monitor/druid")
public class DruidController extends BaseController{
    private String prefix = "/druid";

    @RequiresPermissions("monitor:druid:view")
    @GetMapping()
    public String index(){
        return redirect(prefix + "/index");
    }
}