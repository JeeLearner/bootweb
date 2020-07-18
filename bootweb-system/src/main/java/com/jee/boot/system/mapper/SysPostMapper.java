package com.jee.boot.system.mapper;

import com.jee.boot.system.dto.SysPostDTO;
import com.jee.boot.system.domain.SysPost;
import java.util.List;	

/**
 * 岗位 数据层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysPostMapper {
	/**
	 * 查询岗位列表
	 *
	 * @param post 岗位信息
	 * @return 岗位集合
	 */
	List<SysPostDTO> selectPostList(SysPost post);

	/**
	 * 查询所有岗位
	 *
	 * @return 岗位列表
	 */
	List<SysPostDTO> selectPostAll();

	/**
	 * 根据用户ID查询岗位
	 *
	 * @param userId 用户ID
	 * @return 岗位列表
	 */
	List<SysPostDTO> selectPostsByUserId(Long userId);

	/**
     * 查询岗位信息
     * 
     * @param postId 岗位ID
     * @return 岗位信息
     */
	SysPostDTO selectPostById(Long postId);

	/**
     * 新增岗位
     * 
     * @param post 岗位信息
     * @return 结果
     */
	int insertPost(SysPost post);
	
	/**
     * 修改岗位
     * 
     * @param post 岗位信息
     * @return 结果
     */
	int updatePost(SysPost post);
	
	/**
     * 删除岗位
     * 
     * @param postId 岗位ID
     * @return 结果
     */
	int deletePostById(Long postId);
	
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
	 * @param postName 岗位名称
	 * @return 结果
	 */
	SysPostDTO checkPostNameUnique(String postName);

	/**
	 * 校验岗位编码
	 *
	 * @param postCode 岗位编码
	 * @return 结果
	 */
	SysPostDTO checkPostCodeUnique(String postCode);
}