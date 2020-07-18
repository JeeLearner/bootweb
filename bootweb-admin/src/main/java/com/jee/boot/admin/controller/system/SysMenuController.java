package com.jee.boot.admin.controller.system;

import java.util.List;

import com.jee.boot.common.core.domain.Ztree;
import com.jee.boot.common.core.web.BaseController;
import com.jee.boot.system.dto.SysRoleDTO;
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
import com.jee.boot.system.dto.SysMenuDTO;
import com.jee.boot.system.vo.SysMenuVO;
import com.jee.boot.system.service.ISysMenuService;
import com.jee.boot.shiro.utils.ShiroUtils;



/**
 * 菜单权限 控制层
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {
    private String prefix = "system/menu";
	
	@Autowired
	private ISysMenuService menuService;
	
	@RequiresPermissions("system:menu:view")
	@GetMapping()
	public String menu() {
	    return prefix + "/menu";
	}
	
	/**
	 * 查询菜单权限列表
	 */
	@RequiresPermissions("system:menu:list")
	@PostMapping("/list")
	@ResponseBody
	public R list(SysMenuDTO dto) {
		Long userId = ShiroUtils.getUserId();
		List<SysMenuDTO> dtoList = menuService.listMenu(dto, userId);
		List<SysMenuVO> list = JeeBeanUtils.mapList(dtoList, SysMenuDTO.class, SysMenuVO.class);
		return R.ok().data("list", list);
	}
	
	/**
	 * 新增菜单权限
	 */
	@GetMapping("/add/{parentId}")
	public String add(@PathVariable("parentId") Long parentId, ModelMap mmap) {
		SysMenuDTO menu = null;
		if (0L != parentId) {
			menu = menuService.getMenuById(parentId);
		} else {
			menu = new SysMenuDTO();
			menu.setMenuId(0L);
			menu.setMenuName("主目录");
		}
		mmap.put("menu", menu);
		return prefix + "/add";
	}

	/**
	 * 新增保存菜单权限
	 */
	@RequiresPermissions("system:menu:add")
	@Log(title = "菜单权限", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public R addSave(@Validated SysMenuDTO dto) {
		if (!menuService.checkMenuNameUnique(dto)){
			return R.error().msg("新增菜单'" + dto.getMenuName() + "'失败，菜单名称已存在");
		}
		dto.setCreateBy(ShiroUtils.getLoginName());
		//这里清除一次授权缓存
		ShiroUtils.clearCachedAuthorizationInfo();
		int row = menuService.insertMenu(dto);
		return res(row);
	}

	/**
	 * 修改菜单权限
	 */
	@GetMapping("/edit/{menuId}")
	public String edit(@PathVariable("menuId") Long menuId, ModelMap mmap) {
		SysMenuDTO menu = menuService.getMenuById(menuId);
		mmap.put("menu", menu);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存菜单权限
	 */
	@RequiresPermissions("system:menu:edit")
	@Log(title = "菜单权限", businessType = BusinessType.UPDATE)
	@PutMapping("/edit")
	@ResponseBody
	public R editSave(@Validated SysMenuDTO dto) {
		if (!menuService.checkMenuNameUnique(dto)){
			return R.error().msg("修改菜单'" + dto.getMenuName() + "'失败，菜单名称已存在");
		}
		dto.setUpdateBy(ShiroUtils.getLoginName());
		ShiroUtils.clearCachedAuthorizationInfo();
		int row = menuService.updateMenu(dto);
		return res(row);
	}

	/**
	 * 删除菜单
	 */
	@RequiresPermissions("system:menu:remove")
	@Log(title = "菜单", businessType = BusinessType.DELETE)
	@DeleteMapping( "/remove")
	@ResponseBody
	public R remove(String ids) {
		Long id = Long.parseLong(ids);
		if (menuService.countMenuByParentId(id) > 0){
			return R.error().msg("存在子菜单,不允许删除");
		}
		if (menuService.countRoleMenuByMenuId(id) > 0){
			return R.error().msg("菜单已分配,不允许删除");
		}
		int row = menuService.deleteMenuById(id);
		return res(row);
	}

	/**
	 * 选择菜单图标
	 */
	@GetMapping("/icon")
	public String icon()
	{
		return prefix + "/icon";
	}

	/**
	 * 校验菜单名称
	 */
	@PostMapping("/checkMenuNameUnique")
	@ResponseBody
	public boolean checkMenuNameUnique(SysMenuDTO menu) {
		return menuService.checkMenuNameUnique(menu);
	}

	/**
	 * 选择菜单树
	 *
	 * @param menuId 菜单ID
	 */
	@GetMapping(value = { "/selectMenuTree/{menuId}", "/selectMenuTree/{menuId}/{excludeId}" })
	public String selectMenuTree(@PathVariable("menuId") Long menuId,
								 @PathVariable(value = "excludeId", required = false) String excludeId,ModelMap mmap){
		mmap.put("menu", menuService.getMenuById(menuId));
		mmap.put("excludeId", excludeId);
		return prefix + "/tree";
	}

	/**
	 * 加载所有菜单列表树
	 */
	@GetMapping("/menuTreeData")
	@ResponseBody
	public List<Ztree> menuTreeData() {
		Long userId = ShiroUtils.getUserId();
		List<Ztree> ztrees = menuService.listMenuTree(userId);
		return ztrees;
	}

	/**
	 * 加载部门列表树（排除本菜单）
	 * 		修改时调用，排除本菜单
	 */
	@GetMapping("/menuTreeData/{excludeId}")
	@ResponseBody
	public List<Ztree> treeDataExcludeChild(@PathVariable(value = "excludeId", required = false) Long excludeId){
		SysMenuDTO menu = new SysMenuDTO();
		menu.setMenuId(excludeId);
		List<Ztree> ztrees = menuService.listMenuTreeExcludeChild(menu);
		return ztrees;
	}

	//==============================role模块===================================
	/**
	 * 加载角色菜单列表树
	 */
	@GetMapping("/roleMenuTreeData")
	@ResponseBody
	public List<Ztree> roleMenuTreeData(SysRoleDTO role){
		Long userId = ShiroUtils.getUserId();
		List<Ztree> ztrees = menuService.listRoleMenuTreeData(role, userId);
		return ztrees;
	}
}
