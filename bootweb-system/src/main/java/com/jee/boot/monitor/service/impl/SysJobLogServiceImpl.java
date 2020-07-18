package com.jee.boot.monitor.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jee.boot.monitor.mapper.SysJobLogMapper;
import com.jee.boot.monitor.dto.SysJobLogDTO;
import com.jee.boot.monitor.service.ISysJobLogService;
import com.jee.boot.common.utils.text.Convert;

/**
 * 定时任务调度日志 Service业务层处理
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class SysJobLogServiceImpl implements ISysJobLogService {
	@Autowired
	private SysJobLogMapper jobLogMapper;

	/**
     * 查询定时任务调度日志列表
     * 
     * @param jobLog 定时任务调度日志
     * @return 定时任务调度日志集合
     */
	@Override
	public List<SysJobLogDTO> listJobLog(SysJobLogDTO jobLog) {
	    return jobLogMapper.selectJobLogList(jobLog);
	}

	/**
	 * 查询定时任务调度日志信息
	 *
	 * @param jobLogId 定时任务调度日志ID
	 * @return 定时任务调度日志
	 */
	@Override
	public SysJobLogDTO getJobLogById(Long jobLogId) {
		return jobLogMapper.selectJobLogById(jobLogId);
	}
	
    /**
     * 新增定时任务调度日志
     * 
     * @param jobLog 定时任务调度日志
     * @return 结果
     */
	@Override
	public int insertJobLog(SysJobLogDTO jobLog) {
		jobLog.setCreateTime(LocalDateTime.now());
	    return jobLogMapper.insertJobLog(jobLog);
	}
	
	/**
     * 修改定时任务调度日志
     * 
     * @param jobLog 定时任务调度日志
     * @return 结果
     */
	@Override
	public int updateJobLog(SysJobLogDTO jobLog) {
		jobLog.setUpdateTime(LocalDateTime.now());
	    return jobLogMapper.updateJobLog(jobLog);
	}

	/**
	 * 删除定时任务调度日志信息
	 *
	 * @param jobLogId 定时任务调度日志ID
	 * @return 结果
	 */
	@Override
	public int deleteJobLogById(Long jobLogId) {
		return jobLogMapper.deleteJobLogById(jobLogId);
	}

	/**
     * 删除定时任务调度日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteJobLogByIds(Long[] ids) {
		return jobLogMapper.deleteJobLogByIds(ids);
	}

	/**
	 * 清空任务日志
	 */
	@Override
	public void deleteJobLogAll() {
		jobLogMapper.deleteJobLogAll();
	}
}
