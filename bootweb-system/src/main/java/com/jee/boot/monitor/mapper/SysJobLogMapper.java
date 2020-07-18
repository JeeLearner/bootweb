package com.jee.boot.monitor.mapper;

import com.jee.boot.monitor.dto.SysJobLogDTO;
import com.jee.boot.monitor.domain.SysJobLog;
import java.util.List;	

/**
 * 定时任务调度日志 Mapper接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysJobLogMapper {

	/**
     * 查询定时任务调度日志列表
     * 
     * @param jobLog 定时任务调度日志
     * @return 定时任务调度日志集合
     */
	List<SysJobLogDTO> selectJobLogList(SysJobLog jobLog);

	/**
	 * 查询定时任务调度日志信息
	 *
	 * @param jobLogId 定时任务调度日志ID
	 * @return 定时任务调度日志信息
	 */
	SysJobLogDTO selectJobLogById(Long jobLogId);
	
	/**
     * 新增定时任务调度日志
     * 
     * @param jobLog 定时任务调度日志
     * @return 结果
     */
	int insertJobLog(SysJobLog jobLog);
	
	/**
     * 修改定时任务调度日志
     * 
     * @param jobLog 定时任务调度日志
     * @return 结果
     */
	int updateJobLog(SysJobLog jobLog);
	
	/**
     * 删除定时任务调度日志
     * 
     * @param jobLogId 定时任务调度日志ID
     * @return 结果
     */
	int deleteJobLogById(Long jobLogId);
	
	/**
     * 批量删除定时任务调度日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteJobLogByIds(Long[] ids);

	/**
	 * 清空任务日志
	 */
    void deleteJobLogAll();
}