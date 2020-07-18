package com.jee.boot.system.mapper;

import com.jee.boot.system.dto.SysDictDataDTO;
import com.jee.boot.system.dto.SysDictTypeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典数据 数据层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysDictDataMapper {
	/**
     * 查询字典数据信息
     * 
     * @param dictCode 字典数据ID
     * @return 字典数据信息
     */
	SysDictDataDTO selectDictDataById(Long dictCode);
	
	/**
     * 查询字典数据列表
     * 
     * @param dictData 字典数据信息
     * @return 字典数据集合
     */
	List<SysDictDataDTO> selectDictDataList(SysDictDataDTO dictData);
	
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
     * 删除字典数据
     * 
     * @param dictCode 字典数据ID
     * @return 结果
     */
	int deleteDictDataById(Long dictCode);
	
	/**
     * 批量删除字典数据
     * 
     * @param dictCodes 需要删除的数据ID
     * @return 结果
     */
	int deleteDictDataByIds(Long[] dictCodes);

	/**
	 * 根据字典类型查询字典数据
	 *
	 * @param dictType 字典类型
	 * @return 字典数据集合信息
	 */
	List<SysDictDataDTO> selectDictDataByType(String dictType);

	/**
	 * 根据字典类型和字典键值查询字典数据信息
	 *
	 * @param dictType 字典类型
	 * @param dictValue 字典键值
	 * @return 字典标签
	 */
    String selectDictLabel(String dictType, String dictValue);

	/**
	 * 查询字典数据
	 *
	 * @param dictType 字典类型
	 * @return 字典数据
	 */
    int countDictDataByType(String dictType);

	/**
	 * 同步修改字典类型
	 *
	 * @param dictType 旧字典类型
	 * @param type 新旧字典类型
	 * @return 结果
	 */
	int updateDictType(@Param("dictType") SysDictTypeDTO dictType, @Param("oldType") String type);


}