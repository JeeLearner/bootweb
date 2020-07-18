package com.jee.boot.system.service;

import com.jee.boot.system.dto.SysConfigDTO;
import java.util.List;

/**
 * 参数配置 服务层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface ISysConfigService {
	/**
     * 查询参数配置信息
     * 
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
	SysConfigDTO getConfigById(Long configId);
	
	/**
     * 查询参数配置列表
     * 
     * @param config 参数配置信息
     * @return 参数配置集合
     */
	List<SysConfigDTO> listConfig(SysConfigDTO config);
	
	/**
     * 新增参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
	int insertConfig(SysConfigDTO config);
	
	/**
     * 修改参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
	int updateConfig(SysConfigDTO config);
		
	/**
     * 批量删除参数配置信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteConfigByIds(Long[] ids);

	/**
	 * 校验参数键名是否唯一
	 *
	 * @param config 参数信息
	 * @return 结果
	 */
	boolean checkConfigKeyUnique(SysConfigDTO config);

	/**
	 * 根据键名查询参数配置信息
	 *
	 * @param configKey 参数键名
	 * @return 参数键值
	 */
	String getConfigByKey(String configKey);
}
