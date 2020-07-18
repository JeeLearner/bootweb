package com.jee.boot.admin.controller.system;

import java.util.List;

import com.jee.boot.common.annotation.DataSource;
import com.jee.boot.common.core.web.BaseController;
import com.jee.boot.common.enums.DataSourceType;
import com.jee.boot.system.domain.SysUserRole;
import com.jee.boot.system.dto.SysUserDTO;
import com.jee.boot.system.service.ISysUserService;
import com.jee.boot.system.vo.SysUserVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.jee.boot.common.annotation.Log;
import com.jee.boot.common.utils.text.Convert;
import com.jee.boot.common.enums.BusinessType;
import com.jee.boot.common.core.page.PageUtil;
import com.jee.boot.common.core.result.R;
import com.jee.boot.common.utils.bean.JeeBeanUtils;
import com.jee.boot.common.utils.spring.ServletUtils;
import com.jee.boot.system.dto.SysRoleDTO;
import com.jee.boot.system.vo.SysRoleVO;
import com.jee.boot.system.service.ISysRoleService;
import com.jee.boot.shiro.utils.ShiroUtils;



/**
 * 角色 控制层
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
    private String prefix = "system/role";
	
	@Autowired
	private ISysRoleService roleService;
	@Autowired
	private ISysUserService userService;
	
	@RequiresPermissions("system:role:view")
	@GetMapping()
	public String role() {
	    return prefix + "/role";
	}
	
	/**
	 * 查询角色列表
	 */
	@RequiresPermissions("system:role:list")
	@PostMapping("/list")
	@ResponseBody
	public R list(SysRoleDTO dto) {
		return PageUtil.page(ServletUtils.getRequest(), () -> {
			List<SysRoleDTO> dtoList = roleService.listRole(dto);
			return JeeBeanUtils.mapList(dtoList, SysRoleDTO.class, SysRoleVO.class);
		});
	}
	
	/**
	 * 新增角色
	 */
	@GetMapping("/add")
	public String add() {
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存角色
	 */
	@RequiresPermissions("system:role:add")
	@Log(title = "角色管理", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public R addSave(SysRoleDTO dto) {
	    if (!roleService.checkRoleNameUnique(dto)){
            return R.error().msg("新增角色'" + dto.getRoleName() + "'失败，角色名称已存在");
        }
        if (!roleService.checkRoleKeyUnique(dto)){
            return R.error().msg("新增角色'" + dto.getRoleName() + "'失败，角色权限已存在");
        }
		dto.setCreateBy(ShiroUtils.getLoginName());
        ShiroUtils.clearCachedAuthorizationInfo();
		int row = roleService.insertRole(dto);
		return res(row);
	}

	/**
	 * 修改角色
	 */
	@GetMapping("/edit/{roleId}")
	public String edit(@PathVariable("roleId") Long roleId, ModelMap mmap) {
		SysRoleDTO role = roleService.getRoleById(roleId);
		mmap.put("role", role);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存角色
	 */
	@RequiresPermissions("system:role:edit")
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@PutMapping("/edit")
	@ResponseBody
	public R editSave(SysRoleDTO dto) {
	    //判断是否允许修改此角色
        roleService.checkRoleAllowed(dto);
        if (!roleService.checkRoleNameUnique(dto)){
            return R.error().msg("修改角色'" + dto.getRoleName() + "'失败，角色名称已存在");
        }
        if (!roleService.checkRoleKeyUnique(dto)){
            return R.error().msg("修改角色'" + dto.getRoleName() + "'失败，角色权限已存在");
        }
        dto.setUpdateBy(ShiroUtils.getLoginName());
		ShiroUtils.clearCachedAuthorizationInfo();
		int row = roleService.updateRole(dto);
		return res(row);
	}
	
	/**
	 * 删除角色
	 */
	@RequiresPermissions("system:role:remove")
	@Log(title = "角色管理", businessType = BusinessType.DELETE)
	@DeleteMapping( "/remove")
	@ResponseBody
	public R remove(String ids) {
	    try {
            int row = roleService.deleteRoleByIds(Convert.toLongArray(ids));
            return res(row);
        } catch (Exception e){
	        return R.error().msg(e.getMessage());
        }
	}
    /**
     * 校验角色名称
     */
    @PostMapping("/checkRoleNameUnique")
    @ResponseBody
    public boolean checkRoleNameUnique(SysRoleDTO role){
        return roleService.checkRoleNameUnique(role);
    }

    /**
     * 校验角色权限
     */
    @PostMapping("/checkRoleKeyUnique")
    @ResponseBody
    public boolean checkRoleKeyUnique(SysRoleDTO role) {
        return roleService.checkRoleKeyUnique(role);
    }

	/**
	 * 修改角色状态
	 */
	@RequiresPermissions("system:role:edit")
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@PostMapping("/changeStatus")
	@ResponseBody
	public R changeStatus(SysRoleDTO dto) {
		//判断是否允许修改此角色
		roleService.checkRoleAllowed(dto);
		int row = roleService.updateStatus(dto);
		return res(row);
	}


	//==============================分配数据权限====================================
	/**
	 * 角色分配数据权限
	 */
	@GetMapping("/authDataScope/{roleId}")
	public String authDataScope(@PathVariable("roleId") Long roleId, ModelMap mmap){
		mmap.put("role", roleService.getRoleById(roleId));
		return prefix + "/dataScope";
	}

	/**
	 * 保存角色分配数据权限
	 */
	@RequiresPermissions("system:role:edit")
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@PostMapping("/authDataScope")
	@ResponseBody
	public R authDataScopeSave(SysRoleDTO role){
		roleService.checkRoleAllowed(role);
		role.setUpdateBy(ShiroUtils.getLoginName());
		if (roleService.updateDataScope(role) > 0){
			//本用户的数据权限重置
			ShiroUtils.setSysUser(userService.getUserById(ShiroUtils.getSysUser().getUserId()));
			return R.ok();
		}
		return R.error();
	}


	//============================分配用户====================================
	/**
	 * 分配用户
	 */
	@RequiresPermissions("system:role:edit")
	@GetMapping("/authUser/{roleId}")
	public String authUser(@PathVariable("roleId") Long roleId, ModelMap mmap) {
		mmap.put("role", roleService.getRoleById(roleId));
		return prefix + "/authUser";
	}

	/**
	 * 查询已分配用户角色的用户列表
	 */
	@RequiresPermissions("system:role:list")
	@PostMapping("/authUser/allocatedList")
	@ResponseBody
	public R allocatedList(SysUserDTO user){
		return PageUtil.page(ServletUtils.getRequest(), () -> {
			List<SysUserDTO> dtoList = userService.listAllocatedRole(user);
			return JeeBeanUtils.mapList(dtoList, SysUserDTO.class, SysUserVO.class);
		});
	}

	/**
	 * 选择用户
	 */
	@GetMapping("/authUser/selectUser/{roleId}")
	public String selectUser(@PathVariable("roleId") Long roleId, ModelMap mmap){
		mmap.put("role", roleService.getRoleById(roleId));
		return prefix + "/selectUser";
	}

	/**
	 * 查询未分配用户角色的用户列表
	 */
	@RequiresPermissions("system:role:list")
	@PostMapping("/authUser/unallocatedList")
	@ResponseBody
	public R unallocatedList(SysUserDTO user){
		return PageUtil.page(ServletUtils.getRequest(), () -> {
			List<SysUserDTO> dtoList = userService.listUnallocatedRole(user);
			return JeeBeanUtils.mapList(dtoList, SysUserDTO.class, SysUserVO.class);
		});
	}

	/**
	 * 批量选择用户授权
	 */
	@Log(title = "角色管理", businessType = BusinessType.GRANT)
	@PostMapping("/authUser/save")
	@ResponseBody
	public R saveAuthUserAll(Long roleId, String userIds){
		int row = roleService.insertRoleUsers(roleId, Convert.toLongArray(userIds));
		return res(row);
	}

	/**
	 * 取消授权
	 */
	@Log(title = "角色管理", businessType = BusinessType.GRANT)
	@PostMapping("/authUser/cancel")
	@ResponseBody
	public R cancelAuthUser(SysUserRole userRole){
		int row = roleService.deleteRoleUser(userRole);
		return res(row);
	}

	/**
	 * 批量取消授权
	 */
	@Log(title = "角色管理", businessType = BusinessType.GRANT)
	@PostMapping("/authUser/cancelAll")
	@ResponseBody
	public R cancelAuthUserAll(Long roleId, String userIds){
		int row = roleService.deleteRoleUsers(roleId, Convert.toLongArray(userIds));
		return res(row);
	}
}
