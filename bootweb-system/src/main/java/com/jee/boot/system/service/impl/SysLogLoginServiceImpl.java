package com.jee.boot.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jee.boot.system.mapper.SysLogLoginMapper;
import com.jee.boot.system.dto.SysLogLoginDTO;
import com.jee.boot.system.service.ISysLogLoginService;

/**
 * 系统访问记录 Service业务层处理
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class SysLogLoginServiceImpl implements ISysLogLoginService {
	@Autowired
	private SysLogLoginMapper logLoginMapper;

	/**
     * 查询系统访问记录列表
     * 
     * @param logLogin 系统访问记录
     * @return 系统访问记录集合
     */
	@Override
	public List<SysLogLoginDTO> listLogLogin(SysLogLoginDTO logLogin) {
	    return logLoginMapper.selectLogLoginList(logLogin);
	}

	/**
	 * 查询系统访问记录信息
	 *
	 * @param infoId 系统访问记录ID
	 * @return 系统访问记录
	 */
	@Override
	public SysLogLoginDTO selectLogLoginById(Long infoId) {
		return logLoginMapper.selectLogLoginById(infoId);
	}
	
    /**
     * 新增系统访问记录
     * 
     * @param logLogin 系统访问记录
     * @return 结果
     */
	@Override
	public int insertLogLogin(SysLogLoginDTO logLogin) {
										    return logLoginMapper.insertLogLogin(logLogin);
	}
	
	/**
     * 修改系统访问记录
     * 
     * @param logLogin 系统访问记录
     * @return 结果
     */
	@Override
	public int updateLogLogin(SysLogLoginDTO logLogin) {
										    return logLoginMapper.updateLogLogin(logLogin);
	}

	/**
	 * 删除系统访问记录信息
	 *
	 * @param infoId 系统访问记录ID
	 * @return 结果
	 */
	@Override
	public int deleteLogLoginById(Long infoId) {
		return logLoginMapper.deleteLogLoginById(infoId);
	}

	/**
     * 删除系统访问记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteLogLoginByIds(Long[] ids) {
		return logLoginMapper.deleteLogLoginByIds(ids);
	}

	/**
	 * 清空系统登录日志
	 */
	@Override
	public void deleteLogLoginAll() {
		logLoginMapper.deleteLogLoginAll();
	}
}
