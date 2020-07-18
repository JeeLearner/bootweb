package com.jee.boot.system.mapper;

import com.jee.boot.system.dto.SysLogOperDTO;
import com.jee.boot.system.domain.SysLogOper;
import java.util.List;	

/**
 * 操作日志记录 Mapper接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysLogOperMapper {

	/**
     * 查询操作日志记录列表
     * 
     * @param logOper 操作日志记录
     * @return 操作日志记录集合
     */
	List<SysLogOperDTO> selectLogOperList(SysLogOper logOper);

	/**
	 * 查询操作日志记录信息
	 *
	 * @param operId 操作日志记录ID
	 * @return 操作日志记录信息
	 */
		SysLogOperDTO selectLogOperById(Long operId);
	
	/**
     * 新增操作日志记录
     * 
     * @param logOper 操作日志记录
     * @return 结果
     */
	int insertLogOper(SysLogOper logOper);
	
	/**
     * 修改操作日志记录
     * 
     * @param logOper 操作日志记录
     * @return 结果
     */
	int updateLogOper(SysLogOper logOper);
	
	/**
     * 删除操作日志记录
     * 
     * @param operId 操作日志记录ID
     * @return 结果
     */
	int deleteLogOperById(Long operId);
	
	/**
     * 批量删除操作日志记录
     * 
     * @param operIds 需要删除的数据ID
     * @return 结果
     */
	int deleteLogOperByIds(Long[] operIds);

	/**
	 * 清空操作日志
	 */
	 void deleteOperLogAll();
}