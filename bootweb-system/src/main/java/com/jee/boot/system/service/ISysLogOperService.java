package com.jee.boot.system.service;

import com.jee.boot.system.dto.SysLogOperDTO;
import java.util.List;

/**
 * 操作日志记录 Service接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface ISysLogOperService {

	/**
     * 查询操作日志记录列表
     * 
     * @param logOper 操作日志记录
     * @return 操作日志记录集合
     */
	List<SysLogOperDTO> listLogOper(SysLogOperDTO logOper);

	/**
	 * 查询操作日志记录信息
	 *
	 * @param operId 操作日志记录ID
	 * @return 操作日志记录信息
	 */
	SysLogOperDTO getLogOperById(Long operId);
	
	/**
     * 新增操作日志记录
     * 
     * @param logOper 操作日志记录
     * @return 结果
     */
	int insertLogOper(SysLogOperDTO logOper);
	
	/**
     * 修改操作日志记录
     * 
     * @param logOper 操作日志记录
     * @return 结果
     */
	int updateLogOper(SysLogOperDTO logOper);

	/**
	 * 删除操作日志记录信息
	 *
	 * @param operId 操作日志记录ID
	 * @return 结果
	 */
	int deleteLogOperById(Long operId);

	/**
     * 批量删除操作日志记录信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteLogOperByIds(Long[] ids);

	/**
	 * 清空操作日志
	 */
	void deleteOperLogAll();
}
