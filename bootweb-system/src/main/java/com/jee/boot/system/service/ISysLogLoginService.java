package com.jee.boot.system.service;

import com.jee.boot.system.dto.SysLogLoginDTO;
import java.util.List;

/**
 * 系统访问记录 Service接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface ISysLogLoginService {

	/**
     * 查询系统访问记录列表
     * 
     * @param logLogin 系统访问记录
     * @return 系统访问记录集合
     */
	List<SysLogLoginDTO> listLogLogin(SysLogLoginDTO logLogin);

	/**
	 * 查询系统访问记录信息
	 *
	 * @param infoId 系统访问记录ID
	 * @return 系统访问记录信息
	 */
		SysLogLoginDTO selectLogLoginById(Long infoId);
	
	/**
     * 新增系统访问记录
     * 
     * @param logLogin 系统访问记录
     * @return 结果
     */
	int insertLogLogin(SysLogLoginDTO logLogin);
	
	/**
     * 修改系统访问记录
     * 
     * @param logLogin 系统访问记录
     * @return 结果
     */
	int updateLogLogin(SysLogLoginDTO logLogin);

	/**
	 * 删除系统访问记录信息
	 *
	 * @param infoId 系统访问记录ID
	 * @return 结果
	 */
	int deleteLogLoginById(Long infoId);

	/**
     * 批量删除系统访问记录信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteLogLoginByIds(Long[] ids);

	/**
	 * 清空系统登录日志
	 */
	void deleteLogLoginAll();
}
