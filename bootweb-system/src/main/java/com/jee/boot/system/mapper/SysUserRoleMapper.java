package com.jee.boot.system.mapper;

import com.jee.boot.system.domain.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户和角色关联 Mapper接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysUserRoleMapper {

	/**
	 * 通过用户ID查询用户和角色关联
	 *
	 * @param userId 用户ID
	 * @return 用户和角色关联列表
	 */
	List<SysUserRole> selectUserRoleByUserId(Long userId);

	/**
	 * 通过角色ID查询角色使用数量
	 *
	 * @param roleId 角色ID
	 * @return 结果
	 */
	int countUserRoleByRoleId(Long roleId);

	/**
	 * 批量新增用户角色信息
	 *
	 * @param userRoleList 用户角色列表
	 * @return 结果
	 */
	int insertBatchUserRole(List<SysUserRole> userRoleList);

	/**
	 * 通过用户ID删除用户和角色关联
	 *
	 * @param userId 用户ID
	 * @return 结果
	 */
	int deleteUserRoleByUserId(Long userId);
	/**
	 * 删除用户和角色关联信息
	 *
	 * @param userRole 用户和角色关联信息
	 * @return 结果
	 */
	int deleteUserRoleInfo(SysUserRole userRole);

	/**
	 * 批量取消授权用户角色
	 *
	 * @param roleId 角色ID
	 * @param userIds 需要删除的用户数据ID
	 * @return 结果
	 */
	int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}