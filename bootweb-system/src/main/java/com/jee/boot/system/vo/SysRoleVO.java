package com.jee.boot.system.vo;

import com.jee.boot.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 角色 VO
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysRoleVO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 角色ID */
    private Long roleId;
    /** 角色名称 */
    private String roleName;
    /** 角色权限字符串 */
    private String roleKey;
    /** 显示顺序 */
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
	
}
