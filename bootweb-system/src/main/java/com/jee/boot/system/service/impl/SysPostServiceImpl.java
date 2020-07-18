package com.jee.boot.system.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.jee.boot.common.exception.base.BusinessException;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.system.mapper.SysUserPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jee.boot.system.mapper.SysPostMapper;
import com.jee.boot.system.dto.SysPostDTO;
import com.jee.boot.system.service.ISysPostService;

/**
 * 岗位管理 服务层实现
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class SysPostServiceImpl implements ISysPostService {
	@Autowired
	private SysPostMapper postMapper;
	@Autowired
	private SysUserPostMapper userPostMapper;

	/**
	 * 查询岗位列表
	 *
	 * @param post 岗位信息
	 * @return 岗位集合
	 */
	@Override
	public List<SysPostDTO> listPost(SysPostDTO post) {
		return postMapper.selectPostList(post);
	}

	@Override
	public List<SysPostDTO> listPostAll(){
		return postMapper.selectPostAll();
	}

	/**
	 * 根据用户ID查询岗位
	 *
	 * @param userId 用户ID
	 * @return 岗位列表
	 */
	@Override
	public List<SysPostDTO> listPostsByUserId(Long userId){
		List<SysPostDTO> userPosts = postMapper.selectPostsByUserId(userId);
		//查询所有岗位，userId关联的岗位属性置为true
		List<SysPostDTO> posts = postMapper.selectPostAll();
		for (SysPostDTO post : posts){
			for (SysPostDTO userRole : userPosts){
				if (post.getPostId().longValue() == userRole.getPostId().longValue()){
					post.setFlag(true);
					break;
				}
			}
		}
		return posts;
	}

	/**
     * 查询岗位信息
     * 
     * @param postId 岗位ID
     * @return 岗位信息
     */
    @Override
	public SysPostDTO getPostById(Long postId) {
	    return postMapper.selectPostById(postId);
	}

	/**
	 * 通过岗位ID查询岗位使用数量
	 *
	 * @param postId 岗位ID
	 * @return 结果
	 */
	@Override
	public int countUserPostById(Long postId){
		return userPostMapper.countUserPostById(postId);
	}

    /**
     * 新增岗位
     * 
     * @param post 岗位信息
     * @return 结果
     */
	@Override
	public int insertPost(SysPostDTO post) {
		post.setCreateTime(LocalDateTime.now());
	    return postMapper.insertPost(post);
	}
	
	/**
     * 修改岗位
     * 
     * @param post 岗位信息
     * @return 结果
     */
	@Override
	public int updatePost(SysPostDTO post) {
		post.setUpdateTime(LocalDateTime.now());
	    return postMapper.updatePost(post);
	}

	/**
     * 删除岗位对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePostByIds(Long[] ids) {
		for (Long postId : ids){
			SysPostDTO post = getPostById(postId);
			if (countUserPostById(postId) > 0){
				throw new BusinessException(String.format("%1$s已分配,不能删除", post.getPostName()));
			}
		}
		return postMapper.deletePostByIds(ids);
	}

	/**
	 * 校验岗位名称是否唯一
	 *
	 * @param post 岗位信息
	 * @return 结果
	 */
	@Override
	public boolean checkPostNameUnique(SysPostDTO post){
		Long configId = JeeStringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
		SysPostDTO info = postMapper.checkPostNameUnique(post.getPostName());
		if (JeeStringUtils.isNotNull(info) && info.getPostId().longValue() != configId.longValue()){
			return false;
		}
		return true;
	}

	/**
	 * 校验岗位编码是否唯一
	 *
	 * @param post 岗位信息
	 * @return 结果
	 */
	@Override
	public boolean checkPostCodeUnique(SysPostDTO post){
		Long configId = JeeStringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
		SysPostDTO info = postMapper.checkPostCodeUnique(post.getPostCode());
		if (JeeStringUtils.isNotNull(info) && info.getPostId().longValue() != configId.longValue()){
			return false;
		}
		return true;
	}
}
