package com.jee.boot.monitor.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.jee.boot.common.utils.cron.CronUtils;
import com.jee.boot.support.job.JobUtils;
import com.jee.boot.support.job.constant.JobConstants;
import com.jee.boot.support.job.constant.JobConstants.Status;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jee.boot.monitor.mapper.SysJobMapper;
import com.jee.boot.monitor.dto.SysJobDTO;
import com.jee.boot.monitor.service.ISysJobService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定时任务调度 Service业务层处理
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class SysJobServiceImpl implements ISysJobService {
	@Autowired
	private SysJobMapper jobMapper;

	@Autowired
	private Scheduler scheduler;

	/**
     * 查询定时任务调度列表
     * 
     * @param job 定时任务调度
     * @return 定时任务调度集合
     */
	@Override
	public List<SysJobDTO> listJob(SysJobDTO job) {
	    return jobMapper.selectJobList(job);
	}

	/**
	 * 查询所有调度任务
	 *
	 * @return 调度任务列表
	 */
	@Override
	public List<SysJobDTO> listJobAll() {
		return jobMapper.selectJobAll();
	}

	/**
	 * 查询定时任务调度信息
	 *
	 * @param jobId 定时任务调度ID
	 * @return 定时任务调度
	 */
	@Override
	public SysJobDTO getJobById(Long jobId) {
		return jobMapper.selectJobById(jobId);
	}
	
    /**
     * 新增定时任务调度
     * 
     * @param job 定时任务调度
     * @return 结果
     */
	@Override
	public int insertJob(SysJobDTO job) {
		job.setCreateTime(LocalDateTime.now());
	    return jobMapper.insertJob(job);
	}
	
	/**
     * 修改定时任务调度
     * 
     * @param job 定时任务调度
     * @return 结果
     */
	@Override
	public int updateJob(SysJobDTO job) {
		job.setUpdateTime(LocalDateTime.now());
	    return jobMapper.updateJob(job);
	}

	/**
	 * 修改状态
	 * @param job
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateStatus(SysJobDTO job) throws SchedulerException {
		int rows = 0;
		String status = job.getStatus();
		if (Status.NORMAL.getValue().equals(status)){
			rows = resumeJob(job);
		} else if(Status.PAUSE.getValue().equals(status)){
			rows = pauseJob(job);
		}
		return rows;
	}

	/**
	 * 删除定时任务调度信息
	 *
	 * @param jobId 定时任务调度ID
	 * @return 结果
	 */
	@Override
	public int deleteJobById(Long jobId) {
		return jobMapper.deleteJobById(jobId);
	}

	/**
     * 删除定时任务调度对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteJobByIds(Long[] ids) {
		return jobMapper.deleteJobByIds(ids);
	}

	/**
	 * 暂停任务
	 *
	 * @param job 调度信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int pauseJob(SysJobDTO job) throws SchedulerException {
		Long jobId = job.getJobId();
		String jobGroup = job.getJobGroup();
		job.setStatus(Status.PAUSE.getValue());
		int rows = jobMapper.updateJob(job);
		if (rows > 0){
			scheduler.pauseJob(JobUtils.getJobKey(jobId, jobGroup));
		}
		return rows;
	}

	/**
	 * 恢复任务
	 *
	 * @param job 调度信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int resumeJob(SysJobDTO job) throws SchedulerException {
		Long jobId = job.getJobId();
		String jobGroup = job.getJobGroup();
		job.setStatus(Status.NORMAL.getValue());
		int rows = jobMapper.updateJob(job);
		if (rows > 0){
			scheduler.resumeJob(JobUtils.getJobKey(jobId, jobGroup));
		}
		return rows;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void run(SysJobDTO job) throws SchedulerException {
		Long jobId = job.getJobId();
		SysJobDTO dbJob = getJobById(jobId);
		// 参数
		JobDataMap dataMap = new JobDataMap();
		dataMap.put(JobConstants.TASK_PROPERTIES, dbJob);
		scheduler.triggerJob(JobUtils.getJobKey(jobId, dbJob.getJobGroup()), dataMap);
	}

	/**
	 * 校验cron表达式是否有效
	 *
	 * @param cronExpression 表达式
	 * @return 结果
	 */
	@Override
	public boolean checkCronExpressionIsValid(String cronExpression) {
		return CronUtils.isValid(cronExpression);
	}
}
