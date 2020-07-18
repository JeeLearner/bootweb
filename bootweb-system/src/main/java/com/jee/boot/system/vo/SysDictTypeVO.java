package com.jee.boot.system.vo;

import com.jee.boot.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 字典类型 VO
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysDictTypeVO extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 字典主键 */
    private Long dictId;
    /** 字典名称 */
    private String dictName;
    /** 字典类型 */
    private String dictType;
    /** 状态（0正常 1停用） */
    private String status;


    public void setDictId(Long dictId){
        this.dictId = dictId;
    }

    public Long getDictId(){
        return dictId;
    }
    public void setDictName(String dictName){
        this.dictName = dictName;
    }

    public String getDictName(){
        return dictName;
    }
    public void setDictType(String dictType){
        this.dictType = dictType;
    }

    public String getDictType(){
        return dictType;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
	
}
