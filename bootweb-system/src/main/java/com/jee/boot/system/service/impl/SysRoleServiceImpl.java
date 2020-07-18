package com.jee.boot.system.service.impl;

import java.time.LocalDateTime;
import java.util.*;

import com.jee.boot.common.annotation.DataScope;
import com.jee.boot.common.exception.base.BusinessException;
import com.jee.boot.common.poi.annotation.Excel;
import com.jee.boot.common.utils.spring.SpringUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.system.domain.SysRoleDept;
import com.jee.boot.system.domain.SysRoleMenu;
import com.jee.boot.system.domain.SysUserRole;
import com.jee.boot.system.mapper.SysRoleDeptMapper;
import com.jee.boot.system.mapper.SysRoleMenuMapper;
import com.jee.boot.system.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jee.boot.system.mapper.SysRoleMapper;
import com.jee.boot.system.dto.SysRoleDTO;
import com.jee.boot.system.service.ISysRoleService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色 服务层实现
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {
	@Autowired
	private SysRoleMapper roleMapper;
	@Autowired
	private SysUserRoleMapper userRoleMapper;
	@Autowired
	private SysRoleMenuMapper roleMenuMapper;
	@Autowired
	private SysRoleDeptMapper roleDeptMapper;

	/**
     * 查询角色列表
     * 
     * @param role 角色信息
     * @return 角色集合
     */
	@Override
	@DataScope(deptAlias = "u")
	public List<SysRoleDTO> listRole(SysRoleDTO role) {
	    return roleMapper.selectRoleList(role);
	}

	@Override
	public List<SysRoleDTO> listRoleAll() {
		//这里是有问题的，AOP无法切入此方法
		//return selectRoleList(new SysRoleDTO());
		return SpringUtils.getAopProxy(this).listRole(new SysRoleDTO());
	}

	/**
	 * 根据用户ID查询角色列表
	 *
	 * @param userId 用户ID
	 * @return 角色列表
	 */
	@Override
	public List<SysRoleDTO> listRolesByUserId(Long userId) {
		List<SysRoleDTO> userRoles = roleMapper.selectRolesByUserId(userId);
		List<SysRoleDTO> roles = listRoleAll();
		for (SysRoleDTO role : roles) {
			for (SysRoleDTO userRole : userRoles) {
				if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
					role.setFlag(true);
					break;
				}
			}
		}
		return roles;
	}

	/**
	 * 根据用户ID查询权限
	 *
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	@Override
	public Set<String> listRoleKeys(Long userId) {
		List<SysRoleDTO> perms = roleMapper.selectRolesByUserId(userId);
		Set<String> permsSet = new HashSet<>();
		for (SysRoleDTO perm : perms){
			if (JeeStringUtils.isNotNull(perm)){
				permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
			}
		}
		return permsSet;
	}

	/**
	 * 查询角色信息
	 *
	 * @param roleId 角色ID
	 * @return 角色信息
	 */
	@Override
	public SysRoleDTO getRoleById(Long roleId) {
		return roleMapper.selectRoleById(roleId);
	}

	/**
	 * 通过角色ID查询角色使用数量
	 *
	 * @param roleId 角色ID
	 * @return 结果
	 */
	@Override
	public int countUserRoleByRoleId(Long roleId){
		return userRoleMapper.countUserRoleByRoleId(roleId);
	}

	/**
     * 新增角色
     * 
     * @param role 角色信息
     * @return 结果
     */
	@Override
	@Transactional
	public int insertRole(SysRoleDTO role) {
		role.setCreateTime(LocalDateTime.now());
		roleMapper.insertRole(role);
	    return insertRoleMenu(role);

	}

	/**
	 * 批量选择授权用户角色
	 *
	 * @param roleId 角色ID
	 * @param userIds 需要删除的用户数据ID
	 * @return 结果
	 */
	@Override
	public int insertRoleUsers(Long roleId, Long[] userIds) {
		// 新增用户与角色管理
		List<SysUserRole> list = new ArrayList<SysUserRole>();
		for (Long userId : userIds){
			SysUserRole ur = new SysUserRole();
			ur.setUserId(userId);
			ur.setRoleId(roleId);
			list.add(ur);
		}
		return userRoleMapper.insertBatchUserRole(list);
	}

	/**
     * 修改角色
     * 
     * @param role 角色信息
     * @return 结果
     */
	@Override
	@Transactional
	public int updateRole(SysRoleDTO role) {
		role.setUpdateTime(LocalDateTime.now());
		// 修改角色信息
		int row = roleMapper.updateRole(role);
		// 删除角色与菜单关联
		roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
		insertRoleMenu(role);
		return row;
	}

	/**
     * 删除角色对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteRoleByIds(Long[] ids) {
		for (Long roleId : ids){
			//判断是否允许修改此角色
			checkRoleAllowed(new SysRoleDTO(roleId));
			SysRoleDTO role = getRoleById(roleId);
			if (countUserRoleByRoleId(roleId) > 0){
				throw new BusinessException(String.format("%1$s已分配,不能删除", role.getRoleName()));
			}
		}
		return roleMapper.deleteRoleByIds(ids);
	}


	/**
	 * 角色状态修改
	 * @param role
	 * @return
	 */
	@Override
	public int updateStatus(SysRoleDTO role) {
		role.setUpdateTime(role.getUpdateTime());
		return roleMapper.updateRole(role);
	}

	/**
	 * 修改数据权限信息
	 *
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateDataScope(SysRoleDTO role) {
		// 修改角色信息
		int row = roleMapper.updateRole(role);
		// 删除角色与部门关联
		roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
		// 新增角色和部门信息（数据权限）
		insertRoleDept(role);
		return row;
	}

	/**
	 * 取消授权用户角色
	 *
	 * @param userRole 用户和角色关联信息
	 * @return 结果
	 */
	@Override
	public int deleteRoleUser(SysUserRole userRole) {
		return userRoleMapper.deleteUserRoleInfo(userRole);
	}

	/**
	 * 批量取消授权用户角色
	 *
	 * @param roleId 角色ID
	 * @param userIds 需要删除的用户数据ID
	 * @return 结果
	 */
	@Override
	public int deleteRoleUsers(Long roleId, Long[] userIds) {
		return userRoleMapper.deleteUserRoleInfos(roleId, userIds);
	}

	/**
	 * 校验角色名称是否唯一
	 *
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	public boolean checkRoleNameUnique(SysRoleDTO role) {
		Long roleId = JeeStringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
		SysRoleDTO info = roleMapper.checkRoleNameUnique(role.getRoleName());
		if (JeeStringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()){
			return false;
		}
		return true;
	}

	@Override
	public boolean checkRoleKeyUnique(SysRoleDTO role) {
		Long roleId = JeeStringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
		SysRoleDTO info = roleMapper.checkRoleKeyUnique(role.getRoleName());
		if (JeeStringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()){
			return false;
		}
		return true;
	}

	/**
	 * 校验角色是否允许操作
	 *
	 * @param role 角色信息
	 */
	@Override
	public void checkRoleAllowed(SysRoleDTO role) {
		if(JeeStringUtils.isNotNull(role.getRoleId()) && role.isAdmin()){
			throw new BusinessException("不允许操作超级管理员角色");
		}
	}

	/**
	 * 新增角色部门信息(数据权限)
	 *
	 * @param role 角色对象
	 */
	private int insertRoleDept(SysRoleDTO role) {
		int rows = 0;
		// 新增角色与部门（数据权限）管理
		List<SysRoleDept> list = new ArrayList<SysRoleDept>();
		for (Long deptId : role.getDeptIds()){
			SysRoleDept rd = new SysRoleDept();
			rd.setRoleId(role.getRoleId());
			rd.setDeptId(deptId);
			list.add(rd);
		}
		if (list.size() > 0){
			rows = roleDeptMapper.insertBatchRoleDept(list);
		}
		return rows;

	}

	/**
	 * 新增角色菜单信息
	 *
	 * @param role 角色对象
	 */
	private int insertRoleMenu(SysRoleDTO role) {
		int rows = 0;
		// 新增用户与角色管理
		List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
		for (Long menuId : role.getMenuIds()){
			SysRoleMenu rm = new SysRoleMenu();
			rm.setRoleId(role.getRoleId());
			rm.setMenuId(menuId);
			list.add(rm);
		}
		if (list.size() > 0){
			rows = roleMenuMapper.insertBatchRoleMenu(list);
		}
		return rows;
	}
}
