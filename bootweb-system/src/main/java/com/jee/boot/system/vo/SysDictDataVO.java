package com.jee.boot.system.vo;

import com.jee.boot.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 字典数据 VO
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysDictDataVO extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 字典编码 */
    private Long dictCode;
    /** 字典排序 */
    private Integer dictSort;
    /** 字典标签 */
    private String dictLabel;
    /** 字典键值 */
    private String dictValue;
    /** 字典类型 */
    private String dictType;
    /** 样式属性（其他样式扩展） */
    private String cssClass;
    /** 表格回显样式 */
    private String listClass;
    /** 是否默认（Y是 N否） */
    private String isDefault;
    /** 状态（0正常 1停用） */
    private String status;


    public void setDictCode(Long dictCode){
        this.dictCode = dictCode;
    }

    public Long getDictCode(){
        return dictCode;
    }
    public void setDictSort(Integer dictSort){
        this.dictSort = dictSort;
    }

    public Integer getDictSort(){
        return dictSort;
    }
    public void setDictLabel(String dictLabel){
        this.dictLabel = dictLabel;
    }

    public String getDictLabel(){
        return dictLabel;
    }
    public void setDictValue(String dictValue){
        this.dictValue = dictValue;
    }

    public String getDictValue(){
        return dictValue;
    }
    public void setDictType(String dictType){
        this.dictType = dictType;
    }

    public String getDictType(){
        return dictType;
    }
    public void setCssClass(String cssClass){
        this.cssClass = cssClass;
    }

    public String getCssClass(){
        return cssClass;
    }
    public void setListClass(String listClass){
        this.listClass = listClass;
    }

    public String getListClass(){
        return listClass;
    }
    public void setIsDefault(String isDefault){
        this.isDefault = isDefault;
    }

    public String getIsDefault(){
        return isDefault;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
