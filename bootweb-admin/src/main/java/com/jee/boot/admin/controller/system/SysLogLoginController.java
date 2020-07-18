package com.jee.boot.admin.controller.system;

import java.util.List;

import com.jee.boot.admin.controller.system.excelmodel.SysLogLoginModel;
import com.jee.boot.common.config.BWProp;
import com.jee.boot.common.core.web.BaseController;
import com.jee.boot.common.poi.ExcelUtil;
import com.jee.boot.shiro.service.SysPasswordService;
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
import com.jee.boot.system.dto.SysLogLoginDTO;
import com.jee.boot.system.vo.SysLogLoginVO;
import com.jee.boot.system.service.ISysLogLoginService;
import com.jee.boot.shiro.utils.ShiroUtils;



/**
 * 系统访问记录 控制层
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/system/loglogin")
public class SysLogLoginController extends BaseController {
    private String prefix = "system/loglogin";
	
	@Autowired
	private ISysLogLoginService logLoginService;
	@Autowired
	private SysPasswordService passwordService;
	
	@RequiresPermissions("system:loglogin:view")
	@GetMapping()
	public String logLogin() {
	    return prefix + "/loglogin";
	}
	
	/**
	 * 查询系统访问记录列表
	 */
	@RequiresPermissions("system:loglogin:list")
	@PostMapping("/list")
	@ResponseBody
	public R list(SysLogLoginDTO dto) {
		return PageUtil.page(ServletUtils.getRequest(), () -> {
			List<SysLogLoginDTO> dtoList = logLoginService.listLogLogin(dto);
			return JeeBeanUtils.mapList(dtoList, SysLogLoginDTO.class, SysLogLoginVO.class);
		});
	}

	/**
	 * 用户登录日志导出
	 * @param log
	 * @return
	 */
	@Log(title = "系统访问记录", businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:loglogin:export")
	@PostMapping("/export")
	@ResponseBody
	public R export(SysLogLoginDTO log){
		List<SysLogLoginDTO> dtoList = logLoginService.listLogLogin(log);
		List<SysLogLoginModel> list = JeeBeanUtils.mapList(dtoList, SysLogLoginDTO.class, SysLogLoginModel.class);
		ExcelUtil<SysLogLoginModel> util = new ExcelUtil<SysLogLoginModel>(SysLogLoginModel.class);
		return util.exportExcel(list, "用户登录日志", BWProp.getDownloadPath());
	}
	
	/**
	 * 删除系统访问记录
	 */
	@RequiresPermissions("system:loglogin:remove")
	@Log(title = "系统访问记录", businessType = BusinessType.DELETE)
	@DeleteMapping( "/remove")
	@ResponseBody
	public R remove(String ids) {
		int row = logLoginService.deleteLogLoginByIds(Convert.toLongArray(ids));
		return res(row);
	}

	@RequiresPermissions("monitor:loglogin:remove")
	@Log(title = "登陆日志", businessType = BusinessType.CLEAN)
	@DeleteMapping("/clear")
	@ResponseBody
	public R clear(){
		logLoginService.deleteLogLoginAll();
		return R.ok();
	}

	/**
	 * 账户登录锁定后，可以解开
	 * @param loginName
	 * @return
	 */
	@RequiresPermissions("monitor:loglogin:unlock")
	@Log(title = "账户解锁", businessType = BusinessType.OTHER)
	@PostMapping("/unlock")
	@ResponseBody
	public R unlock(String loginName){
		passwordService.unlock(loginName);
		return R.ok();
	}
}
