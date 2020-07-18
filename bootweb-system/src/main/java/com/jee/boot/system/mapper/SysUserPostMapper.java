package com.jee.boot.system.mapper;

import com.jee.boot.system.domain.SysUserPost;
import java.util.List;	

/**
 * 用户与岗位关联 Mapper接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysUserPostMapper {
	/**
	 * 通过岗位ID查询岗位使用数量
	 *
	 * @param postId 岗位ID
	 * @return 结果
	 */
	int countUserPostById(Long postId);

	/**
	 * 批量新增用户岗位信息
	 *
	 * @param userPostList 用户角色列表
	 * @return 结果
	 */
	int insertBatchUserPost(List<SysUserPost> userPostList);

	/**
	 * 通过用户ID删除用户和岗位关联
	 *
	 * @param userId 用户ID
	 * @return 结果
	 */
	int deleteUserPostByUserId(Long userId);
}