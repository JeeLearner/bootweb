package com.jee.boot.framework.config.properties;

import com.jee.boot.common.utils.text.JeeStringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Auth 属性
 * @author jeeLearner
 * @version V1.0
 */
@Component
@ConfigurationProperties(prefix = "sys")
public class SysProperties {
    // 验证码开关
    private boolean verifyCodeEnabled;
    // 验证码类型
    private String verifyCodeType;





    public boolean isVerifyCodeEnabled() {
        return verifyCodeEnabled;
    }

    public void setVerifyCodeEnabled(boolean verifyCodeEnabled) {
        this.verifyCodeEnabled = verifyCodeEnabled;
    }

    public String getVerifyCodeType() {
        return JeeStringUtils.isEmpty(verifyCodeType) ? "math" : verifyCodeType;
    }

    public void setVerifyCodeType(String verifyCodeType) {
        this.verifyCodeType = verifyCodeType;
    }
}

