package com.jee.boot.system.mapper;

import com.jee.boot.system.domain.SysRoleMenu;
import java.util.List;	

/**
 * 角色和菜单关联 Mapper接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysRoleMenuMapper {

	/**
	 * 查询菜单使用数量
	 *
	 * @param menuId 菜单ID
	 * @return 结果
	 */
	int countRoleMenuByMenuId(Long menuId);

	/**
	 * 批量新增角色菜单信息
	 *
	 * @param roleMenuList 角色菜单列表
	 * @return 结果
	 */
	int insertBatchRoleMenu(List<SysRoleMenu> roleMenuList);

	/**
	 * 通过角色ID删除角色和菜单关联
	 *
	 * @param roleId 角色ID
	 * @return 结果
	 */
	int deleteRoleMenuByRoleId(Long roleId);
}