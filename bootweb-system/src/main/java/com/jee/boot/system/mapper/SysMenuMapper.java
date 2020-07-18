package com.jee.boot.system.mapper;

import com.jee.boot.system.dto.SysMenuDTO;
import com.jee.boot.system.domain.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单 数据层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysMenuMapper {

	/**
	 * 查询菜单模块列表
	 *
	 * @param menu 菜单信息
	 * @return 菜单列表
	 */
	List<SysMenuDTO> selectMenuList(SysMenu menu);

	/**
	 * 查询系统所有菜单（含按钮）
	 *
	 * @return 菜单列表
	 */
	List<SysMenuDTO> selectMenuAll();

	/**
	 * 查询系统正常显示菜单（不含按钮）
	 * 		系统菜单导航
	 *
	 * @return 菜单列表
	 */
	List<SysMenuDTO> selectMenuNormalAll();

	/**
     * 根据菜单ID查询菜单信息
     * 
     * @param menuId 菜单ID
     * @return 菜单信息
     */
	SysMenuDTO selectMenuById(Long menuId);

	/**
	 * 根据用户ID查询菜单（不含按钮）
	 *
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	List<SysMenuDTO> selectMenusByUserId(Long userId);

	/**
	 * 根据用户ID查询菜单（包括按钮）
	 * 		不关心角色、菜单的可用状态
	 *
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	List<SysMenuDTO> selectMenuAllByUserId(Long userId);

	/**
	 * 查询系统菜单列表
	 * 		根据用户ID和搜索内容查询
	 *
	 * @param menu 菜单信息
	 * @return 菜单列表
	 */
	List<SysMenuDTO> selectMenuListByUserId(SysMenu menu);

	/**
	 * 根据用户ID查询权限
	 * 		system:user:view......
	 *
	 * @param userId 用户ID
	 * @return 列表
	 */
	List<String> selectPermsByUserId(Long userId);

	/**
	 * 根据角色ID查询菜单
	 *
	 * @param roleId 角色ID
	 * @return 菜单列表
	 */
	List<String> selectMenuTree(Long roleId);

	/**
	 * 根据parentID查询菜单数量
	 *
	 * @param parentId 菜单父ID
	 * @return 结果
	 */
	int countMenuByParentId(Long parentId);
	
	/**
     * 新增菜单信息
     * 
     * @param menu 菜单信息
     * @return 结果
     */
	int insertMenu(SysMenu menu);
	
	/**
     * 修改菜单信息
     * 
     * @param menu 菜单信息
     * @return 结果
     */
	int updateMenu(SysMenu menu);
	
	/**
     * 删除菜单
     * 
     * @param menuId 菜单ID
     * @return 结果
     */
	int deleteMenuById(Long menuId);
	
	/**
     * 批量删除菜单
     * 
     * @param menuIds 需要删除的数据ID
     * @return 结果
     */
	int deleteMenuByIds(Long[] menuIds);

	/**
	 * 校验菜单名称是否唯一
	 *
	 * @param menuName 菜单名称
	 * @param parentId 父菜单ID
	 * @return 结果
	 */
	SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);

}