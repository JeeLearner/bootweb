package com.jee.boot.system.dto;

import com.jee.boot.system.domain.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限 DTO
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysMenuDTO extends SysMenu{

    /** 父菜单名称 */
    private String parentName;
    /** 子菜单 */
    private List<SysMenuDTO> children = new ArrayList<SysMenuDTO>();


    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<SysMenuDTO> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenuDTO> children) {
        this.children = children;
    }
}
