package com.jee.boot.system.dto;

import com.jee.boot.common.constant.UserConstants;
import com.jee.boot.system.domain.SysDictData;

/**
 * 字典数据 DTO
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysDictDataDTO extends SysDictData{

    public boolean getDefault(){
        return UserConstants.YES.equals(getIsDefault()) ? true : false;
    }
}
