package com.jee.boot.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jee.boot.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 字典类型表 sys_dict_type
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class SysDictType extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 字典主键 */
	private Long dictId;
	/** 字典名称 */
	@NotBlank(message = "字典名称不能为空")
	@Size(min = 0, max = 100, message = "字典类型名称长度不能超过100个字符")
	private String dictName;
	/** 字典类型 */
	@NotBlank(message = "字典类型不能为空")
	@Size(min = 0, max = 100, message = "字典类型类型长度不能超过100个字符")
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dictId", getDictId())
            .append("dictName", getDictName())
            .append("dictType", getDictType())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
