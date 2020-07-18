package com.jee.boot.admin.controller.system;

import java.util.List;

import com.jee.boot.common.constant.UserConstants;
import com.jee.boot.common.core.domain.Ztree;
import com.jee.boot.common.core.web.BaseController;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.system.dto.SysRoleDTO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.jee.boot.common.annotation.Log;
import com.jee.boot.common.enums.BusinessType;
import com.jee.boot.common.core.result.R;
import com.jee.boot.common.utils.bean.JeeBeanUtils;
import com.jee.boot.system.dto.SysDeptDTO;
import com.jee.boot.system.vo.SysDeptVO;
import com.jee.boot.system.service.ISysDeptService;
import com.jee.boot.shiro.utils.ShiroUtils;



/**
 * 部门 控制层
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {
    private String prefix = "system/dept";
	
	@Autowired
	private ISysDeptService deptService;
	
	@RequiresPermissions("system:dept:view")
	@GetMapping()
	public String dept() {
	    return prefix + "/dept";
	}
	
	/**
	 * 查询部门列表
	 */
	@RequiresPermissions("system:dept:list")
	@PostMapping("/list")
	@ResponseBody
	public R list(SysDeptDTO dto) {
		List<SysDeptDTO> dtoList = deptService.listDept(dto);
		List<SysDeptVO> list = JeeBeanUtils.mapList(dtoList, SysDeptDTO.class, SysDeptVO.class);
		return R.ok().data("list", list);
	}
	
	/**
	 * 新增部门
	 */
	@GetMapping("/add/{parentId}")
	public String add(@PathVariable("parentId") Long parentId, ModelMap mmap) {
		mmap.put("dept", deptService.getDeptById(parentId));
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存部门
	 */
	@RequiresPermissions("system:dept:add")
	@Log(title = "部门", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public R addSave(@Validated SysDeptDTO dto) {
		if (!deptService.checkDeptNameUnique(dto)){
			return R.error().msg("新增部门'" + dto.getDeptName() + "'失败，部门名称已存在");
		}
		dto.setCreateBy(ShiroUtils.getLoginName());
		int row = deptService.insertDept(dto);
		return res(row);
	}

	/**
	 * 修改部门
	 */
	@GetMapping("/edit/{deptId}")
	public String edit(@PathVariable("deptId") Long deptId, ModelMap mmap) {
		SysDeptDTO dept = deptService.getDeptById(deptId);
		if (JeeStringUtils.isNotNull(dept) && 100L == deptId) {
			dept.setParentName("无");
		}
		mmap.put("dept", dept);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存部门
	 */
	@RequiresPermissions("system:dept:edit")
	@Log(title = "部门", businessType = BusinessType.UPDATE)
	@PutMapping("/edit")
	@ResponseBody
	public R editSave(@Validated SysDeptDTO dto) {
		if (!deptService.checkDeptNameUnique(dto)){
			return R.error().msg("修改部门'" + dto.getDeptName() + "'失败，部门名称已存在");
		}
		if (dto.getDeptId().equals(dto.getParentId())){
			return R.error().msg("修改部门'" + dto.getDeptName() + "'失败，上级部门不能是自己");
		}
		if (JeeStringUtils.equals(UserConstants.STATUS_DISABLE, dto.getStatus())
			&& deptService.countNormalChildrenDeptById(dto.getDeptId()) > 0){
			return R.error().msg("该部门包含未停用的子部门！");
		}
		dto.setUpdateBy(ShiroUtils.getLoginName());
		int row = deptService.updateDept(dto);
		return res(row);
	}
	
	/**
	 * 删除部门
	 */
	@RequiresPermissions("system:dept:remove")
	@Log(title = "部门", businessType = BusinessType.DELETE)
	@DeleteMapping( "/remove")
	@ResponseBody
	public R remove(String ids) {
		Long id = Long.parseLong(ids);
		if (deptService.countDeptByParentId(id) > 0){
			return R.error().msg("存在下级部门,不允许删除");
		}
		if (deptService.checkDeptExistUser(id)){
			return R.error().msg("部门存在用户,不允许删除");
		}
		int row = deptService.deleteDeptById(id);
		return res(row);
	}

	/**
	 * 校验部门名称
	 */
	@PostMapping("/checkDeptNameUnique")
	@ResponseBody
	public boolean checkDeptNameUnique(SysDeptDTO dept) {
		return deptService.checkDeptNameUnique(dept);
	}

	/**
	 * 选择部门树
	 *
	 * @param deptId 部门ID
	 * @param excludeId 排除ID
	 */
	@GetMapping(value = { "/selectDeptTree/{deptId}", "/selectDeptTree/{deptId}/{excludeId}" })
	public String selectDeptTree(@PathVariable("deptId") Long deptId,
								 @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap) {
		mmap.put("dept", deptService.getDeptById(deptId));
		mmap.put("excludeId", excludeId);
		return prefix + "/tree";
	}

	/**
	 * 加载部门列表树
	 */
	@GetMapping("/treeData")
	@ResponseBody
	public List<Ztree> treeData(){
		List<Ztree> ztrees = deptService.listDeptTree(new SysDeptDTO());
		return ztrees;
	}

	/**
	 * 加载部门列表树（排除下级）
	 * 		修改时调用，排除本部门及其下级部门
	 */
	@GetMapping("/treeData/{excludeId}")
	@ResponseBody
	public List<Ztree> treeDataExcludeChild(@PathVariable(value = "excludeId", required = false) Long excludeId){
		SysDeptDTO dept = new SysDeptDTO();
		dept.setDeptId(excludeId);
		List<Ztree> ztrees = deptService.listDeptTreeExcludeChild(dept);
		return ztrees;
	}

	/**
	 * 加载角色部门（数据权限）列表树
	 */
	@GetMapping("/roleDeptTreeData")
	@ResponseBody
	public List<Ztree> deptTreeData(SysRoleDTO role){
		List<Ztree> ztrees = deptService.listDeptTreeDataByRole(role);
		return ztrees;
	}
}
