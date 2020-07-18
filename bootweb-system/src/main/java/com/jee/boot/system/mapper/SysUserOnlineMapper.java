package com.jee.boot.system.mapper;

import com.jee.boot.system.dto.SysUserOnlineDTO;
import com.jee.boot.system.domain.SysUserOnline;
import java.util.List;	

/**
 * 在线用户记录 Mapper接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysUserOnlineMapper {

	/**
     * 查询在线用户记录列表
     * 
     * @param userOnline 在线用户记录
     * @return 在线用户记录集合
     */
	List<SysUserOnlineDTO> selectUserOnlineList(SysUserOnline userOnline);

	/**
	 * 查询在线用户记录信息
	 *
	 * @param sessionId 在线用户记录ID
	 * @return 在线用户记录信息
	 */
	SysUserOnlineDTO selectUserOnlineById(String sessionId);
	
	/**
     * 新增在线用户记录
     * 
     * @param userOnline 在线用户记录
     * @return 结果
     */
	int insertUserOnline(SysUserOnline userOnline);
	
	/**
     * 修改在线用户记录
     * 
     * @param userOnline 在线用户记录
     * @return 结果
     */
	int updateUserOnline(SysUserOnline userOnline);
	
	/**
     * 删除在线用户记录
     * 
     * @param sessionId 在线用户记录ID
     * @return 结果
     */
	int deleteUserOnlineById(String sessionId);
	
	/**
     * 批量删除在线用户记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteUserOnlineByIds(String[] ids);

	/**
	 * 查询过期会话集合
	 *
	 * @param lastAccessTime 过期时间
	 * @return 会话集合
	 */
	List<SysUserOnlineDTO> selectOnlineByExpired(String lastAccessTime);
}