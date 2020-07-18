package com.jee.boot.system.dto;

import com.jee.boot.system.domain.SysLogOper;

/**
 * 操作日志记录 DTO对象
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysLogOperDTO extends SysLogOper{

    /** 业务类型数组 */
    private Integer[] businessTypes;

    public Integer[] getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessTypes(Integer[] businessTypes) {
        this.businessTypes = businessTypes;
    }
}
