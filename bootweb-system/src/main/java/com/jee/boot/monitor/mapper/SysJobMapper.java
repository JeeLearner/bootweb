package com.jee.boot.monitor.mapper;

import com.jee.boot.monitor.dto.SysJobDTO;
import com.jee.boot.monitor.domain.SysJob;
import java.util.List;	

/**
 * 定时任务调度 Mapper接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysJobMapper {

	/**
     * 查询定时任务调度列表
     * 
     * @param job 定时任务调度
     * @return 定时任务调度集合
     */
	List<SysJobDTO> selectJobList(SysJob job);

	/**
	 * 查询所有调度任务
	 *
	 * @return 调度任务列表
	 */
	List<SysJobDTO> selectJobAll();

	/**
	 * 查询定时任务调度信息
	 *
	 * @param jobId 定时任务调度ID
	 * @return 定时任务调度信息
	 */
	SysJobDTO selectJobById(Long jobId);
	
	/**
     * 新增定时任务调度
     * 
     * @param job 定时任务调度
     * @return 结果
     */
	int insertJob(SysJob job);
	
	/**
     * 修改定时任务调度
     * 
     * @param job 定时任务调度
     * @return 结果
     */
	int updateJob(SysJob job);
	
	/**
     * 删除定时任务调度
     * 
     * @param jobId 定时任务调度ID
     * @return 结果
     */
	int deleteJobById(Long jobId);
	
	/**
     * 批量删除定时任务调度
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteJobByIds(Long[] ids);
}