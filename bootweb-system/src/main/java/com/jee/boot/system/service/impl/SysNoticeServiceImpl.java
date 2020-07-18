package com.jee.boot.system.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jee.boot.system.mapper.SysNoticeMapper;
import com.jee.boot.system.dto.SysNoticeDTO;
import com.jee.boot.system.service.ISysNoticeService;

/**
 * 通知公告 Service业务层处理
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService {
	@Autowired
	private SysNoticeMapper noticeMapper;

	/**
     * 查询通知公告列表
     * 
     * @param notice 通知公告
     * @return 通知公告集合
     */
	@Override
	public List<SysNoticeDTO> listNotice(SysNoticeDTO notice) {
	    return noticeMapper.selectNoticeList(notice);
	}

	/**
	 * 查询通知公告信息
	 *
	 * @param noticeId 通知公告ID
	 * @return 通知公告
	 */
	@Override
	public SysNoticeDTO getNoticeById(Integer noticeId) {
		return noticeMapper.selectNoticeById(noticeId);
	}
	
    /**
     * 新增通知公告
     * 
     * @param notice 通知公告
     * @return 结果
     */
	@Override
	public int insertNotice(SysNoticeDTO notice) {
		notice.setCreateTime(LocalDateTime.now());
		return noticeMapper.insertNotice(notice);
	}
	
	/**
     * 修改通知公告
     * 
     * @param notice 通知公告
     * @return 结果
     */
	@Override
	public int updateNotice(SysNoticeDTO notice) {
		notice.setUpdateTime(LocalDateTime.now());
		return noticeMapper.updateNotice(notice);
	}

	/**
	 * 删除通知公告信息
	 *
	 * @param noticeId 通知公告ID
	 * @return 结果
	 */
	@Override
	public int deleteNoticeById(Long noticeId) {
		return noticeMapper.deleteNoticeById(noticeId);
	}

	/**
     * 删除通知公告对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteNoticeByIds(Long[] ids) {
		return noticeMapper.deleteNoticeByIds(ids);
	}
}
