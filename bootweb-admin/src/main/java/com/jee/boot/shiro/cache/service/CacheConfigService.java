package com.jee.boot.shiro.cache.service;

import com.jee.boot.shiro.cache.CacheConstant;
import com.jee.boot.shiro.cache.CacheUtils;
import com.jee.boot.common.utils.text.Convert;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.framework.cacheservice.ICacheConfigService;
import com.jee.boot.system.dto.SysConfigDTO;
import com.jee.boot.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * config 缓存服务层
 *    1. config_key  ===>  config_value
 *
 *
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class CacheConfigService implements ICacheConfigService {

    @Autowired
    private ISysConfigService configService;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        List<SysConfigDTO> configsList = configService.listConfig(new SysConfigDTO());
        for (SysConfigDTO config : configsList){
            CacheUtils.put(CacheConstant.SYS_CONFIG_CACHE, CacheConstant.SYS_CONFIG_KEY + config.getConfigKey(), config.getConfigValue());
        }
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    @Override
    public String getConfigByKey(String configKey){
        String configValue = Convert.toStr(CacheUtils.get(getCacheName(), getCacheKey(configKey)));
        if (JeeStringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        String retConfigValue = configService.getConfigByKey(configKey);
        if (JeeStringUtils.isNotEmpty(retConfigValue)){
            CacheUtils.put(getCacheName(), getCacheKey(configKey), retConfigValue);
        }
        return retConfigValue;
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfigDTO config){
        int row = configService.insertConfig(config);
        if (row > 0){
            CacheUtils.put(getCacheName(), getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(SysConfigDTO config){
        int row = configService.updateConfig(config);
        if (row > 0) {
            CacheUtils.put(getCacheName(), getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * 批量删除参数配置对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteConfigByIds(Long[] ids) {
        int row = configService.deleteConfigByIds(ids);
        if (row > 0){
            CacheUtils.removeAll(getCacheName());
        }
        return row;
    }

    /**
     * 清空缓存数据
     */
    @Override
    public void clearCache(){
        CacheUtils.removeAll(getCacheName());
    }




    /**
     * 获取cache name
     *
     * @return 缓存名
     */
    private String getCacheName(){
        return CacheConstant.SYS_CONFIG_CACHE;
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstant.SYS_CONFIG_KEY + configKey;
    }
}

