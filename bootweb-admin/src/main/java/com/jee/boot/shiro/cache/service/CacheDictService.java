package com.jee.boot.shiro.cache.service;

import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.framework.cacheservice.ICacheDictService;
import com.jee.boot.shiro.cache.CacheConstant;
import com.jee.boot.shiro.cache.CacheUtils;
import com.jee.boot.system.dto.SysDictDataDTO;
import com.jee.boot.system.dto.SysDictTypeDTO;
import com.jee.boot.system.service.ISysDictDataService;
import com.jee.boot.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * dict 缓存服务层
 *    1.dict_type ==> List<dictData>
 *
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class CacheDictService implements ICacheDictService {

    @Autowired
    private ISysDictTypeService dictTypeService;
    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        List<SysDictTypeDTO> dictTypeList = dictTypeService.listDictTypeAll();
        for (SysDictTypeDTO dictType : dictTypeList) {
            List<SysDictDataDTO> dictDataList = dictDataService.listDictDataByType(dictType.getDictType());
            CacheUtils.put(getCacheName(), getCacheKey(dictType.getDictType()), dictDataList);
        }
    }

    @Override
    public List<SysDictDataDTO> listDictDataByType(String dictType) {
        Object cacheObj = CacheUtils.get(getCacheName(), getCacheKey(dictType));
        if (JeeStringUtils.isNotNull(cacheObj)){
            List<SysDictDataDTO> DictDatas = JeeStringUtils.cast(cacheObj);
            return DictDatas;
        }
        List<SysDictDataDTO> dictDatas = dictDataService.listDictDataByType(dictType);
        if (JeeStringUtils.isNotNull(dictDatas)){
            CacheUtils.put(getCacheName(), getCacheKey(dictType), dictDatas);
            return dictDatas;
        }
        return null;
    }

    @Override
    public int insertDictType(SysDictTypeDTO dictType) {
        int row = dictTypeService.insertDictType(dictType);
        if (row > 0){
            CacheUtils.removeAll(getCacheName());
        }
        return row;
    }

    @Override
    public int updateDictType(SysDictTypeDTO dictType) {
        int row = dictTypeService.updateDictType(dictType);
        if (row > 0){
            CacheUtils.removeAll(getCacheName());
        }
        return row;
    }

    @Override
    public int deleteDictTypeByIds(Long[] ids) {
        int row = dictTypeService.deleteDictTypeByIds(ids);
        if (row > 0){
            CacheUtils.removeAll(getCacheName());
        }
        return row;
    }

    @Override
    public int insertDictData(SysDictDataDTO dictData) {
        int row = dictDataService.insertDictData(dictData);
        if (row > 0){
            CacheUtils.removeAll(getCacheName());
        }
        return row;
    }

    @Override
    public int updateDictData(SysDictDataDTO dictData) {
        int row = dictDataService.updateDictData(dictData);
        if (row > 0){
            CacheUtils.removeAll(getCacheName());
        }
        return row;
    }

    @Override
    public int deleteDictDataByIds(Long[] ids) {
        int row = dictDataService.deleteDictDataByIds(ids);
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
    public static String getCacheName() {
        return CacheConstant.SYS_DICT_CACHE;
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey) {
        return CacheConstant.SYS_DICT_KEY + configKey;
    }
}

