package com.jee.boot.system.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import com.jee.boot.common.utils.text.JeeStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jee.boot.system.mapper.SysConfigMapper;
import com.jee.boot.system.dto.SysConfigDTO;
import com.jee.boot.system.service.ISysConfigService;

/**
 * 参数配置 服务层实现
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService {
	@Autowired
	private SysConfigMapper configMapper;

	/**
     * 查询参数配置信息
     * 
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
	public SysConfigDTO getConfigById(Long configId) {
	    return configMapper.selectConfigById(configId);
	}
	
	/**
     * 查询参数配置列表
     * 
     * @param config 参数配置信息
     * @return 参数配置集合
     */
	@Override
	public List<SysConfigDTO> listConfig(SysConfigDTO config) {
	    return configMapper.selectConfigList(config);
	}
	
    /**
     * 新增参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
	@Override
	public int insertConfig(SysConfigDTO config) {
		config.setCreateTime(LocalDateTime.now());
	    return configMapper.insertConfig(config);
	}
	
	/**
     * 修改参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
	@Override
	public int updateConfig(SysConfigDTO config) {
		config.setUpdateTime(LocalDateTime.now());
	    return configMapper.updateConfig(config);
	}

	/**
     * 删除参数配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteConfigByIds(Long[] ids) {
		return configMapper.deleteConfigByIds(ids);
	}

	/**
	 * 校验参数键名是否唯一
	 *
	 * @param config 参数信息
	 * @return 结果
	 */
	@Override
	public boolean checkConfigKeyUnique(SysConfigDTO config) {
		Long configId = JeeStringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
		SysConfigDTO info = configMapper.selectConfigByKey(config.getConfigKey());
		if (JeeStringUtils.isNotNull(info) && info.getConfigId().longValue() != configId.longValue()){
			return false;
		}
		return true;
	}

	/**
	 * 根据键名查询参数配置信息
	 *
	 * @param configKey 参数key
	 * @return 参数键值
	 */
	@Override
	public String getConfigByKey(String configKey){
		SysConfigDTO config = new SysConfigDTO();
		config.setConfigKey(configKey);
		SysConfigDTO retConfig = configMapper.selectConfig(config);
		if (JeeStringUtils.isNotNull(retConfig)){
			return retConfig.getConfigValue();
		}
		return JeeStringUtils.EMPTY;
	}
}
