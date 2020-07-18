package com.jee.boot.admin.controller.system;

import java.util.List;
import com.jee.boot.common.core.web.BaseController;
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
import com.jee.boot.system.dto.SysNoticeDTO;
import com.jee.boot.system.vo.SysNoticeVO;
import com.jee.boot.system.service.ISysNoticeService;
import com.jee.boot.shiro.utils.ShiroUtils;



/**
 * 通知公告 控制层
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {
    private String prefix = "system/notice";
	
	@Autowired
	private ISysNoticeService noticeService;
	
	@RequiresPermissions("system:notice:view")
	@GetMapping()
	public String notice() {
	    return prefix + "/notice";
	}
	
	/**
	 * 查询通知公告列表
	 */
	@RequiresPermissions("system:notice:list")
	@PostMapping("/list")
	@ResponseBody
	public R list(SysNoticeDTO dto) {
		return PageUtil.page(ServletUtils.getRequest(), () -> {
			List<SysNoticeDTO> dtoList = noticeService.listNotice(dto);
			return JeeBeanUtils.mapList(dtoList, SysNoticeDTO.class, SysNoticeVO.class);
		});
	}
	
	/**
	 * 新增通知公告
	 */
	@GetMapping("/add")
	public String add() {
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存通知公告
	 */
	@RequiresPermissions("system:notice:add")
	@Log(title = "通知公告", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public R addSave(@Validated SysNoticeDTO dto) {
		dto.setCreateBy(ShiroUtils.getLoginName());
		int row = noticeService.insertNotice(dto);
		return res(row);
	}

	/**
	 * 修改通知公告
	 */
	@GetMapping("/edit/{noticeId}")
	public String edit(@PathVariable("noticeId") Integer noticeId, ModelMap mmap) {
		SysNoticeDTO notice = noticeService.getNoticeById(noticeId);
		mmap.put("notice", notice);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存通知公告
	 */
	@RequiresPermissions("system:notice:edit")
	@Log(title = "通知公告", businessType = BusinessType.UPDATE)
	@PutMapping("/edit")
	@ResponseBody
	public R editSave(@Validated SysNoticeDTO dto) {
		dto.setUpdateBy(ShiroUtils.getLoginName());
		int row = noticeService.updateNotice(dto);
		return res(row);
	}
	
	/**
	 * 删除通知公告
	 */
	@RequiresPermissions("system:notice:remove")
	@Log(title = "通知公告", businessType = BusinessType.DELETE)
	@DeleteMapping( "/remove")
	@ResponseBody
	public R remove(String ids) {
		int row = noticeService.deleteNoticeByIds(Convert.toLongArray(ids));
		return res(row);
	}
}
