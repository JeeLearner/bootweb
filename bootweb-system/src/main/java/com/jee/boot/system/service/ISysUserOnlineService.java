package com.jee.boot.system.service;

import com.jee.boot.system.dto.SysUserOnlineDTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 在线用户记录 Service接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface ISysUserOnlineService {

	/**
     * 查询在线用户记录列表
     * 
     * @param userOnline 在线用户记录
     * @return 在线用户记录集合
     */
	List<SysUserOnlineDTO> listUserOnline(SysUserOnlineDTO userOnline);

	/**
	 * 查询在线用户记录信息
	 *
	 * @param sessionId 在线用户记录ID
	 * @return 在线用户记录信息
	 */
	SysUserOnlineDTO getUserOnlineById(String sessionId);
	
	/**
     * 新增在线用户记录
     * 
     * @param userOnline 在线用户记录
     * @return 结果
     */
	int insertUserOnline(SysUserOnlineDTO userOnline);
	
	/**
     * 修改在线用户记录
     * 
     * @param userOnline 在线用户记录
     * @return 结果
     */
	int updateUserOnline(SysUserOnlineDTO userOnline);

	/**
	 * 删除在线用户记录信息
	 *
	 * @param sessionId 在线用户记录ID
	 * @return 结果
	 */
	int deleteUserOnlineById(String sessionId);

	/**
     * 批量删除在线用户记录信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteUserOnlineByIds(String[] ids);

	/**
	 * 查询会话集合
	 *
	 * @param expiredDate 有效期
	 * @return 会话集合
	 */
	List<SysUserOnlineDTO> listOnlineByExpired(LocalDateTime expiredDate);

	/**
	 * 强退用户
	 *
	 * @param sessionId 会话ID
	 */
	int forceLogout(String sessionId);
}
