package com.jee.boot.system.dto;

import com.jee.boot.system.domain.SysPost;

/**
 * 岗位 DTO
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysPostDTO extends SysPost{

    /** 用户是否存在此岗位标识 默认不存在 */
    private boolean flag = false;


    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
