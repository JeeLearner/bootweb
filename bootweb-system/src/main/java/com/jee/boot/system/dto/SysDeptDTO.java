package com.jee.boot.system.dto;

import com.jee.boot.system.domain.SysDept;

/**
 * 部门 DTO
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysDeptDTO extends SysDept{

    /** 父部门名称 */
    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

}
