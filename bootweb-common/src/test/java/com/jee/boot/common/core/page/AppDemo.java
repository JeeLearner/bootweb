package com.jee.boot.common.core.page;

import com.jee.boot.common.poi.annotation.Excel;
import com.jee.boot.common.poi.annotation.Excel.Type;

public class AppDemo {
    /**
     * 用户序号
     */
    @Excel(name = "应用名称", type = Type.ALL)
    private String appName;
    @Excel(name = "ip", type = Type.ALL)
    private String ip;

    public AppDemo() {
    }

    public AppDemo(String appName, String ip) {
        this.appName = appName;
        this.ip = ip;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}