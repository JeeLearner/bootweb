package com.jee.boot.system.dto;

import com.jee.boot.system.domain.SysRole;

/**
 * 角色 DTO
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysRoleDTO extends SysRole{

    /** 用户是否存在此角色标识 默认不存在 */
    private boolean flag = false;

    /** 菜单组 */
    private Long[] menuIds;

    /** 部门组（数据权限） */
    private Long[] deptIds;

    /**
     * 判断role是否是admin
     * @return
     */
    public boolean isAdmin(){
        return isAdmin(getRoleId());
    }

    public static boolean isAdmin(Long roleId){
        return roleId != null && 1L == roleId;
    }

    public SysRoleDTO() {
    }
    public SysRoleDTO(Long roleId){
        setRoleId(roleId);
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Long[] getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(Long[] menuIds) {
        this.menuIds = menuIds;
    }

    public Long[] getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(Long[] deptIds) {
        this.deptIds = deptIds;
    }
}
