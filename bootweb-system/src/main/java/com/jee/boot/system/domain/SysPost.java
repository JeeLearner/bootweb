package com.jee.boot.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jee.boot.common.core.domain.BaseEntity;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 岗位表 sys_post
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class SysPost extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 岗位ID */
	private Long postId;
	/** 岗位编码 */
	@NotBlank(message = "岗位编码不能为空")
	@Size(min = 0, max = 64, message = "岗位编码长度不能超过64个字符")
	private String postCode;
	/** 岗位名称 */
	@NotBlank(message = "岗位名称不能为空")
	@Size(min = 0, max = 50, message = "岗位名称长度不能超过50个字符")
	private String postName;
	/** 显示顺序 */
	@NotNull(message = "显示顺序不能为空")
	private Integer postSort;
	/** 状态（0正常 1停用） */
	private String status;


	public void setPostId(Long postId){
		this.postId = postId;
	}

	public Long getPostId(){
		return postId;
	}
	public void setPostCode(String postCode){
		this.postCode = postCode;
	}

	public String getPostCode(){
		return postCode;
	}
	public void setPostName(String postName){
		this.postName = postName;
	}

	public String getPostName(){
		return postName;
	}
	public void setPostSort(Integer postSort){
		this.postSort = postSort;
	}

	public Integer getPostSort(){
		return postSort;
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
            .append("postId", getPostId())
            .append("postCode", getPostCode())
            .append("postName", getPostName())
            .append("postSort", getPostSort())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
