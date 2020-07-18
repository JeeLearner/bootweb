package com.jee.boot.admin.controller.system;

import java.util.List;

import com.jee.boot.admin.controller.system.excelmodel.SysLogOperModel;
import com.jee.boot.common.config.BWProp;
import com.jee.boot.common.core.web.BaseController;
import com.jee.boot.common.poi.ExcelUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.jee.boot.common.annotation.Log;
import com.jee.boot.common.utils.text.Convert;
import com.jee.boot.common.enums.BusinessType;
import com.jee.boot.common.core.page.PageUtil;
import com.jee.boot.common.core.result.R;
import com.jee.boot.common.utils.bean.JeeBeanUtils;
import com.jee.boot.common.utils.spring.ServletUtils;
import com.jee.boot.system.dto.SysLogOperDTO;
import com.jee.boot.system.vo.SysLogOperVO;
import com.jee.boot.system.service.ISysLogOperService;
import com.jee.boot.shiro.utils.ShiroUtils;



/**
 * 操作日志记录 控制层
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/system/logoper")
public class SysLogOperController extends BaseController {
    private String prefix = "system/logoper";
	
	@Autowired
	private ISysLogOperService logOperService;
	
	@RequiresPermissions("system:logoper:view")
	@GetMapping()
	public String logOper() {
	    return prefix + "/logoper";
	}
	
	/**
	 * 查询操作日志记录列表
	 */
	@RequiresPermissions("system:logoper:list")
	@PostMapping("/list")
	@ResponseBody
	public R list(SysLogOperDTO dto) {
		return PageUtil.page(ServletUtils.getRequest(), () -> {
			List<SysLogOperDTO> dtoList = logOperService.listLogOper(dto);
			return JeeBeanUtils.mapList(dtoList, SysLogOperDTO.class, SysLogOperVO.class);
		});
	}

	/**
	 * 用户登录日志导出
	 * @param log
	 * @return
	 */
	@Log(title = "操作日志记录", businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:logoper:export")
	@PostMapping("/export")
	@ResponseBody
	public R export(SysLogOperDTO log){
		List<SysLogOperDTO> dtoList = logOperService.listLogOper(log);
		List<SysLogOperModel> list = JeeBeanUtils.mapList(dtoList, SysLogOperDTO.class, SysLogOperModel.class);
		ExcelUtil<SysLogOperModel> util = new ExcelUtil<SysLogOperModel>(SysLogOperModel.class);
		return util.exportExcel(list, "用户操作日志", BWProp.getDownloadPath());
	}

	/**
	 * 删除操作日志记录
	 */
	@RequiresPermissions("system:logoper:remove")
	@Log(title = "操作日志记录", businessType = BusinessType.DELETE)
	@DeleteMapping( "/remove")
	@ResponseBody
	public R remove(String ids) {
		int row = logOperService.deleteLogOperByIds(Convert.toLongArray(ids));
		return res(row);
	}

	/**
	 * 操作日志详情
	 * @param operId
	 * @param mmap
	 * @return
	 */
	@RequiresPermissions("monitor:logoper:detail")
	@GetMapping("/detail/{operId}")
	public String detail(@PathVariable("operId") Long operId, ModelMap mmap){
		mmap.put("operLog", logOperService.getLogOperById(operId));
		return prefix + "/detail";
	}

	@Log(title = "操作日志", businessType = BusinessType.CLEAN)
	@RequiresPermissions("monitor:logoper:remove")
	@DeleteMapping("/clear")
	@ResponseBody
	public R clear(){
		logOperService.deleteOperLogAll();
		return R.ok();
	}
}
