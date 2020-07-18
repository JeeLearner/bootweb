package com.jee.boot.system.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jee.boot.common.annotation.DataScope;
import com.jee.boot.common.constant.UserConstants;
import com.jee.boot.common.core.domain.Ztree;
import com.jee.boot.common.exception.base.BusinessException;
import com.jee.boot.common.utils.bean.JeeBeanUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.system.domain.SysDept;
import com.jee.boot.system.dto.SysRoleDTO;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jee.boot.system.mapper.SysDeptMapper;
import com.jee.boot.system.dto.SysDeptDTO;
import com.jee.boot.system.service.ISysDeptService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 部门 服务层实现
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {
	@Autowired
	private SysDeptMapper deptMapper;

	/**
	 * 查询部门列表
	 *
	 * @param dept 部门信息
	 * @return 部门集合
	 */
	@Override
	@DataScope(deptAlias = "d")
	public List<SysDeptDTO> listDept(SysDeptDTO dept) {
		return deptMapper.selectDeptList(dept);
	}

	/**
	 * 根据ID查询所有子部门
	 *
	 * @param deptId 部门ID
	 * @return 部门列表
	 */
	@Override
	public List<SysDeptDTO> listChildrenDeptById(Long deptId) {
		return null;
	}

	/**
	 * 根据角色ID查询部门
	 *
	 * @param roleId 角色ID
	 * @return 部门列表
	 */
	@Override
	public List<String> listRoleDeptTree(Long roleId) {
		return null;
	}

	/**
	 * 查询部门管理树
	 *
	 * @param dept 部门信息
	 * @return 所有部门信息
	 */
	@Override
	@DataScope(deptAlias = "d")
	public List<Ztree> listDeptTree(SysDeptDTO dept){
		List<SysDeptDTO> deptList = deptMapper.selectDeptList(dept);
		List<Ztree> ztrees = initZtree(deptList);
		return ztrees;
	}

	/**
	 * 查询部门管理树（排除下级）
	 *
	 * @param dept 部门
	 * @return 所有部门信息
	 */
	@Override
	@DataScope(deptAlias = "d")
	public List<Ztree> listDeptTreeExcludeChild(SysDeptDTO dept){
		Long deptId = dept.getDeptId();
		List<SysDeptDTO> deptList = deptMapper.selectDeptList(dept);
		Iterator<SysDeptDTO> it = deptList.iterator();
		while (it.hasNext()) {
			SysDeptDTO d = it.next();
			if (d.getDeptId().longValue() == deptId
					|| ArrayUtils.contains(JeeStringUtils.split(d.getAncestors(), ","), deptId + "")) {
				it.remove();
			}
		}
		List<Ztree> ztrees = initZtree(deptList);
		return ztrees;
	}

	/**
	 * 根据角色ID查询部门（数据权限）
	 *
	 * @param role 角色对象
	 * @return 部门列表（数据权限）
	 */
	@Override
	public List<Ztree> listDeptTreeDataByRole(SysRoleDTO role){
		Long roleId = role.getRoleId();
		List<Ztree> ztrees = new ArrayList<Ztree>();
		List<SysDeptDTO> deptList = listDept(new SysDeptDTO());
		if (JeeStringUtils.isNotNull(roleId)){
			List<String> roleDeptList = deptMapper.selectRoleDeptTree(roleId);
			ztrees = initZtree(deptList, roleDeptList);
		} else {
			ztrees = initZtree(deptList);
		}
		return ztrees;
	}

	/**
	 * 查询部门信息
	 *
	 * @param deptId 部门ID
	 * @return 部门信息
	 */
	@Override
	public SysDeptDTO getDeptById(Long deptId) {
		return deptMapper.selectDeptById(deptId);
	}

	/**
	 * 根据部门parentId查询子部门数量
	 *
	 * @param parentId 部门parentId
	 * @return 结果
	 */
	@Override
	public int countDeptByParentId(Long parentId) {
		return deptMapper.countDeptByParentId(parentId);
	}

	/**
	 * 根据ID查询所有子部门（正常状态）
	 *
	 * @param deptId 部门ID
	 * @return 子部门数
	 */
	@Override
	public int countNormalChildrenDeptById(Long deptId) {
		return deptMapper.countNormalChildrenDeptById(deptId);
	}

	/**
	 * 新增部门
	 *
	 * @param dept 部门信息
	 * @return 结果
	 */
	@Override
	public int insertDept(SysDeptDTO dept) {
		SysDeptDTO info = deptMapper.selectDeptById(dept.getParentId());
		// 如果父节点不为"正常"状态,则不允许新增子节点
		if (!UserConstants.STATUS_NORMAL.equals(info.getStatus())){
			throw new BusinessException("父部门停用，不允许新增");
		}
		dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
		dept.setCreateTime(LocalDateTime.now());
		return deptMapper.insertDept(dept);
	}

	/**
	 * 修改部门
	 *
	 * @param dept 部门信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateDept(SysDeptDTO dept) {
		SysDeptDTO newParentDept = getDeptById(dept.getParentId());
		//不是根节点
		if (newParentDept != null){
			SysDeptDTO oldDept = getDeptById(dept.getDeptId());
			if (JeeStringUtils.isNotNull(newParentDept) && JeeStringUtils.isNotNull(oldDept)){
				String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
				String oldAncestors = oldDept.getAncestors();
				dept.setAncestors(newAncestors);
				updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
			}
			if (UserConstants.STATUS_NORMAL.equals(dept.getStatus())){
				// 如果该部门是启用状态，则启用该部门的所有上级部门
				updateParentDeptStatus(dept);
			}
		}
		dept.setUpdateTime(LocalDateTime.now());
		int row = deptMapper.updateDept(dept);
		return row;
	}

	/**
	 * 修改子元素关系
	 *
	 * @param deptId 被修改的部门ID
	 * @param newAncestors 新的父ID集合
	 * @param oldAncestors 旧的父ID集合
	 */
	@Override
	public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
		List<SysDeptDTO> children = deptMapper.selectChildrenDeptById(deptId);
		List<SysDept> list = JeeBeanUtils.mapList(children, SysDeptDTO.class, SysDept.class);
		for (SysDept child : list){
			child.setUpdateTime(LocalDateTime.now());
			child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
		}
		if (list.size() > 0){
			deptMapper.updateDeptChildren(list);
		}
	}

	/**
	 * 修改该部门的父级部门状态
	 *
	 * @param dept 当前部门
	 */
	@Override
	public void updateParentDeptStatus(SysDeptDTO dept) {
		deptMapper.updateParentDeptStatus(dept);
	}

	/**
	 * 删除部门
	 * 		逻辑删除
	 *
	 * @param deptId 部门ID
	 * @return 结果
	 */
	@Override
	public int deleteDeptById(Long deptId) {
		return deptMapper.deleteDeptById(deptId);
	}


	/**
	 * 查询部门是否存在用户
	 *
	 * @param deptId 部门ID
	 * @return 结果
	 */
	@Override
	public boolean checkDeptExistUser(Long deptId) {
		int row = deptMapper.checkDeptExistUser(deptId);
		return row > 0 ? true : false;
	}

	/**
	 * 校验部门名称是否唯一
	 *
	 * @param dept 部门信息
	 * @return 结果
	 */
	@Override
	public boolean checkDeptNameUnique(SysDeptDTO dept) {
		Long deptId = JeeStringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
		SysDeptDTO info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
		if (JeeStringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()){
			return false;
		}
		return true;
	}


	/**
	 * 对象转部门树
	 *
	 * @param deptList 部门列表
	 * @return 树结构列表
	 */
	private List<Ztree> initZtree(List<SysDeptDTO> deptList){
		return initZtree(deptList, null);
	}

	/**
	 * 对象转部门树
	 *
	 * @param deptList 部门列表
	 * @param roleDeptList 角色已存在菜单列表
	 * @return 树结构列表
	 */
	private List<Ztree> initZtree(List<SysDeptDTO> deptList, List<String> roleDeptList){
		List<Ztree> ztrees = new ArrayList<Ztree>();
		boolean isCheck = JeeStringUtils.isNotNull(roleDeptList);
		for (SysDeptDTO dept : deptList){
			if (UserConstants.STATUS_NORMAL.equals(dept.getStatus())){
				Ztree ztree = new Ztree();
				ztree.setId(dept.getDeptId());
				ztree.setpId(dept.getParentId());
				ztree.setName(dept.getDeptName());
				ztree.setTitle(dept.getDeptName());
				if (isCheck){
					ztree.setChecked(roleDeptList.contains(dept.getDeptId() + dept.getDeptName()));
				}
				ztrees.add(ztree);
			}
		}
		return ztrees;
	}
}
