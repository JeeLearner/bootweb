package com.jee.boot.admin.controller.monitor;

import java.util.List;

import com.jee.boot.admin.controller.monitor.excelmodel.SysJobLogModel;
import com.jee.boot.admin.controller.monitor.excelmodel.SysJobModel;
import com.jee.boot.common.annotation.RepeatSubmit;
import com.jee.boot.common.config.BWProp;
import com.jee.boot.common.core.web.BaseController;
import com.jee.boot.common.poi.ExcelUtil;
import org.apache.ibatis.annotations.Delete;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.SchedulerException;
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
import com.jee.boot.monitor.dto.SysJobDTO;
import com.jee.boot.monitor.vo.SysJobVO;
import com.jee.boot.monitor.service.ISysJobService;
import com.jee.boot.shiro.utils.ShiroUtils;

/**
 * 定时任务调度 控制层
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/monitor/job")
public class SysJobController extends BaseController {
    private String prefix = "monitor/job";
	
	@Autowired
	private ISysJobService jobService;

	@RequiresPermissions("monitor:job:view")
	@GetMapping()
	public String job() {
		return prefix + "/job";
	}
	
	/**
	 * 查询定时任务调度列表
	 */
	@RequiresPermissions("monitor:job:list")
	@PostMapping("/list")
	@ResponseBody
	public R list(SysJobDTO dto) {
		return PageUtil.page(ServletUtils.getRequest(), () -> {
			List<SysJobDTO> dtoList = jobService.listJob(dto);
			return JeeBeanUtils.mapList(dtoList, SysJobDTO.class, SysJobVO.class);
		});
	}

	/**
	 * 定时任务日志导出
	 * @param job
	 * @return
	 */
	@Log(title = "定时任务", businessType = BusinessType.EXPORT)
	@RequiresPermissions("monitor:job:export")
	@PostMapping("/export")
	@ResponseBody
	@RepeatSubmit
	public R export(SysJobDTO job){
		List<SysJobDTO> dtoList = jobService.listJob(job);
		List<SysJobModel> list = JeeBeanUtils.mapList(dtoList, SysJobDTO.class, SysJobModel.class);
		ExcelUtil<SysJobModel> util = new ExcelUtil<SysJobModel>(SysJobModel.class);
		return util.exportExcel(list, "定时任务", BWProp.getDownloadPath());
	}
	
	/**
	 * 新增定时任务调度
	 */
	@GetMapping("/add")
	public String add() {
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存定时任务调度
	 */
	@RequiresPermissions("monitor:job:add")
	@Log(title = "定时任务", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public R addSave(@Validated SysJobDTO dto) {
		dto.setCreateBy(ShiroUtils.getLoginName());
		int row = jobService.insertJob(dto);
		return res(row);
	}

	/**
	 * 修改定时任务调度
	 */
	@GetMapping("/edit/{jobId}")
	public String edit(@PathVariable("jobId") Long jobId, ModelMap mmap) {
		SysJobDTO job = jobService.getJobById(jobId);
		mmap.put("job", job);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存定时任务调度
	 */
	@RequiresPermissions("monitor:job:edit")
	@Log(title = "定时任务", businessType = BusinessType.UPDATE)
	@PutMapping("/edit")
	@ResponseBody
	public R editSave(@Validated SysJobDTO dto) {
		dto.setUpdateBy(ShiroUtils.getLoginName());
		int row = jobService.updateJob(dto);
		return res(row);
	}
	
	/**
	 * 删除定时任务调度
	 */
	@RequiresPermissions("monitor:job:remove")
	@Log(title = "定时任务", businessType = BusinessType.DELETE)
	@DeleteMapping( "/remove")
	@ResponseBody
	public R remove(String ids) {
		int row = jobService.deleteJobByIds(Convert.toLongArray(ids));
		return res(row);
	}

	/**
	 * 修改状态
	 * @param job
	 * @return
	 */
	@RequiresPermissions("monitor:job:changeStatus")
	@Log(title = "定时任务", businessType = BusinessType.UPDATE)
	@PostMapping("/changeStatus")
	@ResponseBody
	public R changeStatus(SysJobDTO job) throws SchedulerException {
		SysJobDTO dbJob = jobService.getJobById(Convert.toLong(job.getJobId()));
		dbJob.setStatus(job.getStatus());
		int rows = jobService.updateStatus(dbJob);
		return res(rows);
	}

	/**
	 * 任务调度立即执行一次
	 */
	@RequiresPermissions("monitor:job:changeStatus")
	@Log(title = "定时任务", businessType = BusinessType.UPDATE)
	@PostMapping("/run")
	@ResponseBody
	public R run(SysJobDTO job) throws SchedulerException {
		jobService.run(job);
		return R.ok();
	}

	@RequiresPermissions("monitor:job:detail")
	@GetMapping("/detail/{jobId}")
	public String detail(@PathVariable("jobId") Long jobId, ModelMap mmap) {
		mmap.put("name", "job");
		mmap.put("job", jobService.getJobById(jobId));
		return prefix + "/detail";
	}

	/**
	 * 校验cron表达式是否有效
	 */
	@PostMapping("/checkCronExpressionIsValid")
	@ResponseBody
	public boolean checkCronExpressionIsValid(SysJobDTO job){
		return jobService.checkCronExpressionIsValid(job.getCronExpression());
	}
}
