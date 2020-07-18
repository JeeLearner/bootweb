package com.jee.boot.system.service;

import com.jee.boot.system.dto.SysDictTypeDTO;
import java.util.List;

/**
 * 字典类型 服务层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface ISysDictTypeService {
	/**
     * 查询字典类型信息
     * 
     * @param dictId 字典类型ID
     * @return 字典类型信息
     */
	SysDictTypeDTO getDictTypeById(Long dictId);
	
	/**
     * 查询字典类型列表
     * 
     * @param dictType 字典类型信息
     * @return 字典类型集合
     */
	List<SysDictTypeDTO> listDictType(SysDictTypeDTO dictType);
	
	/**
     * 新增字典类型
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
	int insertDictType(SysDictTypeDTO dictType);
	
	/**
     * 修改字典类型
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
	int updateDictType(SysDictTypeDTO dictType);
		
	/**
     * 删除字典类型信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteDictTypeByIds(Long[] ids);

	/**
	 * 查询所有字典类型
	 *
	 * @return 字典类型集合信息
	 */
	List<SysDictTypeDTO> listDictTypeAll();
	/**
	 * 根据字典类型查询信息
	 *
	 * @param dictType 字典类型
	 * @return 字典类型
	 */
	SysDictTypeDTO getDictTypeByType(String dictType);

	/**
	 * 校验字典类型称是否唯一
	 *
	 * @param dictType 字典类型
	 * @return 结果
	 */
	boolean checkDictTypeUnique(SysDictTypeDTO dictType);
}
