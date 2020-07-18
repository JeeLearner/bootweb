package com.jee.boot.system.service;

import com.jee.boot.system.dto.SysPostDTO;
import java.util.List;

/**
 * 岗位 服务层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface ISysPostService {
	/**
	 * 查询岗位列表
	 *
	 * @param post 岗位信息
	 * @return 岗位集合
	 */
	List<SysPostDTO> listPost(SysPostDTO post);

	/**
	 * 查询所有岗位
	 *
	 * @return 岗位列表
	 */
	List<SysPostDTO> listPostAll();

	/**
	 * 根据用户ID查询岗位
	 *
	 * @param userId 用户ID
	 * @return 岗位列表
	 */
	List<SysPostDTO> listPostsByUserId(Long userId);

	/**
	 * 查询岗位信息
	 *
	 * @param postId 岗位ID
	 * @return 岗位信息
	 */
	SysPostDTO getPostById(Long postId);

	/**
	 * 通过岗位ID查询岗位使用数量
	 *
	 * @param postId 岗位ID
	 * @return 结果
	 */
	int countUserPostById(Long postId);

	/**
	 * 新增岗位
	 *
	 * @param post 岗位信息
	 * @return 结果
	 */
	int insertPost(SysPostDTO post);

	/**
	 * 修改岗位
	 *
	 * @param post 岗位信息
	 * @return 结果
	 */
	int updatePost(SysPostDTO post);

	/**
	 * 批量删除岗位
	 *
	 * @param postIds 需要删除的数据ID
	 * @return 结果
	 */
	int deletePostByIds(Long[] postIds);

	/**
	 * 校验岗位名称
	 *
	 * @param post 岗位名称
	 * @return 结果
	 */
	boolean checkPostNameUnique(SysPostDTO post);

	/**
	 * 校验岗位编码
	 *
	 * @param post 岗位编码
	 * @return 结果
	 */
	boolean checkPostCodeUnique(SysPostDTO post);
}
