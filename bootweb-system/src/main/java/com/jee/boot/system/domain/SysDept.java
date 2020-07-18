package com.jee.boot.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jee.boot.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 部门表 sys_dept
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class SysDept extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 部门id */
	private Long deptId;
	/** 父部门id */
	private Long parentId;
	/** 祖级列表 */
	private String ancestors;
	/** 部门名称 */
	@NotBlank(message = "部门名称不能为空")
	@Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
	private String deptName;
	/** 显示顺序 */
	@NotNull(message = "显示顺序不能为空")
	private Integer orderNum;
	/** 负责人 */
	private String leader;
	/** 联系电话 */
	private String phone;
	/** 邮箱 */
	private String email;
	/** 部门状态（0正常 1停用） */
	private String status;
	/** 删除标志（0代表存在 2代表删除） */
	private String delFlag;


	public void setDeptId(Long deptId){
		this.deptId = deptId;
	}

	public Long getDeptId(){
		return deptId;
	}
	public void setParentId(Long parentId){
		this.parentId = parentId;
	}

	public Long getParentId(){
		return parentId;
	}
	public void setAncestors(String ancestors){
		this.ancestors = ancestors;
	}

	public String getAncestors(){
		return ancestors;
	}
	public void setDeptName(String deptName){
		this.deptName = deptName;
	}

	public String getDeptName(){
		return deptName;
	}
	public void setOrderNum(Integer orderNum){
		this.orderNum = orderNum;
	}

	public Integer getOrderNum(){
		return orderNum;
	}
	public void setLeader(String leader){
		this.leader = leader;
	}

	public String getLeader(){
		return leader;
	}
	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}
	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
	public void setDelFlag(String delFlag){
		this.delFlag = delFlag;
	}

	public String getDelFlag(){
		return delFlag;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deptId", getDeptId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("deptName", getDeptName())
            .append("orderNum", getOrderNum())
            .append("leader", getLeader())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
