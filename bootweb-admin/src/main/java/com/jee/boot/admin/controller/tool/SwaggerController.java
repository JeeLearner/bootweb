package com.jee.boot.admin.controller.tool;

import com.jee.boot.common.core.web.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * swagger 控制层
 *
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/tool/swagger")
public class SwaggerController extends BaseController {

    /**
     * 首页
     * @return 首页地址
     */
    @RequiresPermissions("tool:swagger:view")
    @GetMapping()
    public String index(){
        return redirect("/swagger-ui.html");
    }

}

