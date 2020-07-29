package com.jee.boot.api.controller.common;

import com.jee.boot.common.annotation.RepeatSubmit;
import com.jee.boot.common.core.result.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页 控制器
 *
 * @author jeeLearner
 * @version V1.0
 */
@RestController
public class SysIndexController {


    /**
     * 系统首页
     */
    @PostMapping("/index")
    @PreAuthorize("@ss.hasPermi('system:user:view')")
    @RepeatSubmit
    public R index(){
        return R.ok().data("version", "v1.0");
    }

    @PostMapping("/hello")
    @RepeatSubmit
    public R helo(@RequestBody Demo demo){
        return R.ok().data("demo", demo.getName());
    }

    @PostMapping("/test")
    @RepeatSubmit
    public R testRepeat(@RequestBody Demo demo){
        return R.ok().data("demo", demo.getName());
    }

}

class Demo {
    private String name;
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

