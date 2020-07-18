package com.jee.boot.framework.cacheservice;

import com.jee.boot.system.dto.SysDictDataDTO;
import com.jee.boot.system.dto.SysDictTypeDTO;

import java.util.List;

/**
 * dict 缓存服务 接口
 *
 * @author jeeLearner
 * @version V1.0
 */
public interface ICacheDictService {
    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    List<SysDictDataDTO> listDictDataByType(String dictType);

    /**
     * 新增保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    int insertDictType(SysDictTypeDTO dictType);
    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    int updateDictType(SysDictTypeDTO dictType);
    /**
     * 批量删除字典类型
     *
     * @param ids 需要删除的数据
     * @return 结果
     */
    int deleteDictTypeByIds(Long[] ids);


    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    int insertDictData(SysDictDataDTO dictData);
    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    int updateDictData(SysDictDataDTO dictData);
    /**
     * 批量删除字典数据
     *
     * @param ids 需要删除的数据
     * @return 结果
     */
    int deleteDictDataByIds(Long[] ids);


    /**
     * 清空缓存数据
     */
    void clearCache();
}
