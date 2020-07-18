package com.jee.boot.system.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jee.boot.system.mapper.SysDictDataMapper;
import com.jee.boot.system.dto.SysDictDataDTO;
import com.jee.boot.system.service.ISysDictDataService;

/**
 * 字典数据 服务层实现
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService {
	@Autowired
	private SysDictDataMapper dictDataMapper;

	/**
     * 查询字典数据信息
     * 
     * @param dictCode 字典数据ID
     * @return 字典数据信息
     */
    @Override
	public SysDictDataDTO getDictDataById(Long dictCode) {
	    return dictDataMapper.selectDictDataById(dictCode);
	}
	
	/**
     * 查询字典数据列表
     * 
     * @param dictData 字典数据信息
     * @return 字典数据集合
     */
	@Override
	public List<SysDictDataDTO> listDictData(SysDictDataDTO dictData) {
	    return dictDataMapper.selectDictDataList(dictData);
	}
	
    /**
     * 新增字典数据
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
	@Override
	public int insertDictData(SysDictDataDTO dictData) {
		dictData.setCreateTime(LocalDateTime.now());
	    return dictDataMapper.insertDictData(dictData);
	}
	
	/**
     * 修改字典数据
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
	@Override
	public int updateDictData(SysDictDataDTO dictData) {
		dictData.setUpdateTime(LocalDateTime.now());
	    return dictDataMapper.updateDictData(dictData);
	}

	/**
     * 删除字典数据对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteDictDataByIds(Long[] ids) {
		return dictDataMapper.deleteDictDataByIds(ids);
	}

	/**
	 * 根据字典类型查询字典数据
	 *
	 * @param dictType 字典类型
	 * @return 字典数据集合信息
	 */
	@Override
	public List<SysDictDataDTO> listDictDataByType(String dictType) {
		return dictDataMapper.selectDictDataByType(dictType);
	}

	/**
	 * 根据字典类型和字典键值查询字典数据信息
	 *
	 * @param dictType 字典类型
	 * @param dictValue 字典键值
	 * @return 字典标签
	 */
	@Override
	public String getDictLabel(String dictType, String dictValue) {
		return dictDataMapper.selectDictLabel(dictType, dictValue);
	}

}
