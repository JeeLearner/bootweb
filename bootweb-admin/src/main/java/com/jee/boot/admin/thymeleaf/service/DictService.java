package com.jee.boot.admin.thymeleaf.service;

import com.jee.boot.framework.cacheservice.ICacheDictService;
import com.jee.boot.system.domain.SysDictData;
import com.jee.boot.system.dto.SysDictDataDTO;
import com.jee.boot.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * html调用 thymeleaf 实现字典读取
 *
 * @author jeeLearner
 */
@Service("dict")
public class DictService {

    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private ICacheDictService cacheDictService;


    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType 字典类型
     * @return 参数键值
     */
    public List<SysDictDataDTO> getType(String dictType) {
        return cacheDictService.listDictDataByType(dictType);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String getLabel(String dictType, String dictValue) {
        return dictDataService.getDictLabel(dictType, dictValue);
    }
}

