package com.jee.boot.admin.controller.monitor;

import java.util.List;

import com.jee.boot.admin.controller.monitor.excelmodel.SysJobLogModel;
import com.jee.boot.common.config.BWProp;
import com.jee.boot.common.core.web.BaseController;
import com.jee.boot.common.poi.ExcelUtil;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.monitor.dto.SysJobDTO;
import com.jee.boot.monitor.service.ISysJobService;
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
import com.jee.boot.monitor.dto.SysJobLogDTO;
import com.jee.boot.monitor.vo.SysJobLogVO;
import com.jee.boot.monitor.service.ISysJobLogService;
import com.jee.boot.shiro.utils.ShiroUtils;



/**
 * 定时任务调度日志 控制层
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/monitor/jobLog")
public class SysJobLogController extends BaseController {
    private String prefix = "monitor/job";
	
	@Autowired
	private ISysJobLogService jobLogService;
	@Autowired
	private ISysJobService jobService;
	
	@RequiresPermissions("monitor:job:view")
	@GetMapping()
	public String jobLog(@RequestParam(value = "jobId", required = false) Long jobId, ModelMap mmap) {
		if (JeeStringUtils.isNotNull(jobId)){
			SysJobDTO job = jobService.getJobById(jobId);
			mmap.put("job", job);
		}
	    return prefix + "/jobLog";
	}
	
	/**
	 * 查询定时任务调度日志列表
	 */
	@RequiresPermissions("monitor:job:list")
	@PostMapping("/list")
	@ResponseBody
	public R list(SysJobLogDTO dto) {
		return PageUtil.page(ServletUtils.getRequest(), () -> {
			List<SysJobLogDTO> dtoList = jobLogService.listJobLog(dto);
			return JeeBeanUtils.mapList(dtoList, SysJobLogDTO.class, SysJobLogVO.class);
		});
	}

	/**
	 * 定时任务调度日志导出
	 * @param jobLog
	 * @return
	 */
	@Log(title = "定时任务调度日志", businessType = BusinessType.EXPORT)
	@RequiresPermissions("monitor:job:export")
	@PostMapping("/export")
	@ResponseBody
	public R export(SysJobLogDTO jobLog){
		List<SysJobLogDTO> dtoList = jobLogService.listJobLog(jobLog);
		List<SysJobLogModel> list = JeeBeanUtils.mapList(dtoList, SysJobLogDTO.class, SysJobLogModel.class);
		ExcelUtil<SysJobLogModel> util = new ExcelUtil<SysJobLogModel>(SysJobLogModel.class);
		return util.exportExcel(list, "定时任务调度日志", BWProp.getDownloadPath());
	}

	/**
	 * 删除定时任务调度日志
	 */
	@RequiresPermissions("monitor:job:remove")
	@Log(title = "定时任务调度日志", businessType = BusinessType.DELETE)
	@DeleteMapping( "/remove")
	@ResponseBody
	public R remove(String ids) {
		int row = jobLogService.deleteJobLogByIds(Convert.toLongArray(ids));
		return res(row);
	}

	/**
	 * 查询任务日志详情
	 * @param jobLogId
	 * @param mmap
	 * @return
	 */
	@RequiresPermissions("monitor:job:detail")
	@GetMapping("/detail/{jobLogId}")
	public String detail(@PathVariable("jobLogId") Long jobLogId, ModelMap mmap) {
		mmap.put("name", "jobLog");
		mmap.put("jobLog", jobLogService.getJobLogById(jobLogId));
		return prefix + "/detail";
	}

	/**
	 * 清空调度日志
	 * @return
	 */
	@Log(title = "调度日志", businessType = BusinessType.CLEAN)
	@RequiresPermissions("monitor:job:remove")
	@DeleteMapping("/clear")
	@ResponseBody
	public R clear() {
		jobLogService.deleteJobLogAll();
		return R.ok();
	}
}
