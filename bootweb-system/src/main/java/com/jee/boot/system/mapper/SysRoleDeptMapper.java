package com.jee.boot.system.mapper;

import com.jee.boot.system.domain.SysRoleDept;
import java.util.List;	

/**
 * 角色和部门关联 Mapper接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysRoleDeptMapper {
	/**
	 * 批量新增角色部门信息
	 *
	 * @param roleDeptList 角色部门列表
	 * @return 结果
	 */
	int insertBatchRoleDept(List<SysRoleDept> roleDeptList);

	/**
	 * 通过角色ID删除角色和部门关联
	 *
	 * @param roleId 角色ID
	 * @return 结果
	 */
	int deleteRoleDeptByRoleId(Long roleId);
}