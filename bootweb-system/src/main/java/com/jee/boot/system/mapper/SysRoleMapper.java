package com.jee.boot.system.mapper;

import com.jee.boot.system.dto.SysRoleDTO;
import com.jee.boot.system.domain.SysRole;
import java.util.List;	

/**
 * 角色 数据层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysRoleMapper {

	/**
	 * 查询角色列表
	 *
	 * @param role 角色信息
	 * @return 角色集合
	 */
	List<SysRoleDTO> selectRoleList(SysRole role);

	/**
	 * 根据用户ID查询角色
	 *
	 * @param userId 用户ID
	 * @return 角色列表
	 */
	List<SysRoleDTO> selectRolesByUserId(Long userId);

	/**
     * 查询角色信息
     * 
     * @param roleId 角色ID
     * @return 角色信息
     */
	SysRoleDTO selectRoleById(Long roleId);

	/**
     * 新增角色
     * 
     * @param role 角色信息
     * @return 结果
     */
	int insertRole(SysRole role);
	
	/**
     * 修改角色
     * 
     * @param role 角色信息
     * @return 结果
     */
	int updateRole(SysRole role);
	
	/**
     * 删除角色
     * 
     * @param roleId 角色ID
     * @return 结果
     */
	int deleteRoleById(Long roleId);
	
	/**
     * 批量删除角色
     * 
     * @param roleIds 需要删除的数据ID
     * @return 结果
     */
	int deleteRoleByIds(Long[] roleIds);

	/**
	 * 校验角色名称是否唯一
	 *
	 * @param roleName 角色名称
	 * @return 角色信息
	 */
	SysRoleDTO checkRoleNameUnique(String roleName);

	/**
	 * 校验角色权限是否唯一
	 *
	 * @param roleKey 角色权限
	 * @return 角色信息
	 */
	SysRoleDTO checkRoleKeyUnique(String roleKey);
}