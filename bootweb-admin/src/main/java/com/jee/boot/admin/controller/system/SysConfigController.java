package com.jee.boot.admin.controller.system;

import java.util.List;

import com.jee.boot.common.core.web.BaseController;
import com.jee.boot.common.utils.bean.JeeBeanUtils;
import com.jee.boot.common.utils.text.Convert;
import com.jee.boot.framework.cacheservice.ICacheConfigService;
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
import com.jee.boot.common.utils.spring.ServletUtils;
import com.jee.boot.system.dto.SysConfigDTO;
import com.jee.boot.system.vo.SysConfigVO;
import com.jee.boot.system.service.ISysConfigService;


/**
 * 参数配置 控制层
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {
    private String prefix = "system/config";
	
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private ICacheConfigService cacheConfigService;



	@RequiresPermissions("system:config:view")
	@GetMapping()
	public String config() {
		return prefix + "/config";
	}
	
	/**
	 * 查询参数配置列表
	 */
	@RequiresPermissions("system:config:list")
	@PostMapping("/list")
	@ResponseBody
	public R list(SysConfigDTO dto) {
		return PageUtil.page(ServletUtils.getRequest(), () -> {
			List<SysConfigDTO> dtoList = configService.listConfig(dto);
			return JeeBeanUtils.mapList(dtoList, SysConfigDTO.class, SysConfigVO.class);
		});
	}
	
	/**
	 * 新增参数配置
	 */
	@GetMapping("/add")
	public String add() {
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存参数配置
	 */
	@RequiresPermissions("system:config:add")
	@Log(title = "参数配置", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public R addSave(@Validated SysConfigDTO dto) {
		dto.setCreateBy(ShiroUtils.getLoginName());
		int row = cacheConfigService.insertConfig(dto);
		return res(row);
	}

	/**
	 * 修改参数配置
	 */
	@GetMapping("/edit/{configId}")
	public String edit(@PathVariable("configId") Long configId, ModelMap mmap) {
		SysConfigDTO config = configService.getConfigById(configId);
		mmap.put("config", config);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存参数配置
	 */
	@RequiresPermissions("system:config:edit")
	@Log(title = "参数配置", businessType = BusinessType.UPDATE)
	@PutMapping("/edit")
	@ResponseBody
	public R editSave(@Validated SysConfigDTO dto) {
		dto.setUpdateBy(ShiroUtils.getLoginName());
		int row = cacheConfigService.updateConfig(dto);
		return res(row);
	}
	
	/**
	 * 删除参数配置
	 */
	@RequiresPermissions("system:config:remove")
	@Log(title = "参数配置", businessType = BusinessType.DELETE)
	@DeleteMapping( "/remove")
	@ResponseBody
	public R remove(String ids) {
		int row = cacheConfigService.deleteConfigByIds(Convert.toLongArray(ids));
		return res(row);
	}

	/**
	 * 清空缓存
	 */
	@RequiresPermissions("system:config:remove")
	@Log(title = "参数管理", businessType = BusinessType.CLEAN)
	@DeleteMapping("/clearCache")
	@ResponseBody
	public R clearCache(){
		cacheConfigService.clearCache();
		return R.ok();
	}

	/**
	 * 校验参数键名
	 */
	@PostMapping("/checkConfigKeyUnique")
	@ResponseBody
	public boolean checkConfigKeyUnique(SysConfigDTO dto){
		return configService.checkConfigKeyUnique(dto);
	}
}
