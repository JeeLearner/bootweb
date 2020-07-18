package com.jee.boot.system.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.jee.boot.common.exception.base.BusinessException;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.system.mapper.SysDictDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jee.boot.system.mapper.SysDictTypeMapper;
import com.jee.boot.system.dto.SysDictTypeDTO;
import com.jee.boot.system.service.ISysDictTypeService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典类型 服务层实现
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService {
	@Autowired
	private SysDictTypeMapper dictTypeMapper;
	@Autowired
	private SysDictDataMapper dictDataMapper;

	/**
     * 查询字典类型信息
     * 
     * @param dictId 字典类型ID
     * @return 字典类型信息
     */
    @Override
	public SysDictTypeDTO getDictTypeById(Long dictId) {
	    return dictTypeMapper.selectDictTypeById(dictId);
	}
	
	/**
     * 查询字典类型列表
     * 
     * @param dictType 字典类型信息
     * @return 字典类型集合
     */
	@Override
	public List<SysDictTypeDTO> listDictType(SysDictTypeDTO dictType) {
	    return dictTypeMapper.selectDictTypeList(dictType);
	}
	
    /**
     * 新增字典类型
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
	@Override
	public int insertDictType(SysDictTypeDTO dictType) {
		dictType.setCreateTime(LocalDateTime.now());
	    return dictTypeMapper.insertDictType(dictType);
	}
	
	/**
     * 修改字典类型
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
	@Override
	@Transactional
	public int updateDictType(SysDictTypeDTO dictType) {
		dictType.setUpdateTime(LocalDateTime.now());
		SysDictTypeDTO oldDictType = dictTypeMapper.selectDictTypeById(dictType.getDictId());
		if (!dictType.getDictType().equals(oldDictType.getDictType())){
			dictDataMapper.updateDictType(dictType, oldDictType.getDictType());
		}
		return dictTypeMapper.updateDictType(dictType);
	}

	/**
     * 删除字典类型对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteDictTypeByIds(Long[] ids) {
		for (Long dictId : ids) {
			SysDictTypeDTO dictType = getDictTypeById(dictId);
			if (dictDataMapper.countDictDataByType(dictType.getDictType()) > 0){
				throw new BusinessException(JeeStringUtils.format("{}已分配,不能删除", dictType.getDictName()));
			}
		}
		return dictTypeMapper.deleteDictTypeByIds(ids);
	}

	/**
	 * 查询所有字典类型
	 *
	 * @return 字典类型集合信息
	 */
	@Override
	public List<SysDictTypeDTO> listDictTypeAll() {
		return dictTypeMapper.selectDictTypeAll();
	}

	/**
	 * 根据字典类型查询信息
	 *
	 * @param dictType 字典类型
	 * @return 字典类型
	 */
	@Override
	public SysDictTypeDTO getDictTypeByType(String dictType) {
		return dictTypeMapper.selectDictTypeByType(dictType);
	}

	/**
	 * 校验字典类型称是否唯一
	 *
	 * @param dictType 字典类型
	 * @return 结果
	 */
	@Override
	public boolean checkDictTypeUnique(SysDictTypeDTO dictType) {
		Long dictId = JeeStringUtils.isNull(dictType.getDictId()) ? -1L : dictType.getDictId();
		SysDictTypeDTO info = dictTypeMapper.selectDictTypeByType(dictType.getDictType());
		if (JeeStringUtils.isNotNull(info) && info.getDictId().longValue() != dictId.longValue()){
			return false;
		}
		return true;
	}

}
