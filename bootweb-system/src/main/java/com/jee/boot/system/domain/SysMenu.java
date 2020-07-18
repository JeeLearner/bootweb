package com.jee.boot.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jee.boot.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 菜单权限表 sys_menu
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class SysMenu extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 菜单ID */
	private Long menuId;
	/** 菜单名称 */
	@NotBlank(message = "菜单名称不能为空")
	@Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
	private String menuName;
	/** 父菜单ID */
	private Long parentId;
	/** 显示顺序 */
	@NotNull(message = "显示顺序不能为空")
	private Integer orderNum;
	/** 请求地址 */
	@Size(min = 0, max = 200, message = "请求地址不能超过200个字符")
	private String url;
	/** 打开方式（menuItem页签 menuBlank新窗口） */
	private String target;
	/** 菜单类型（M目录 C菜单 F按钮） */
	@NotBlank(message = "菜单类型不能为空")
	private String menuType;
	/** 菜单状态（0显示 1隐藏） */
	private String visible;
	/** 权限标识 */
	@Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("menuId", getMenuId())
            .append("menuName", getMenuName())
            .append("parentId", getParentId())
            .append("orderNum", getOrderNum())
            .append("url", getUrl())
            .append("target", getTarget())
            .append("menuType", getMenuType())
            .append("visible", getVisible())
            .append("perms", getPerms())
            .append("icon", getIcon())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
