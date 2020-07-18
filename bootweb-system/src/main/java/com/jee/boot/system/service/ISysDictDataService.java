package com.jee.boot.system.service;

import com.jee.boot.system.dto.SysDictDataDTO;
import java.util.List;

/**
 * 字典数据 服务层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface ISysDictDataService {
	/**
     * 查询字典数据信息
     * 
     * @param dictCode 字典数据ID
     * @return 字典数据信息
     */
	SysDictDataDTO getDictDataById(Long dictCode);
	
	/**
     * 查询字典数据列表
     * 
     * @param dictData 字典数据信息
     * @return 字典数据集合
     */
	List<SysDictDataDTO> listDictData(SysDictDataDTO dictData);
	
	/**
     * 新增字典数据
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
	int insertDictData(SysDictDataDTO dictData);
	
	/**
     * 修改字典数据
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
	int updateDictData(SysDictDataDTO dictData);
		
	/**
     * 删除字典数据信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteDictDataByIds(Long[] ids);

	/**
	 * 根据字典类型查询字典数据
	 *
	 * @param dictType 字典类型
	 * @return 字典数据集合信息
	 */
	List<SysDictDataDTO> listDictDataByType(String dictType);

	/**
	 * 根据字典类型和字典键值查询字典数据信息
	 *
	 * @param dictType 字典类型
	 * @param dictValue 字典键值
	 * @return 字典标签
	 */
	String getDictLabel(String dictType, String dictValue);


}
