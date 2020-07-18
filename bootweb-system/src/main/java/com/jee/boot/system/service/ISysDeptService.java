package com.jee.boot.system.service;

import com.jee.boot.common.core.domain.Ztree;
import com.jee.boot.system.dto.SysDeptDTO;
import com.jee.boot.system.dto.SysRoleDTO;

import java.util.List;

/**
 * 部门 服务层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface ISysDeptService {

	/**
	 * 查询部门列表
	 *
	 * @param dept 部门信息
	 * @return 部门集合
	 */
	List<SysDeptDTO> listDept(SysDeptDTO dept);

	/**
	 * 根据ID查询所有子部门
	 *
	 * @param deptId 部门ID
	 * @return 部门列表
	 */
	List<SysDeptDTO> listChildrenDeptById(Long deptId);

	/**
	 * 根据角色ID查询部门
	 *
	 * @param roleId 角色ID
	 * @return 部门列表
	 */
	List<String> listRoleDeptTree(Long roleId);

	/**
	 * 查询部门管理树
	 *
	 * @param dept 部门信息
	 * @return 所有部门信息
	 */
	List<Ztree> listDeptTree(SysDeptDTO dept);

	/**
	 * 查询部门管理树（排除下级）
	 *
	 * @param dept 部门信息
	 * @return 所有部门信息
	 */
	List<Ztree> listDeptTreeExcludeChild(SysDeptDTO dept);

	/**
	 * 根据角色ID查询部门（数据权限）
	 *
	 * @param role 角色对象
	 * @return 菜单列表
	 */
	List<Ztree> listDeptTreeDataByRole(SysRoleDTO role);

	/**
	 * 查询部门信息
	 *
	 * @param deptId 部门ID
	 * @return 部门信息
	 */
	SysDeptDTO getDeptById(Long deptId);

	/**
	 * 根据部门parentId查询子部门数量
	 *
	 * @param parentId 部门parentId
	 * @return 结果
	 */
	int countDeptByParentId(Long parentId);

	/**
	 * 根据ID查询所有子部门（正常状态）
	 *
	 * @param deptId 部门ID
	 * @return 子部门数
	 */
	int countNormalChildrenDeptById(Long deptId);



	/**
	 * 新增部门
	 *
	 * @param dept 部门信息
	 * @return 结果
	 */
	int insertDept(SysDeptDTO dept);

	/**
	 * 修改部门
	 *
	 * @param dept 部门信息
	 * @return 结果
	 */
	int updateDept(SysDeptDTO dept);

	/**
	 * 修改子元素关系
	 *
	 * @param deptId 被修改的部门ID
	 * @param newAncestors 新的父ID集合
	 * @param oldAncestors 旧的父ID集合
	 */
	void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors);

	/**
	 * 修改所在部门的父级部门状态
	 *
	 * @param dept 部门
	 */
	void updateParentDeptStatus(SysDeptDTO dept);

	/**
	 * 删除部门
	 * 		逻辑删除
	 *
	 * @param deptId 部门ID
	 * @return 结果
	 */
	int deleteDeptById(Long deptId);

	/**
	 * 查询部门是否存在用户
	 *
	 * @param deptId 部门ID
	 * @return 结果
	 */
	boolean checkDeptExistUser(Long deptId);

	/**
	 * 校验部门名称是否唯一
	 *
	 * @param dept 部门信息
	 * @return 结果
	 */
	boolean checkDeptNameUnique(SysDeptDTO dept);
}