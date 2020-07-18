package com.jee.boot.framework.cacheservice;

import com.jee.boot.system.dto.SysConfigDTO;

/**
 * config 缓存服务 接口
 *
 * @author jeeLearner
 * @version V1.0
 */
public interface ICacheConfigService {
    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    String getConfigByKey(String configKey);

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
     * 批量删除参数配置对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteConfigByIds(Long[] ids);

    /**
     * 清空缓存数据
     */
    void clearCache();
}
