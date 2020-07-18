package com.jee.boot.system.mapper;

import com.jee.boot.system.dto.SysDictTypeDTO;
import com.jee.boot.system.domain.SysDictType;
import java.util.List;	

/**
 * 字典类型 数据层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysDictTypeMapper {
	/**
     * 查询字典类型信息
     * 
     * @param dictId 字典类型ID
     * @return 字典类型信息
     */
	SysDictTypeDTO selectDictTypeById(Long dictId);
	
	/**
     * 查询字典类型列表
     * 
     * @param dictType 字典类型
     * @return 字典类型集合
     */
	List<SysDictTypeDTO> selectDictTypeList(SysDictType dictType);
	
	/**
     * 新增字典类型
     * 
     * @param dictType 字典类型
     * @return 结果
     */
	int insertDictType(SysDictType dictType);
	
	/**
     * 修改字典类型
     * 
     * @param dictType 字典类型
     * @return 结果
     */
	int updateDictType(SysDictType dictType);
	
	/**
     * 删除字典类型
     * 
     * @param dictId 字典类型ID
     * @return 结果
     */
	int deleteDictTypeById(Long dictId);
	
	/**
     * 批量删除字典类型
     * 
     * @param dictIds 需要删除的数据ID
     * @return 结果
     */
	int deleteDictTypeByIds(Long[] dictIds);

	/**
	 * 查询所有字典类型
	 *
	 * @return 字典类型集合信息
	 */
    List<SysDictTypeDTO> selectDictTypeAll();

	/**
	 * 根据字典类型查询信息
	 *
	 * @param dictType 字典类型
	 * @return 字典类型
	 */
	SysDictTypeDTO selectDictTypeByType(String dictType);


}