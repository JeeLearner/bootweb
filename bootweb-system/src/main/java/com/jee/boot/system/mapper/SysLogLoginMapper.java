package com.jee.boot.system.mapper;

import com.jee.boot.system.dto.SysLogLoginDTO;
import com.jee.boot.system.domain.SysLogLogin;
import java.util.List;	

/**
 * 系统访问记录 Mapper接口
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysLogLoginMapper {

	/**
     * 查询系统访问记录列表
     * 
     * @param logLogin 系统访问记录
     * @return 系统访问记录集合
     */
	List<SysLogLoginDTO> selectLogLoginList(SysLogLogin logLogin);

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
	int insertLogLogin(SysLogLogin logLogin);
	
	/**
     * 修改系统访问记录
     * 
     * @param logLogin 系统访问记录
     * @return 结果
     */
	int updateLogLogin(SysLogLogin logLogin);
	
	/**
     * 删除系统访问记录
     * 
     * @param infoId 系统访问记录ID
     * @return 结果
     */
	int deleteLogLoginById(Long infoId);
	
	/**
     * 批量删除系统访问记录
     * 
     * @param infoIds 需要删除的数据ID
     * @return 结果
     */
	int deleteLogLoginByIds(Long[] infoIds);

	/**
	 * 清空系统登录日志
	 *
	 * @return 结果
	 */
	void deleteLogLoginAll();
}