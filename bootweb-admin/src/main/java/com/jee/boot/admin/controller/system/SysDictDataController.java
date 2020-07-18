package com.jee.boot.admin.controller.system;

import java.util.List;

import com.jee.boot.common.utils.text.Convert;
import com.jee.boot.framework.cacheservice.ICacheDictService;
import com.jee.boot.shiro.utils.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.jee.boot.common.annotation.Log;
import com.jee.boot.common.enums.BusinessType;
import com.jee.boot.common.core.page.PageUtil;
import com.jee.boot.common.core.result.R;
import com.jee.boot.common.utils.bean.JeeBeanUtils;
import com.jee.boot.common.utils.spring.ServletUtils;
import com.jee.boot.system.domain.SysDictData;
import com.jee.boot.system.dto.SysDictDataDTO;
import com.jee.boot.system.vo.SysDictDataVO;
import com.jee.boot.system.service.ISysDictDataService;
import com.jee.boot.common.core.web.BaseController;


/**
 * 字典数据 控制层
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {
    private String prefix = "system/dict/data";
	
	@Autowired
	private ISysDictDataService dictDataService;
	@Autowired
	private ICacheDictService cacheDictService;
	
	@RequiresPermissions("system:dict:view")
	@GetMapping()
	public String dictData() {
	    return prefix + "/data";
	}
	
	/**
	 * 查询字典数据列表
	 */
	@RequiresPermissions("system:data:list")
	@PostMapping("/list")
	@ResponseBody
	public R list(SysDictDataDTO dto) {
		return PageUtil.page(ServletUtils.getRequest(), () -> {
			List<SysDictDataDTO> dtoList = dictDataService.listDictData(dto);
			return JeeBeanUtils.mapList(dtoList, SysDictDataDTO.class, SysDictDataVO.class);
		});
	}
	
	/**
	 * 新增字典数据
	 */
	@GetMapping("/add/{dictType}")
	public String add(@PathVariable("dictType") String dictType, ModelMap mmap) {
		mmap.put("dictType", dictType);
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存字典数据
	 */
	@RequiresPermissions("system:data:add")
	@Log(title = "字典数据", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public R addSave(@Validated SysDictDataDTO dto) {
		dto.setCreateBy(ShiroUtils.getLoginName());
		int row = cacheDictService.insertDictData(dto);
		return res(row);
	}

	/**
	 * 修改字典数据
	 */
	@GetMapping("/edit/{dictCode}")
	public String edit(@PathVariable("dictCode") Long dictCode, ModelMap mmap) {
		SysDictDataDTO dictData = dictDataService.getDictDataById(dictCode);
		mmap.put("dict", dictData);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存字典数据
	 */
	@RequiresPermissions("system:data:edit")
	@Log(title = "字典数据", businessType = BusinessType.UPDATE)
	@PutMapping("/edit")
	@ResponseBody
	public R editSave(SysDictDataDTO dto) {
		dto.setUpdateBy(ShiroUtils.getLoginName());
		int row = cacheDictService.updateDictData(dto);
		return res(row);
	}
	
	/**
	 * 删除字典数据
	 */
	@RequiresPermissions("system:data:remove")
	@Log(title = "字典数据", businessType = BusinessType.DELETE)
	@DeleteMapping( "/remove")
	@ResponseBody
	public R remove(String ids) {
		int row = cacheDictService.deleteDictDataByIds(Convert.toLongArray(ids));
		return res(row);
	}
}
