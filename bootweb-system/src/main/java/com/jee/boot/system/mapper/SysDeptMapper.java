package com.jee.boot.system.mapper;

import com.jee.boot.system.dto.SysDeptDTO;
import com.jee.boot.system.domain.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门 数据层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysDeptMapper {

	/**
	 * 查询部门列表
	 *
	 * @param dept 部门信息
	 * @return 部门集合
	 */
	List<SysDeptDTO> selectDeptList(SysDept dept);

	/**
	 * 根据ID查询所有子部门
	 *
	 * @param deptId 部门ID
	 * @return 部门列表
	 */
	List<SysDeptDTO> selectChildrenDeptById(Long deptId);

	/**
	 * 根据角色ID查询部门
	 *
	 * @param roleId 角色ID
	 * @return 部门列表
	 */
	List<String> selectRoleDeptTree(Long roleId);

	/**
     * 查询部门信息
     * 
     * @param deptId 部门ID
     * @return 部门信息
     */
	SysDeptDTO selectDeptById(Long deptId);

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
	int insertDept(SysDept dept);
	
	/**
     * 修改部门
     * 
     * @param dept 部门信息
     * @return 结果
     */
	int updateDept(SysDept dept);

	/**
	 * 修改子元素关系
	 *
	 * @param depts 子元素
	 * @return 结果
	 */
	int updateDeptChildren(@Param("depts") List<SysDept> depts);

	/**
	 * 修改所在部门的父级部门状态
	 *
	 * @param dept 部门
	 */
	void updateParentDeptStatus(SysDept dept);

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
	int checkDeptExistUser(Long deptId);

	/**
	 * 校验部门名称是否唯一
	 *
	 * @param deptName 部门名称
	 * @param parentId 父部门ID
	 * @return 结果
	 */
	SysDeptDTO checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);
}