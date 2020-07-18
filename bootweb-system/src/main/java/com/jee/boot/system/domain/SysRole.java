package com.jee.boot.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jee.boot.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 角色表 sys_role
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class SysRole extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 角色ID */
	private Long roleId;
	/** 角色名称 */
	@NotBlank(message = "角色名称不能为空")
	@Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
	private String roleName;
	/** 角色权限字符串 */
	@NotBlank(message = "权限字符不能为空")
	@Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
	private String roleKey;
	/** 显示顺序 */
	@NotBlank(message = "显示顺序不能为空")
	private Integer roleSort;
	/** 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限） */
	private String dataScope;
	/** 角色状态（0正常 1停用） */
	private String status;
	/** 删除标志（0代表存在 2代表删除） */
	private String delFlag;


	public void setRoleId(Long roleId){
		this.roleId = roleId;
	}

	public Long getRoleId(){
		return roleId;
	}
	public void setRoleName(String roleName){
		this.roleName = roleName;
	}

	public String getRoleName(){
		return roleName;
	}
	public void setRoleKey(String roleKey){
		this.roleKey = roleKey;
	}

	public String getRoleKey(){
		return roleKey;
	}
	public void setRoleSort(Integer roleSort){
		this.roleSort = roleSort;
	}

	public Integer getRoleSort(){
		return roleSort;
	}
	public void setDataScope(String dataScope){
		this.dataScope = dataScope;
	}

	public String getDataScope(){
		return dataScope;
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
            .append("roleId", getRoleId())
            .append("roleName", getRoleName())
            .append("roleKey", getRoleKey())
            .append("roleSort", getRoleSort())
            .append("dataScope", getDataScope())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
