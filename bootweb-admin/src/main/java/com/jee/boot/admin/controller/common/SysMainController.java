package com.jee.boot.admin.controller.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jee.boot.common.core.result.R;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页 控制器
 *
 * @author jeeLearner
 * @version V1.0
 */
@RestController
@RequestMapping(value = "/main")
public class SysMainController {

    /**
     * 主页一周访客
     * @return
     */
    @PostMapping("/weekUser")
    public R weekUser(){
        List<WeekUser> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 6; i >=0; i--) {
            WeekUser user = new WeekUser();
            user.setLoginDate(now.minusDays(i));
            user.setCount(RandomUtils.nextInt(100));
            user.setOuter(RandomUtils.nextInt(500));
            list.add(user);
        }
        return R.ok().data("list", list);
    }
}


class WeekUser{
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime loginDate;
    private int count;
    private int outer;

    public LocalDateTime getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(LocalDateTime loginDate) {
        this.loginDate = loginDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOuter() {
        return outer;
    }

    public void setOuter(int outer) {
        this.outer = outer;
    }
}

