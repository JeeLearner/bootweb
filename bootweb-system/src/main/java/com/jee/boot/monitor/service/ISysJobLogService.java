package com.jee.boot.monitor.service;

import com.jee.boot.monitor.dto.SysJobLogDTO;
import java.util.List;

/**
 * 定时任务调度日志 Service接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface ISysJobLogService {

	/**
     * 查询定时任务调度日志列表
     * 
     * @param jobLog 定时任务调度日志
     * @return 定时任务调度日志集合
     */
	List<SysJobLogDTO> listJobLog(SysJobLogDTO jobLog);

	/**
	 * 查询定时任务调度日志信息
	 *
	 * @param jobLogId 定时任务调度日志ID
	 * @return 定时任务调度日志信息
	 */
	SysJobLogDTO getJobLogById(Long jobLogId);
	
	/**
     * 新增定时任务调度日志
     * 
     * @param jobLog 定时任务调度日志
     * @return 结果
     */
	int insertJobLog(SysJobLogDTO jobLog);
	
	/**
     * 修改定时任务调度日志
     * 
     * @param jobLog 定时任务调度日志
     * @return 结果
     */
	int updateJobLog(SysJobLogDTO jobLog);

	/**
	 * 删除定时任务调度日志信息
	 *
	 * @param jobLogId 定时任务调度日志ID
	 * @return 结果
	 */
	int deleteJobLogById(Long jobLogId);

	/**
     * 批量删除定时任务调度日志信息
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
