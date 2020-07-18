package com.jee.boot.admin.controller.system;

import java.util.List;

import com.jee.boot.common.utils.text.Convert;
import com.jee.boot.framework.cacheservice.ICacheDictService;
import com.jee.boot.shiro.utils.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.jee.boot.common.annotation.Log;
import com.jee.boot.common.enums.BusinessType;
import com.jee.boot.common.core.page.PageUtil;
import com.jee.boot.common.core.result.R;
import com.jee.boot.common.utils.bean.JeeBeanUtils;
import com.jee.boot.common.utils.spring.ServletUtils;
import com.jee.boot.system.dto.SysDictTypeDTO;
import com.jee.boot.system.vo.SysDictTypeVO;
import com.jee.boot.system.service.ISysDictTypeService;
import com.jee.boot.common.core.web.BaseController;


/**
 * 字典类型 控制层
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/system/dict")
public class SysDictTypeController extends BaseController {
    private String prefix = "system/dict/type";
	
	@Autowired
	private ISysDictTypeService dictTypeService;
	@Autowired
	private ICacheDictService cacheDictService;
	
	@RequiresPermissions("system:dict:view")
	@GetMapping()
	public String dictType() {
	    return prefix + "/type";
	}
	
	/**
	 * 查询字典类型列表
	 */
	@RequiresPermissions("system:dict:list")
	@PostMapping("/list")
	@ResponseBody
	public R list(SysDictTypeDTO dto) {
		return PageUtil.page(ServletUtils.getRequest(), () -> {
			List<SysDictTypeDTO> dtoList = dictTypeService.listDictType(dto);
			return JeeBeanUtils.mapList(dtoList, SysDictTypeDTO.class, SysDictTypeVO.class);
		});
	}
	
	/**
	 * 新增字典类型
	 */
	@GetMapping("/add")
	public String add() {
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存字典类型
	 */
	@RequiresPermissions("system:dict:add")
	@Log(title = "字典类型", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public R addSave(SysDictTypeDTO dto) {
		if (!dictTypeService.checkDictTypeUnique(dto)){
			return R.error().msg("新增字典'" + dto.getDictName() + "'失败，字典类型已存在");
		}
		dto.setCreateBy(ShiroUtils.getLoginName());
		int row = cacheDictService.insertDictType(dto);
		return res(row);
	}

	/**
	 * 修改字典类型
	 */
	@GetMapping("/edit/{dictId}")
	public String edit(@PathVariable("dictId") Long dictId, ModelMap mmap) {
		SysDictTypeDTO dictType = dictTypeService.getDictTypeById(dictId);
		mmap.put("dict", dictType);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存字典类型
	 */
	@RequiresPermissions("system:dict:edit")
	@Log(title = "字典类型", businessType = BusinessType.UPDATE)
	@PutMapping("/edit")
	@ResponseBody
	public R editSave(SysDictTypeDTO dto) {
		if (!dictTypeService.checkDictTypeUnique(dto)){
			return R.error().msg("修改字典'" + dto.getDictName() + "'失败，字典类型已存在");
		}
		dto.setUpdateBy(ShiroUtils.getLoginName());
		int row = cacheDictService.updateDictType(dto);
		return res(row);
	}
	
	/**
	 * 删除字典类型
	 */
	@RequiresPermissions("system:dict:remove")
	@Log(title = "字典类型", businessType = BusinessType.DELETE)
	@DeleteMapping( "/remove")
	@ResponseBody
	public R remove(String ids) {
		int row = cacheDictService.deleteDictTypeByIds(Convert.toLongArray(ids));
		return res(row);
	}

	/**
	 * 清空缓存
	 */
	@RequiresPermissions("system:dict:remove")
	@Log(title = "字典类型", businessType = BusinessType.CLEAN)
	@DeleteMapping("/clearCache")
	@ResponseBody
	public R clearCache(){
		cacheDictService.clearCache();
		return R.ok();
	}

	/**
	 * 校验字典类型
	 */
	@PostMapping("/checkDictTypeUnique")
	@ResponseBody
	public boolean checkDictTypeUnique(SysDictTypeDTO dto) {
		return dictTypeService.checkDictTypeUnique(dto);
	}

	/**
	 * 查询字典详细
	 */
	@RequiresPermissions("system:dict:list")
	@GetMapping("/detail/{dictId}")
	public String detail(@PathVariable("dictId") Long dictId, ModelMap mmap) {
		mmap.put("dict", dictTypeService.getDictTypeById(dictId));
		mmap.put("dictList", dictTypeService.listDictTypeAll());
		return "system/dict/data/data";
	}
}
