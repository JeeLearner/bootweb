package com.jee.boot.monitor.service;

import com.jee.boot.monitor.dto.SysJobDTO;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 定时任务调度 Service接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface ISysJobService {

	/**
     * 查询定时任务调度列表
     * 
     * @param job 定时任务调度
     * @return 定时任务调度集合
     */
	List<SysJobDTO> listJob(SysJobDTO job);

	/**
	 * 查询所有调度任务
	 *
	 * @return 调度任务列表
	 */
	List<SysJobDTO> listJobAll();

	/**
	 * 查询定时任务调度信息
	 *
	 * @param jobId 定时任务调度ID
	 * @return 定时任务调度信息
	 */
	SysJobDTO getJobById(Long jobId);
	
	/**
     * 新增定时任务调度
     * 
     * @param job 定时任务调度
     * @return 结果
     */
	int insertJob(SysJobDTO job);
	
	/**
     * 修改定时任务调度
     * 
     * @param job 定时任务调度
     * @return 结果
     */
	int updateJob(SysJobDTO job);

	/**
	 * 修改状态
	 * @param job 调度信息
	 * @return
	 */
	int updateStatus(SysJobDTO job) throws SchedulerException;

	/**
	 * 删除定时任务调度信息
	 *
	 * @param jobId 定时任务调度ID
	 * @return 结果
	 */
	int deleteJobById(Long jobId);

	/**
     * 批量删除定时任务调度信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteJobByIds(Long[] ids);

	/**
	 * 暂停任务
	 *
	 * @param job 调度信息
	 * @return 结果
	 */
	int pauseJob(SysJobDTO job) throws SchedulerException;

	/**
	 * 恢复任务
	 *
	 * @param job 调度信息
	 * @return 结果
	 */
	int resumeJob(SysJobDTO job) throws SchedulerException;

	/**
	 * 立即运行任务
	 *
	 * @param job 调度信息
	 * @throws SchedulerException
	 */
	void run(SysJobDTO job) throws SchedulerException;

	/**
	 * 校验cron表达式是否有效
	 *
	 * @param cronExpression 表达式
	 * @return 结果
	 */
	boolean checkCronExpressionIsValid(String cronExpression);
}
