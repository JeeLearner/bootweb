package com.jee.boot.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jee.boot.system.mapper.SysLogOperMapper;
import com.jee.boot.system.dto.SysLogOperDTO;
import com.jee.boot.system.service.ISysLogOperService;

/**
 * 操作日志记录 Service业务层处理
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class SysLogOperServiceImpl implements ISysLogOperService {
	@Autowired
	private SysLogOperMapper logOperMapper;

	/**
     * 查询操作日志记录列表
     * 
     * @param logOper 操作日志记录
     * @return 操作日志记录集合
     */
	@Override
	public List<SysLogOperDTO> listLogOper(SysLogOperDTO logOper) {
	    return logOperMapper.selectLogOperList(logOper);
	}

	/**
	 * 查询操作日志记录信息
	 *
	 * @param operId 操作日志记录ID
	 * @return 操作日志记录
	 */
	@Override
	public SysLogOperDTO getLogOperById(Long operId) {
		return logOperMapper.selectLogOperById(operId);
	}

    /**
     * 新增操作日志记录
     * 
     * @param logOper 操作日志记录
     * @return 结果
     */
	@Override
	public int insertLogOper(SysLogOperDTO logOper) {
																	    return logOperMapper.insertLogOper(logOper);
	}
	
	/**
     * 修改操作日志记录
     * 
     * @param logOper 操作日志记录
     * @return 结果
     */
	@Override
	public int updateLogOper(SysLogOperDTO logOper) {
																	    return logOperMapper.updateLogOper(logOper);
	}

	/**
	 * 删除操作日志记录信息
	 *
	 * @param operId 操作日志记录ID
	 * @return 结果
	 */
	@Override
	public int deleteLogOperById(Long operId) {
		return logOperMapper.deleteLogOperById(operId);
	}

	/**
     * 删除操作日志记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteLogOperByIds(Long[] ids) {
		return logOperMapper.deleteLogOperByIds(ids);
	}

	/**
	 * 清空操作日志
	 */
	@Override
	public void deleteOperLogAll(){
		logOperMapper.deleteOperLogAll();
	}
}
