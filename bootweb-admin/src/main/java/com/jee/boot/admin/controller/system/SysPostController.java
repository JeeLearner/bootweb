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
import com.jee.boot.system.dto.SysPostDTO;
import com.jee.boot.system.vo.SysPostVO;
import com.jee.boot.system.service.ISysPostService;
import com.jee.boot.shiro.utils.ShiroUtils;



/**
 * 岗位 控制层
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/system/post")
public class SysPostController extends BaseController {
    private String prefix = "system/post";
	
	@Autowired
	private ISysPostService postService;
	
	@RequiresPermissions("system:post:view")
	@GetMapping()
	public String post() {
	    return prefix + "/post";
	}
	
	/**
	 * 查询岗位列表
	 */
	@RequiresPermissions("system:post:list")
	@PostMapping("/list")
	@ResponseBody
	public R list(SysPostDTO dto) {
		return PageUtil.page(() -> {
			List<SysPostDTO> dtoList = postService.listPost(dto);
			return JeeBeanUtils.mapList(dtoList, SysPostDTO.class, SysPostVO.class);
		});
	}
	
	/**
	 * 新增岗位
	 */
	@GetMapping("/add")
	public String add() {
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存岗位
	 */
	@RequiresPermissions("system:post:add")
	@Log(title = "岗位管理", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public R addSave(@Validated SysPostDTO dto) {
		if (!postService.checkPostNameUnique(dto)){
			return R.error().msg("新增岗位'" + dto.getPostName() + "'失败，岗位名称已存在");
		}
		if (!postService.checkPostCodeUnique(dto)){
			return R.error().msg("新增岗位'" + dto.getPostName() + "'失败，岗位编码已存在");
		}
		dto.setCreateBy(ShiroUtils.getLoginName());
		int row = postService.insertPost(dto);
		return res(row);
	}

	/**
	 * 修改岗位
	 */
	@GetMapping("/edit/{postId}")
	public String edit(@PathVariable("postId") Long postId, ModelMap mmap) {
		SysPostDTO post = postService.getPostById(postId);
		mmap.put("post", post);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存岗位
	 */
	@RequiresPermissions("system:post:edit")
	@Log(title = "岗位管理", businessType = BusinessType.UPDATE)
	@PutMapping("/edit")
	@ResponseBody
	public R editSave(@Validated SysPostDTO dto) {
		if (!postService.checkPostNameUnique(dto)){
			return R.error().msg("修改岗位'" + dto.getPostName() + "'失败，岗位名称已存在");
		}
		if (!postService.checkPostCodeUnique(dto)){
			return R.error().msg("修改岗位'" + dto.getPostName() + "'失败，岗位编码已存在");
		}
		dto.setUpdateBy(ShiroUtils.getLoginName());
		int row = postService.updatePost(dto);
		return res(row);
	}
	
	/**
	 * 删除岗位
	 */
	@RequiresPermissions("system:post:remove")
	@Log(title = "岗位管理", businessType = BusinessType.DELETE)
	@DeleteMapping( "/remove")
	@ResponseBody
	public R remove(String ids) {
		int row = postService.deletePostByIds(Convert.toLongArray(ids));
		return res(row);
	}

	/**
	 * 校验岗位名称
	 */
	@PostMapping("/checkPostNameUnique")
	@ResponseBody
	public boolean checkPostNameUnique(SysPostDTO post){
		return postService.checkPostNameUnique(post);
	}

	/**
	 * 校验岗位编码
	 */
	@PostMapping("/checkPostCodeUnique")
	@ResponseBody
	public boolean checkPostCodeUnique(SysPostDTO post){
		return postService.checkPostCodeUnique(post);
	}
}
