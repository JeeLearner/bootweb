package com.jee.boot.system.mapper;

import com.jee.boot.system.dto.SysConfigDTO;
import com.jee.boot.system.domain.SysConfig;
import java.util.List;	

/**
 * 参数配置 数据层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysConfigMapper {
	/**
     * 查询参数配置信息
     * 
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
	SysConfigDTO selectConfigById(Long configId);
	
	/**
     * 查询参数配置列表
     * 
     * @param config 参数配置信息
     * @return 参数配置集合
     */
	List<SysConfigDTO> selectConfigList(SysConfig config);
	
	/**
     * 新增参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
	int insertConfig(SysConfig config);
	
	/**
     * 修改参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
	int updateConfig(SysConfig config);
	
	/**
     * 删除参数配置
     * 
     * @param configId 参数配置ID
     * @return 结果
     */
	int deleteConfigById(Integer configId);
	
	/**
     * 批量删除参数配置
     * 
     * @param configIds 需要删除的数据ID
     * @return 结果
     */
	int deleteConfigByIds(Long[] configIds);

	/**
	 * 根据键名查询参数配置信息
	 *
	 * @param configKey 参数键名
	 * @return 参数配置信息
	 */
    SysConfigDTO selectConfigByKey(String configKey);

	/**
	 * 查询参数配置信息
	 *
	 * @param config 参数配置信息
	 * @return 参数配置信息
	 */
	SysConfigDTO selectConfig(SysConfigDTO config);
}