package com.jee.boot.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色和菜单关联 DO对象 sys_role_menu
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class SysRoleMenu {

	/** 角色ID */
	private Long roleId;
	/** 菜单ID */
	private Long menuId;


	public void setRoleId(Long roleId){
		this.roleId = roleId;
	}

	public Long getRoleId(){
		return roleId;
	}
	public void setMenuId(Long menuId){
		this.menuId = menuId;
	}

	public Long getMenuId(){
		return menuId;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("menuId", getMenuId())
            .toString();
    }
}
