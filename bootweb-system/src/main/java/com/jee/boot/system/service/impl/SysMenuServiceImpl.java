package com.jee.boot.system.service.impl;

import java.time.LocalDateTime;
import java.util.*;

import com.jee.boot.common.core.domain.Ztree;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.system.domain.SysMenu;
import com.jee.boot.system.dto.SysRoleDTO;
import com.jee.boot.system.dto.SysUserDTO;
import com.jee.boot.system.mapper.SysRoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jee.boot.system.mapper.SysMenuMapper;
import com.jee.boot.system.dto.SysMenuDTO;
import com.jee.boot.system.service.ISysMenuService;

/**
 * 菜单权限 服务层实现
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService {
	@Autowired
	private SysMenuMapper menuMapper;
	@Autowired
	private SysRoleMenuMapper roleMenuMapper;

	/**
     * 查询菜单权限信息
     * 
     * @param menuId 菜单权限ID
     * @return 菜单权限信息
     */
    @Override
	public SysMenuDTO getMenuById(Long menuId) {
	    return menuMapper.selectMenuById(menuId);
	}

	/**
	 * 查询系统菜单列表
	 *
	 * @param menu 菜单信息
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	@Override
	public List<SysMenuDTO> listMenu(SysMenuDTO menu, Long userId) {
		List<SysMenuDTO> menuList = null;
		if (SysUserDTO.isAdmin(userId)){
			menuList = menuMapper.selectMenuList(menu);
		} else {
			menu.getParams().put("userId", userId);
			menuList = menuMapper.selectMenuListByUserId(menu);
		}
		return menuList;
	}
	
    /**
     * 新增菜单权限
     * 
     * @param menu 菜单权限信息
     * @return 结果
     */
	@Override
	public int insertMenu(SysMenuDTO menu) {
		menu.setCreateTime(LocalDateTime.now());
	    return menuMapper.insertMenu(menu);
	}
	
	/**
     * 修改菜单权限
     * 
     * @param menu 菜单权限信息
     * @return 结果
     */
	@Override
	public int updateMenu(SysMenuDTO menu) {
		menu.setUpdateTime(LocalDateTime.now());
	    return menuMapper.updateMenu(menu);
	}

	/**
     * 删除菜单权限对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMenuByIds(Long[] ids) {
		return menuMapper.deleteMenuByIds(ids);
	}

	@Override
	public Set<String> listPermsByUserId(Long userId) {
		List<String> perms = menuMapper.selectPermsByUserId(userId);
		Set<String> permsSet = new HashSet<>();
		for (String perm : perms){
			if (JeeStringUtils.isNotEmpty(perm)){
				permsSet.addAll(Arrays.asList(perm.trim().split(",")));
			}
		}
		return permsSet;
	}

	@Override
	public List<Ztree> listMenuTreeExcludeChild(SysMenuDTO menu) {
		Long menuId = menu.getMenuId();
		List<SysMenuDTO> menuList = menuMapper.selectMenuList(menu);
		Iterator<SysMenuDTO> it = menuList.iterator();
		while (it.hasNext()) {
			SysMenuDTO m = it.next();
			if (m.getMenuId().longValue() == menuId) {
				it.remove();
			}
		}
		List<Ztree> ztrees = initZtree(menuList);
		return ztrees;
	}

	/**
	 * 查询所有菜单
	 *
	 * @return 菜单列表
	 */
	@Override
	public List<Ztree> listMenuTree(Long userId) {
		List<SysMenuDTO> menuList = listMenuAll(userId);
		List<Ztree> ztrees = initZtree(menuList);
		return ztrees;
	}

	/**
	 * 根据角色ID查询菜单
	 *
	 * @param role 角色对象
	 * @return 菜单列表
	 */
	@Override
	public List<Ztree> listRoleMenuTreeData(SysRoleDTO role, Long userId) {
		Long roleId = role.getRoleId();
		List<Ztree> ztrees = new ArrayList<Ztree>();
		List<SysMenuDTO> menuList = listMenuAll(userId);
		if (JeeStringUtils.isNotNull(roleId)){
			List<String> roleMenuList = menuMapper.selectMenuTree(roleId);
			ztrees = initZtree(menuList, roleMenuList, true);
		}else {
			ztrees = initZtree(menuList, null, true);
		}
		return ztrees;
	}

	/**
	 * 根据parentID查询菜单数量
	 *
	 * @param parentId 菜单父ID
	 * @return 结果
	 */
	@Override
	public int countMenuByParentId(Long parentId) {
		return menuMapper.countMenuByParentId(parentId);
	}

	/**
	 * 查询菜单使用数量
	 *
	 * @param menuId 菜单ID
	 * @return 结果
	 */
	@Override
	public int countRoleMenuByMenuId(Long menuId) {
		return roleMenuMapper.countRoleMenuByMenuId(menuId);
	}

	/**
	 * 根据用户查询菜单
	 *
	 * @param user 用户信息
	 * @return 菜单列表
	 */
	@Override
	public List<SysMenuDTO> listMenusByUser(SysUserDTO user) {
		List<SysMenuDTO> menus = new LinkedList<SysMenuDTO>();
		// 管理员显示所有菜单信息
		if (user.isAdmin()){
			menus = menuMapper.selectMenuNormalAll();
		} else {
			menus = menuMapper.selectMenusByUserId(user.getUserId());
		}
		return getChildPerms(menus, 0);
	}

	/**
	 * 查询菜单集合
	 *
	 * @return 所有菜单信息
	 */
	@Override
	public List<SysMenuDTO> listMenuAll(Long userId) {
		List<SysMenuDTO> menuList = null;
		if (SysUserDTO.isAdmin(userId)){
			menuList = menuMapper.selectMenuAll();
		} else {
			menuList = menuMapper.selectMenuAllByUserId(userId);
		}
		return menuList;
	}

	/**
	 * 校验菜单名称是否唯一
	 *
	 * @param menu 菜单信息
	 * @return 结果
	 */
	@Override
	public boolean checkMenuNameUnique(SysMenuDTO menu){
		Long menuId = JeeStringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
		SysMenu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
		if (JeeStringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId){
			return false;
		}
		return true;
	}

	/**
	 * 删除菜单管理信息
	 *
	 * @param menuId 菜单ID
	 * @return 结果
	 */
	@Override
	public int deleteMenuById(Long menuId){
		return menuMapper.deleteMenuById(menuId);
	}

	/**
	 * 根据父节点的ID获取所有子节点
	 *
	 * @param list 分类表
	 * @param parentId 传入的父节点ID
	 * @return String
	 */
	private List<SysMenuDTO> getChildPerms(List<SysMenuDTO> list, int parentId) {
		List<SysMenuDTO> returnList = new ArrayList<SysMenuDTO>();
		Iterator<SysMenuDTO> iterator = list.iterator();
		while (iterator.hasNext()){
			SysMenuDTO t = iterator.next();
			// 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
			if (t.getParentId() == parentId){
				recursionFn(list, t);
				returnList.add(t);
			}
		}
		return returnList;
	}

	/**
	 * 递归列表
	 *
	 * @param list
	 * @param t
	 */
	private void recursionFn(List<SysMenuDTO> list, SysMenuDTO t) {
		// 得到子节点列表
		List<SysMenuDTO> childList = getChildList(list, t);
		t.setChildren(childList);
		for (SysMenuDTO tChild : childList) {
			// 判断是否有子节点
			if (hasChild(list, tChild)){
				Iterator<SysMenuDTO> it = childList.iterator();
				while (it.hasNext()) {
					SysMenuDTO n = it.next();
					recursionFn(list, n);
				}
			}
		}
	}

	/**
	 * 得到子节点列表
	 * @param list
	 * @param t
	 * @return
	 */
	private List<SysMenuDTO> getChildList(List<SysMenuDTO> list, SysMenuDTO t) {
		List<SysMenuDTO> tlist = new ArrayList<SysMenuDTO>();
		Iterator<SysMenuDTO> it = list.iterator();
		while (it.hasNext()){
			SysMenuDTO n = it.next();
			if (n.getParentId().longValue() == t.getMenuId().longValue()){
				tlist.add(n);
			}
		}
		return tlist;
	}

	/**
	 * 判断是否有子节点
	 */
	private boolean hasChild(List<SysMenuDTO> list, SysMenuDTO t) {
		return getChildList(list, t).size() > 0 ? true : false;
	}


	/**
	 * 对象转菜单树
	 *
	 * @param menuList 菜单列表
	 * @return 树结构列表
	 */
	private List<Ztree> initZtree(List<SysMenuDTO> menuList){
		return initZtree(menuList, null, false);
	}

	/**
	 * 对象转菜单树
	 *
	 * @param menuList 菜单列表
	 * @param roleMenuList 角色已存在菜单列表
	 * @param permsFlag 是否需要显示权限标识
	 * @return 树结构列表
	 */
	private List<Ztree> initZtree(List<SysMenuDTO> menuList, List<String> roleMenuList, boolean permsFlag) {
		List<Ztree> ztrees = new ArrayList<Ztree>();
		boolean isCheck = JeeStringUtils.isNotNull(roleMenuList);
		for (SysMenu menu : menuList) {
			Ztree ztree = new Ztree();
			ztree.setId(menu.getMenuId());
			ztree.setpId(menu.getParentId());
			ztree.setName(transMenuName(menu, permsFlag));
			ztree.setTitle(menu.getMenuName());
			if (isCheck){
				ztree.setChecked(roleMenuList.contains(menu.getMenuId() + menu.getPerms()));
			}
			ztrees.add(ztree);
		}
		return ztrees;
	}

	private String transMenuName(SysMenu menu, boolean permsFlag){
		StringBuffer sb = new StringBuffer();
		sb.append(menu.getMenuName());
		if (permsFlag){
			sb.append("<font color=\"#888\">&nbsp;&nbsp;&nbsp;" + menu.getPerms() + "</font>");
		}
		return sb.toString();
	}
}
