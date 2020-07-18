package com.jee.boot.api.cache;

import com.jee.boot.api.redis.RedisCache;
import com.jee.boot.common.utils.text.Convert;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.framework.cacheservice.ICacheConfigService;
import com.jee.boot.system.dto.SysConfigDTO;
import com.jee.boot.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * config 服务层
 *    带缓存版
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class CacheConfigService implements ICacheConfigService {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        List<SysConfigDTO> configsList = configService.listConfig(new SysConfigDTO());
        for (SysConfigDTO config : configsList){
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
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
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
        if (JeeStringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        String retConfigValue = configService.getConfigByKey(configKey);
        if (JeeStringUtils.isNotEmpty(retConfigValue)){
            redisCache.setCacheObject(getCacheKey(configKey), retConfigValue);
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
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
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
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
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
        int count = configService.deleteConfigByIds(ids);
        if (count > 0){
            Collection<String> keys = redisCache.keys(getCacheKey("*"));
            redisCache.deleteObject(keys);
        }
        return count;
    }

    /**
     * 清空缓存数据
     */
    @Override
    public void clearCache(){
        Collection<String> keys = redisCache.keys(getCacheKey("*"));
        redisCache.deleteObject(keys);
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

