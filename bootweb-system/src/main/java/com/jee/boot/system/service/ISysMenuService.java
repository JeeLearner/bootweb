package com.jee.boot.system.service;

import com.jee.boot.common.core.domain.Ztree;
import com.jee.boot.system.dto.SysMenuDTO;
import com.jee.boot.system.dto.SysRoleDTO;
import com.jee.boot.system.dto.SysUserDTO;

import java.util.List;
import java.util.Set;

/**
 * 菜单权限 服务层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface ISysMenuService {
	/**
	 * 查询系统菜单列表
	 *
	 * @param menu 菜单信息
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	List<SysMenuDTO> listMenu(SysMenuDTO menu, Long userId);

	/**
	 * 根据用户查询菜单
	 * @param user 用户
	 * @return 菜单列表
	 */
	List<SysMenuDTO> listMenusByUser(SysUserDTO user);

	/**
	 * 查询菜单集合
	 *
	 * @param userId 用户ID
	 * @return 所有菜单信息
	 */
	List<SysMenuDTO> listMenuAll(Long userId);

	/**
     * 查询菜单权限信息
     * 
     * @param menuId 菜单权限ID
     * @return 菜单权限信息
     */
	SysMenuDTO getMenuById(Long menuId);

	/**
	 * 根据用户ID查询权限
	 *
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	Set<String> listPermsByUserId(Long userId);

	/**
	 * 查询菜单管理树（排除自己）
	 *
	 * @param menu 菜单信息
	 * @return 所有菜单信息
	 */
	List<Ztree> listMenuTreeExcludeChild(SysMenuDTO menu);

	/**
	 * 查询所有菜单信息
	 *
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	List<Ztree> listMenuTree(Long userId);

	/**
	 * 根据角色ID查询菜单
	 *
	 * @param role 角色对象
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	List<Ztree> listRoleMenuTreeData(SysRoleDTO role, Long userId);

	/**
	 * 根据parentID查询菜单数量
	 *
	 * @param parentId 菜单父ID
	 * @return 结果
	 */
	int countMenuByParentId(Long parentId);

	/**
	 * 查询菜单使用数量
	 *
	 * @param menuId 菜单ID
	 * @return 结果
	 */
	int countRoleMenuByMenuId(Long menuId);
	
	/**
     * 新增菜单权限
     * 
     * @param menu 菜单权限信息
     * @return 结果
     */
	int insertMenu(SysMenuDTO menu);
	
	/**
     * 修改菜单权限
     * 
     * @param menu 菜单权限信息
     * @return 结果
     */
	int updateMenu(SysMenuDTO menu);

	/**
	 * 删除菜单管理信息
	 *
	 * @param menuId 菜单ID
	 * @return 结果
	 */
	int deleteMenuById(Long menuId);

	/**
     * 删除菜单权限信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteMenuByIds(Long[] ids);

	/**
	 * 校验菜单名称是否唯一
	 *
	 * @param menu 菜单信息
	 * @return 结果
	 */
	boolean checkMenuNameUnique(SysMenuDTO menu);
}
