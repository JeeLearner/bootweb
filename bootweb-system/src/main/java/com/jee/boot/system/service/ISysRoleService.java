package com.jee.boot.system.service;

import com.jee.boot.system.domain.SysUserRole;
import com.jee.boot.system.dto.SysRoleDTO;
import java.util.List;
import java.util.Set;

/**
 * 角色 服务层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface ISysRoleService {

	/**
     * 查询角色列表
     * 
     * @param role 角色信息
     * @return 角色集合
     */
	List<SysRoleDTO> listRole(SysRoleDTO role);

	/**
	 * 查询所有角色
	 *
	 * @return 角色列表
	 */
	List<SysRoleDTO> listRoleAll();

	/**
	 * 根据用户ID查询角色列表
	 *
	 * @param userId 用户ID
	 * @return 角色列表
	 */
	List<SysRoleDTO> listRolesByUserId(Long userId);

	/**
	 * 查询角色信息
	 *
	 * @param roleId 角色ID
	 * @return 角色信息
	 */
	SysRoleDTO getRoleById(Long roleId);

	/**
	 * 根据用户ID查询角色
	 *
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	Set<String> listRoleKeys(Long userId);

	/**
	 * 通过角色ID查询角色使用数量
	 *
	 * @param roleId 角色ID
	 * @return 结果
	 */
	int countUserRoleByRoleId(Long roleId);

	/**
     * 新增角色
     * 
     * @param role 角色信息
     * @return 结果
     */
	int insertRole(SysRoleDTO role);

	/**
	 * 批量选择授权用户角色
	 *
	 * @param roleId 角色ID
	 * @param userIds 需要删除的用户数据ID
	 * @return 结果
	 */
	int insertRoleUsers(Long roleId, Long[] userIds);
	
	/**
     * 修改角色
     * 
     * @param role 角色信息
     * @return 结果
     */
	int updateRole(SysRoleDTO role);

	/**
	 * 角色状态修改
	 * @param dto
	 * @return
	 */
	int updateStatus(SysRoleDTO dto);

	/**
	 * 修改数据权限信息
	 *
	 * @param role 角色信息
	 * @return 结果
	 */
	int updateDataScope(SysRoleDTO role);
		
	/**
     * 删除角色信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteRoleByIds(Long[] ids);

	/**
	 * 取消授权用户角色
	 *
	 * @param userRole 用户和角色关联信息
	 * @return 结果
	 */
	int deleteRoleUser(SysUserRole userRole);

	/**
	 * 批量取消授权用户角色
	 *
	 * @param roleId 角色ID
	 * @param userIds 需要删除的用户数据ID
	 * @return 结果
	 */
	int deleteRoleUsers(Long roleId, Long[] userIds);

	/**
	 * 校验角色名称是否唯一
	 *
	 * @param role 角色信息
	 * @return 结果
	 */
	boolean checkRoleNameUnique(SysRoleDTO role);

	/**
	 * 校验角色权限是否唯一
	 *
	 * @param role 角色信息
	 * @return 结果
	 */
	boolean checkRoleKeyUnique(SysRoleDTO role);

	/**
	 * 校验角色是否允许操作
	 *
	 * @param role 角色信息
	 * @return 结果
	 */
	void checkRoleAllowed(SysRoleDTO role);



}
