package com.jee.boot.admin.controller.monitor;

import java.util.List;
import com.jee.boot.common.core.web.BaseController;
import com.jee.boot.common.enums.OnlineStatus;
import com.jee.boot.shiro.session.OnlineSession;
import com.jee.boot.shiro.session.OnlineSessionDAO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
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
import com.jee.boot.system.dto.SysUserOnlineDTO;
import com.jee.boot.system.vo.SysUserOnlineVO;
import com.jee.boot.system.service.ISysUserOnlineService;
import com.jee.boot.shiro.utils.ShiroUtils;



/**
 * 在线用户记录 控制层
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {
    private String prefix = "monitor/online";
	
	@Autowired
	private ISysUserOnlineService userOnlineService;
	@Autowired
	private OnlineSessionDAO onlineSessionDAO;
	
	@RequiresPermissions("monitor:online:view")
	@GetMapping()
	public String userOnline() {
	    return prefix + "/online";
	}
	
	/**
	 * 查询在线用户记录列表
	 */
	@RequiresPermissions("monitor:online:list")
	@PostMapping("/list")
	@ResponseBody
	public R list(SysUserOnlineDTO dto) {
		return PageUtil.page(ServletUtils.getRequest(), () -> {
			List<SysUserOnlineDTO> dtoList = userOnlineService.listUserOnline(dto);
			return JeeBeanUtils.mapList(dtoList, SysUserOnlineDTO.class, SysUserOnlineVO.class);
		});
	}

	@RequiresPermissions("monitor:online:forceLogout")
	@Log(title = "在线用户", businessType = BusinessType.FORCE)
	@PostMapping("/forceLogout")
	@ResponseBody
	public R forceLogout(String sessionId){
		SysUserOnlineDTO online = userOnlineService.getUserOnlineById(sessionId);
		if (sessionId.equals(ShiroUtils.getSessionId())){
			return R.error().msg("当前登陆用户无法强退");
		}
		if (online == null){
			return R.error().msg("用户已下线");
		}
		OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
		if (onlineSession == null) {
			return R.error().msg("用户已下线");
		}
		onlineSession.setStatus(OnlineStatus.off_line);
		onlineSessionDAO.update(onlineSession);
		online.setStatus(OnlineStatus.off_line);
		userOnlineService.insertUserOnline(online);
		return R.ok();
	}

	/**
	 * 批量强退，
	 * 		需要设置@RequestParam("ids[]")
	 * @param ids
	 * @return
	 */
	@RequiresPermissions("monitor:online:batchForceLogout")
	@Log(title = "在线用户", businessType = BusinessType.FORCE)
	@PostMapping("/batchForceLogout")
	@ResponseBody
	public R batchForceLogout(@RequestParam("ids[]") String[] ids) {
		for (String sessionId : ids) {
			SysUserOnlineDTO online = userOnlineService.getUserOnlineById(sessionId);
			if (sessionId.equals(ShiroUtils.getSessionId())) {
				return R.error().msg("当前登陆用户无法强退");
			}
			if (online == null) {
				return R.error().msg("用户已下线");
			}
			OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
			if (onlineSession == null) {
				return R.error().msg("用户已下线");
			}
			onlineSession.setStatus(OnlineStatus.off_line);
			onlineSessionDAO.update(onlineSession);
			online.setStatus(OnlineStatus.off_line);
			userOnlineService.insertUserOnline(online);
		}
		return R.ok();
	}
}
