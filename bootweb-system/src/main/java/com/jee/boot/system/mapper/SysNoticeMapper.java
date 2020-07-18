package com.jee.boot.system.mapper;

import com.jee.boot.system.dto.SysNoticeDTO;
import com.jee.boot.system.domain.SysNotice;
import java.util.List;	

/**
 * 通知公告 Mapper接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysNoticeMapper {

	/**
     * 查询通知公告列表
     * 
     * @param notice 通知公告
     * @return 通知公告集合
     */
	List<SysNoticeDTO> selectNoticeList(SysNotice notice);

	/**
	 * 查询通知公告信息
	 *
	 * @param noticeId 通知公告ID
	 * @return 通知公告信息
	 */
	SysNoticeDTO selectNoticeById(Integer noticeId);
	
	/**
     * 新增通知公告
     * 
     * @param notice 通知公告
     * @return 结果
     */
	int insertNotice(SysNotice notice);
	
	/**
     * 修改通知公告
     * 
     * @param notice 通知公告
     * @return 结果
     */
	int updateNotice(SysNotice notice);
	
	/**
     * 删除通知公告
     * 
     * @param noticeId 通知公告ID
     * @return 结果
     */
	int deleteNoticeById(Long noticeId);
	
	/**
     * 批量删除通知公告
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteNoticeByIds(Long[] ids);
}