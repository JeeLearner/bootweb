package com.jee.boot.alone.license.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 输入参数
 *
 * @author jeeLearner
 * @version V1.0
 */
public class LicenseServerParam {

    /**
     * 证书生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issuedTime = new Date();

    /**
     * 证书失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    /**
     * 硬件信息
     */
    private LicenseHardwareCheck licenseHardwareCheck;



    public Date getIssuedTime() {
        return issuedTime;
    }

    public void setIssuedTime(Date issuedTime) {
        this.issuedTime = issuedTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public LicenseHardwareCheck getLicenseHardwareCheck() {
        return licenseHardwareCheck;
    }

    public void setLicenseHardwareCheck(LicenseHardwareCheck licenseHardwareCheck) {
        this.licenseHardwareCheck = licenseHardwareCheck;
    }
}

