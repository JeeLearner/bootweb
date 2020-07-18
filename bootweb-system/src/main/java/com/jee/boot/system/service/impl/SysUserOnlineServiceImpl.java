package com.jee.boot.system.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.jee.boot.common.utils.DateUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jee.boot.system.mapper.SysUserOnlineMapper;
import com.jee.boot.system.dto.SysUserOnlineDTO;
import com.jee.boot.system.service.ISysUserOnlineService;

/**
 * 在线用户记录 Service业务层处理
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService {
	@Autowired
	private SysUserOnlineMapper userOnlineMapper;

	/**
     * 查询在线用户记录列表
     * 
     * @param userOnline 在线用户记录
     * @return 在线用户记录集合
     */
	@Override
	public List<SysUserOnlineDTO> listUserOnline(SysUserOnlineDTO userOnline) {
	    return userOnlineMapper.selectUserOnlineList(userOnline);
	}

	/**
	 * 查询在线用户记录信息
	 *
	 * @param sessionId 在线用户记录ID
	 * @return 在线用户记录
	 */
	@Override
	public SysUserOnlineDTO getUserOnlineById(String sessionId) {
		return userOnlineMapper.selectUserOnlineById(sessionId);
	}
	
    /**
     * 新增在线用户记录
     * 
     * @param userOnline 在线用户记录
     * @return 结果
     */
	@Override
	public int insertUserOnline(SysUserOnlineDTO userOnline) {
		return userOnlineMapper.insertUserOnline(userOnline);
	}
	
	/**
     * 修改在线用户记录
     * 
     * @param userOnline 在线用户记录
     * @return 结果
     */
	@Override
	public int updateUserOnline(SysUserOnlineDTO userOnline) {
												    return userOnlineMapper.updateUserOnline(userOnline);
	}

	/**
	 * 删除在线用户记录信息
	 *
	 * @param sessionId 在线用户记录ID
	 * @return 结果
	 */
	@Override
	public int deleteUserOnlineById(String sessionId) {
		SysUserOnlineDTO userOnline = getUserOnlineById(sessionId);
		if (JeeStringUtils.isNotNull(userOnline)){
			return userOnlineMapper.deleteUserOnlineById(sessionId);
		}
		return 0;
	}

	/**
     * 删除在线用户记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteUserOnlineByIds(String[] ids) {
		AtomicInteger ai = new AtomicInteger(0);
		for (String sessionId : ids) {
			SysUserOnlineDTO userOnline = getUserOnlineById(sessionId);
			if (JeeStringUtils.isNotNull(userOnline)){
				userOnlineMapper.deleteUserOnlineById(sessionId);
				ai.incrementAndGet();
			}
		}
		return ai.get();
	}

	/**
	 * 查询会话集合
	 *
	 * @param expiredDate 失效日期
	 */
	@Override
	public List<SysUserOnlineDTO> listOnlineByExpired(LocalDateTime expiredDate) {
		String lastAccessTime = DateUtils.formatDateTime(expiredDate);
		return userOnlineMapper.selectOnlineByExpired(lastAccessTime);
	}

	/**
	 * 强退用户
	 *
	 * @param sessionId 会话ID
	 */
	@Override
	public int forceLogout(String sessionId) {
		return userOnlineMapper.deleteUserOnlineById(sessionId);
	}
}
