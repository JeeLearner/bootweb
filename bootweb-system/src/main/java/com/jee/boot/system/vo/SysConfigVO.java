package com.jee.boot.system.vo;

import com.jee.boot.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 参数配置 VO
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysConfigVO extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 参数主键 */
    private Integer configId;
    /** 参数名称 */
    private String configName;
    /** 参数键名 */
    private String configKey;
    /** 参数键值 */
    private String configValue;
    /** 系统内置（Y是 N否） */
    private String configType;



    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }
}
