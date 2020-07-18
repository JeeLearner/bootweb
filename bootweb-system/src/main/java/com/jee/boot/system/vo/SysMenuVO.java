package com.jee.boot.system.vo;

import com.jee.boot.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 菜单权限 VO
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysMenuVO extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 菜单ID */
    private Long menuId;
    /** 菜单名称 */
    private String menuName;
    /** 父菜单ID */
    private Long parentId;
    /** 显示顺序 */
    private Integer orderNum;
    /** 请求地址 */
    private String url;
    /** 打开方式（menuItem页签 menuBlank新窗口） */
    private String target;
    /** 菜单类型（M目录 C菜单 F按钮） */
    private String menuType;
    /** 菜单状态（0显示 1隐藏） */
    private String visible;
    /** 权限标识 */
    private String perms;
    /** 菜单图标 */
    private String icon;


    public void setMenuId(Long menuId){
        this.menuId = menuId;
    }

    public Long getMenuId(){
        return menuId;
    }
    public void setMenuName(String menuName){
        this.menuName = menuName;
    }

    public String getMenuName(){
        return menuName;
    }
    public void setParentId(Long parentId){
        this.parentId = parentId;
    }

    public Long getParentId(){
        return parentId;
    }
    public void setOrderNum(Integer orderNum){
        this.orderNum = orderNum;
    }

    public Integer getOrderNum(){
        return orderNum;
    }
    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }
    public void setTarget(String target){
        this.target = target;
    }

    public String getTarget(){
        return target;
    }
    public void setMenuType(String menuType){
        this.menuType = menuType;
    }

    public String getMenuType(){
        return menuType;
    }
    public void setVisible(String visible){
        this.visible = visible;
    }

    public String getVisible(){
        return visible;
    }
    public void setPerms(String perms){
        this.perms = perms;
    }

    public String getPerms(){
        return perms;
    }
    public void setIcon(String icon){
        this.icon = icon;
    }

    public String getIcon(){
        return icon;
    }
}
