package com.jee.boot.system.dto;

import com.jee.boot.system.domain.SysDept;
import com.jee.boot.system.domain.SysUser;

import java.util.List;

/**
 * 用户 DTO
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysUserDTO extends SysUser{

    /** 部门父ID */
    private Long parentId;

    /** 角色ID */
    private Long roleId;

    /** 部门对象 */
    private SysDeptDTO dept;

    private List<SysRoleDTO> roles;

    /** 角色组 */
    private Long[] roleIds;

    /** 岗位组 */
    private Long[] postIds;

    public SysUserDTO() {
    }
    public SysUserDTO(Long userId) {
        setUserId(userId);
    }

    /**
     * 判断user是否是admin
     * @return
     */
    public boolean isAdmin(){
        return isAdmin(getUserId());
    }
    public static boolean isAdmin(Long userId){
        return userId != null && userId == 1L;
    }



    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public SysDeptDTO getDept() {
        if (dept == null){
            dept = new SysDeptDTO();
        }
        return dept;
    }

    public void setDept(SysDeptDTO dept) {
        this.dept = dept;
    }

    public List<SysRoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRoleDTO> roles) {
        this.roles = roles;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds() {
        return postIds;
    }

    public void setPostIds(Long[] postIds) {
        this.postIds = postIds;
    }
}
