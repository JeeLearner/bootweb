package com.jee.boot.admin.controller.system;

import java.util.List;

import com.jee.boot.admin.controller.system.excelmodel.SysUserModel;
import com.jee.boot.common.config.BWProp;
import com.jee.boot.common.core.web.BaseController;
import com.jee.boot.common.poi.ExcelUtil;
import com.jee.boot.shiro.service.SysPasswordService;
import com.jee.boot.system.domain.SysUserRole;
import com.jee.boot.system.service.ISysPostService;
import com.jee.boot.system.service.ISysRoleService;
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
import com.jee.boot.system.dto.SysUserDTO;
import com.jee.boot.system.vo.SysUserVO;
import com.jee.boot.system.service.ISysUserService;
import com.jee.boot.shiro.utils.ShiroUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 * 用户 控制层
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    private String prefix = "system/user";
	
	@Autowired
	private ISysUserService userService;

	@Autowired
	private ISysRoleService roleService;

	@Autowired
	private ISysPostService postService;

	@Autowired
	private SysPasswordService passwordService;


	@RequiresPermissions("system:user:view")
	@GetMapping()
	public String user() {
	    return prefix + "/user";
	}
	
	/**
	 * 查询用户列表
	 */
	@RequiresPermissions("system:user:list")
	@PostMapping("/list")
	@ResponseBody
	public R list(SysUserDTO dto) {
		return PageUtil.page(ServletUtils.getRequest(), () -> {
			List<SysUserDTO> dtoList = userService.listUser(dto);
			return JeeBeanUtils.mapList(dtoList, SysUserDTO.class, SysUserVO.class);
		});
	}

	/**
	 * 用户导出
	 * @param user
	 * @return
	 */
	@Log(title = "用户管理", businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:user:export")
	@PostMapping("/export")
	@ResponseBody
	public R export(SysUserDTO user){
		List<SysUserDTO> dtoList = userService.listUser(user);
		List<SysUserModel> list = JeeBeanUtils.mapList(dtoList, SysUserDTO.class, SysUserModel.class);
		ExcelUtil<SysUserModel> util = new ExcelUtil<SysUserModel>(SysUserModel.class);
		return util.exportExcel(list, "用户信息", BWProp.getDownloadPath());
	}

	/**
	 * 用户导入模板下载
	 * @return
	 */
	@RequiresPermissions("system:user:view")
	@GetMapping("/importTemplate")
	@ResponseBody
	public R importTemplate(){
		ExcelUtil<SysUserModel> util = new ExcelUtil<SysUserModel>(SysUserModel.class);
		return util.importTemplateExcel("用户数据", BWProp.getDownloadPath());
	}

	/**
	 * 用户信息导入
	 * 		注意：用户序号未使用
	 * @param file 文件
	 * @param updateSupport  是否更新已存在的数据
	 * @return 结果
	 * @throws Exception
	 */
	@Log(title = "用户管理", businessType = BusinessType.IMPORT)
	@RequiresPermissions("system:user:import")
	@PostMapping("/importData")
	@ResponseBody
	public R importData(MultipartFile file, boolean updateSupport) throws Exception {
		ExcelUtil<SysUserModel> util = new ExcelUtil<SysUserModel>(SysUserModel.class);
		List<SysUserModel> excelDataList = util.importExcel(file.getInputStream());
		List<SysUserDTO> userList = JeeBeanUtils.mapList(excelDataList, SysUserModel.class, SysUserDTO.class);

		String operName = ShiroUtils.getSysUser().getLoginName();
		String message = userService.importUser(userList, updateSupport, operName);
		return R.ok().data("info", message);
	}

	/**
	 * 新增用户
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap) {
		mmap.put("roles", roleService.listRoleAll());
		mmap.put("posts", postService.listPostAll());
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存用户
	 */
	@RequiresPermissions("system:user:add")
	@Log(title = "用户", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public R addSave(@Validated SysUserDTO user) {
		if (!userService.checkLoginNameUnique(user.getLoginName())){
			return R.error().msg("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
		}
		if (!userService.checkPhoneUnique(user)){
			return R.error().msg("新增用户'" + user.getLoginName() + "'失败，手机号码已存在");
		}
		if (!userService.checkEmailUnique(user)){
			return R.error().msg("新增用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
		}
        user.setSalt(ShiroUtils.randomSalt());
		user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCreateBy(ShiroUtils.getLoginName());
		int row = userService.insertUser(user);
		return res(row);
	}

	/**
	 * 修改用户
	 */
	@GetMapping("/edit/{userId}")
	public String edit(@PathVariable("userId") Long userId, ModelMap mmap) {
		mmap.put("user", userService.getUserById(userId));
		mmap.put("roles", roleService.listRolesByUserId(userId));
		mmap.put("posts", postService.listPostsByUserId(userId));
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存用户
	 */
	@RequiresPermissions("system:user:edit")
	@Log(title = "用户", businessType = BusinessType.UPDATE)
	@PutMapping("/edit")
	@ResponseBody
	public R editSave(@Validated SysUserDTO user) {
		userService.checkUserAllowed(user);
		if (!userService.checkPhoneUnique(user)){
			return R.error().msg("修改用户'" + user.getLoginName() + "'失败，手机号码已存在");
		}
		if (!userService.checkEmailUnique(user)){
			return R.error().msg("修改用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
		}
		user.setUpdateBy(ShiroUtils.getLoginName());
		int row = userService.updateUser(user);
		return res(row);
	}
	
	/**
	 * 删除用户
	 */
	@RequiresPermissions("system:user:remove")
	@Log(title = "用户", businessType = BusinessType.DELETE)
	@DeleteMapping( "/remove")
	@ResponseBody
	public R remove(String ids) {
		try {
			int row = userService.deleteUserByIds(Convert.toLongArray(ids));
			return res(row);
		} catch (Exception e){
			return R.error().msg(e.getMessage());
		}
	}

	/**
	 * 校验用户名
	 */
	@PostMapping("/checkLoginNameUnique")
	@ResponseBody
	public boolean checkLoginNameUnique(SysUserDTO user){
		return userService.checkLoginNameUnique(user.getLoginName());
	}

	/**
	 * 校验手机号码
	 */
	@PostMapping("/checkPhoneUnique")
	@ResponseBody
	public boolean checkPhoneUnique(SysUserDTO user){
		return userService.checkPhoneUnique(user);
	}

	/**
	 * 校验email邮箱
	 */
	@PostMapping("/checkEmailUnique")
	@ResponseBody
	public boolean checkEmailUnique(SysUserDTO user){
		return userService.checkEmailUnique(user);
	}

	/**
	 * 用户状态修改
	 */
	@Log(title = "用户管理", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:user:edit")
	@PostMapping("/changeStatus")
	@ResponseBody
	public R changeStatus(SysUserDTO user){
		userService.checkUserAllowed(user);
		user.setUpdateBy(ShiroUtils.getLoginName());
		int row = userService.updateUser(user);
		return res(row);
	}

	//===================重置密码=======================
	@RequiresPermissions("system:user:resetPwd")
	@Log(title = "重置密码", businessType = BusinessType.UPDATE)
	@GetMapping("/resetPwd/{userId}")
	public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap){
		mmap.put("user", userService.getUserById(userId));
		return prefix + "/resetPwd";
	}

	@RequiresPermissions("system:user:resetPwd")
	@Log(title = "重置密码", businessType = BusinessType.UPDATE)
	@PostMapping("/resetPwd")
	@ResponseBody
	public R resetPwdSave(@Validated SysUserDTO user){
		userService.checkUserAllowed(user);
		user.setSalt(ShiroUtils.randomSalt());
		user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
		user.setUpdateBy(ShiroUtils.getLoginName());
		if (userService.updateUserPwd(user) > 0){
			if (ShiroUtils.getUserId().longValue() == user.getUserId().longValue()){
				ShiroUtils.setSysUser(userService.getUserById(user.getUserId()));
			}
			return R.ok();
		}
		return R.error();
	}

	//===================分配角色=======================
	/**
	 * 进入授权角色页
	 */
	@GetMapping("/authRole/{userId}")
	public String authRole(@PathVariable("userId") Long userId, ModelMap mmap){
		SysUserDTO user = userService.getUserById(userId);
		// 获取用户所属的角色列表
		List<SysUserRole> userRoles = userService.listUserRoleByUserId(userId);
		mmap.put("user", user);
		mmap.put("userRoles", userRoles);
		return prefix + "/authRole";
	}

	/**
	 * 用户授权角色
	 */
	@RequiresPermissions("system:user:add")
	@Log(title = "用户管理", businessType = BusinessType.GRANT)
	@PostMapping("/authRole/insertAuthRole")
	@ResponseBody
	public R insertAuthRole(Long userId, Long[] roleIds){
		userService.insertUserRoles(userId, roleIds);
		return R.ok();
	}
}
