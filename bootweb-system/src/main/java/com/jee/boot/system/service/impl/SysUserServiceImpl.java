package com.jee.boot.system.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.jee.boot.common.annotation.DataScope;
import com.jee.boot.common.exception.base.BusinessException;
import com.jee.boot.common.utils.security.Md5Utils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.system.domain.SysUserPost;
import com.jee.boot.system.domain.SysUserRole;
import com.jee.boot.system.dto.SysPostDTO;
import com.jee.boot.system.dto.SysRoleDTO;
import com.jee.boot.system.mapper.*;
import com.jee.boot.system.service.ISysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jee.boot.system.dto.SysUserDTO;
import com.jee.boot.system.service.ISysUserService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户 服务层实现
 * 
 * @author jeeLearner
 * @version V1.0
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
	private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

	@Autowired
	private SysUserMapper userMapper;
	@Autowired
	private SysRoleMapper roleMapper;
	@Autowired
	private SysPostMapper postMapper;
	@Autowired
	private SysUserPostMapper userPostMapper;
	@Autowired
	private SysUserRoleMapper userRoleMapper;
	@Autowired
	private ISysConfigService configService;


	/**
	 * 导入用户数据
	 *
	 * @param userList 用户数据列表
	 * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
	 * @param operName 操作用户
	 * @return 结果
	 */
	@Override
	public String importUser(List<SysUserDTO> userList, boolean isUpdateSupport, String operName) {
		if (JeeStringUtils.isNull(userList) || userList.size() == 0){
			throw new BusinessException("导入用户数据不能为空！");
		}
		int successNum = 0;
		int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		String password = configService.getConfigByKey("sys.user.initPassword");
		for (SysUserDTO user : userList) {
			try {
				// 验证是否存在这个用户
				SysUserDTO u = userMapper.selectUserByLoginName(user.getLoginName());
				if (JeeStringUtils.isNull(u)) {
					user.setPassword(Md5Utils.hash(user.getLoginName() + password));
					user.setCreateBy(operName);
					user.setCreateTime(LocalDateTime.now());
					this.insertUser(user);
					successNum++;
					successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 导入成功");
				} else if (isUpdateSupport) {
					user.setUpdateBy(operName);
					user.setUpdateTime(LocalDateTime.now());
					successNum++;
					successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 更新成功");
				} else {
					failureNum++;
					failureMsg.append("<br/>" + failureNum + "、账号 " + user.getLoginName() + " 已存在");
				}
			} catch (Exception e) {
				failureNum++;
				String msg = "<br/>" + failureNum + "、账号 " + user.getLoginName() + " 导入失败：";
				failureMsg.append(msg + e.getMessage());
				log.error(msg, e);
			}
		}
		if (failureNum > 0){
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
			throw new BusinessException(failureMsg.toString());
		} else {
			successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
		}
		return successMsg.toString();
	}

	/**
     * 查询用户列表
     * 
     * @param user 用户信息
     * @return 用户集合
     */
	@Override
	@DataScope(deptAlias = "d", userAlias = "u")
	public List<SysUserDTO> listUser(SysUserDTO user) {
	    return userMapper.selectUserList(user);
	}

	/**
	 * 查询已分配用户角色的用户列表
	 *
	 * @param user 用户信息
	 * @return 用户信息集合信息
	 */
	@Override
	@DataScope(deptAlias = "d", userAlias = "u")
	public List<SysUserDTO> listAllocatedRole(SysUserDTO user) {
		return userMapper.selectAllocatedList(user);
	}

	/**
	 * 查询未分配用户角色的用户列表
	 *
	 * @param user 用户信息
	 * @return 用户信息集合信息
	 */
	@Override
	@DataScope(deptAlias = "d", userAlias = "u")
	public List<SysUserDTO> listUnallocatedRole(SysUserDTO user){
		return userMapper.selectUnallocatedList(user);
	}

	/**
	 * 通过用户ID查询用户和角色关联
	 *
	 * @param userId 用户ID
	 * @return 用户和角色关联列表
	 */
	@Override
	public List<SysUserRole> listUserRoleByUserId(Long userId){
		return userRoleMapper.selectUserRoleByUserId(userId);
	}

	/**
	 * 查询用户信息
	 *
	 * @param userId 用户ID
	 * @return 用户信息
	 */
	@Override
	public SysUserDTO getUserById(Long userId) {
		return userMapper.selectUserById(userId);
	}

	/**
	 * 通过用户名查询用户
	 *
	 * @param userName 用户名
	 * @return 用户对象信息
	 */
	@Override
	public SysUserDTO getUserByLoginName(String userName) {
		return userMapper.selectUserByLoginName(userName);
	}

	/**
	 * 通过手机号码查询用户
	 *
	 * @param phoneNumber 手机号码
	 * @return 用户对象信息
	 */
	@Override
	public SysUserDTO getUserByPhoneNumber(String phoneNumber) {
		return userMapper.selectUserByPhoneNumber(phoneNumber);
	}

	/**
	 * 通过邮箱查询用户
	 *
	 * @param email 邮箱
	 * @return 用户对象信息
	 */
	@Override
	public SysUserDTO getUserByEmail(String email) {
		return userMapper.selectUserByEmail(email);
	}

	/**
	 * 查询用户所属角色组
	 *
	 * @param userId 用户ID
	 * @return 结果
	 */
	@Override
	public String getUserRoleGroup(Long userId) {
		List<SysRoleDTO> list = roleMapper.selectRolesByUserId(userId);
		StringBuffer nameStr = new StringBuffer();
		for (SysRoleDTO role : list){
			nameStr.append(role.getRoleName()).append(",");
		}
		if (JeeStringUtils.isNotEmpty(nameStr.toString())){
			return nameStr.substring(0, nameStr.length() - 1);
		}
		return nameStr.toString();
	}

	/**
	 * 查询用户所属岗位组
	 *
	 * @param userId 用户ID
	 * @return 结果
	 */
	@Override
	public String getUserPostGroup(Long userId) {
		List<SysPostDTO> list = postMapper.selectPostsByUserId(userId);
		StringBuffer nameStr = new StringBuffer();
		for (SysPostDTO post : list){
			nameStr.append(post.getPostName()).append(",");
		}
		if (JeeStringUtils.isNotEmpty(nameStr.toString())){
			return nameStr.substring(0, nameStr.length() - 1);
		}
		return nameStr.toString();
	}

	/**
     * 新增用户
     * 
     * @param user 用户信息
     * @return 结果
     */
	@Override
	@Transactional
	public int insertUser(SysUserDTO user) {
		user.setCreateTime(LocalDateTime.now());
		// 新增用户信息
		int rows = userMapper.insertUser(user);
		// 新增用户岗位关联
		insertUserPost(user);
		// 新增用户与角色管理
		insertUserRole(user);
		return rows;
	}

	/**
	 * 用户授权角色
	 *
	 * @param userId 用户ID
	 * @param roleIds 角色组
	 */
	@Override
	@Transactional
	public void insertUserRoles(Long userId, Long[] roleIds){
		userRoleMapper.deleteUserRoleByUserId(userId);
		SysUserDTO user = new SysUserDTO();
		user.setUserId(userId);
		user.setRoleIds(roleIds);
		insertUserRole(user);
	}

	/**
     * 修改用户
     * 
     * @param user 用户信息
     * @return 结果
     */
	@Override
	public int updateUser(SysUserDTO user) {
		Long userId = user.getUserId();
		// 删除用户与角色关联
		userRoleMapper.deleteUserRoleByUserId(userId);
		// 新增用户与角色管理
		insertUserRole(user);
		// 删除用户与岗位关联
		userPostMapper.deleteUserPostByUserId(userId);
		// 新增用户与岗位管理
		insertUserPost(user);
		user.setUpdateTime(LocalDateTime.now());
		return userMapper.updateUser(user);
	}

	/**
	 * 修改用户个人详细信息
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	public int updateUserInfo(SysUserDTO user){
		user.setUpdateTime(LocalDateTime.now());
		return userMapper.updateUser(user);
	}

	/**
	 * 修改用户密码信息
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	public int updateUserPwd(SysUserDTO user) {
		return updateUserInfo(user);
	}

	/**
     * 删除用户对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteUserByIds(Long[] ids) {
		for (Long userId : ids){
			checkUserAllowed(new SysUserDTO(userId));
		}
		return userMapper.deleteUserByIds(ids);
	}

	/**
	 * 校验登录名称是否唯一
	 *
	 * @param loginName 用户名
	 * @return
	 */
	@Override
	public boolean checkLoginNameUnique(String loginName) {
		return userMapper.checkLoginNameUnique(loginName) > 0 ? false : true;
	}

	/**
	 * 校验手机号码是否唯一
	 *
	 * @param user 用户信息
	 * @return
	 */
	@Override
	public boolean checkPhoneUnique(SysUserDTO user) {
		Long userId = JeeStringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
		SysUserDTO info = userMapper.checkPhoneUnique(user.getPhonenumber());
		if (JeeStringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()){
			return false;
		}
		return true;
	}

	/**
	 * 校验email是否唯一
	 *
	 * @param user 用户信息
	 * @return
	 */
	@Override
	public boolean checkEmailUnique(SysUserDTO user) {
		Long userId = JeeStringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
		SysUserDTO info = userMapper.checkEmailUnique(user.getEmail());
		if (JeeStringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()){
			return false;
		}
		return true;
	}

	/**
	 * 校验用户是否允许操作
	 *
	 * @param user 用户信息
	 */
	@Override
	public void checkUserAllowed(SysUserDTO user){
		if (JeeStringUtils.isNotNull(user.getUserId()) && user.isAdmin()){
			throw new BusinessException("不允许操作超级管理员用户");
		}
	}


	/**
	 * 新增用户岗位信息
	 *
	 * @param user 用户对象
	 */
	private void insertUserPost(SysUserDTO user) {
		Long[] postIds = user.getPostIds();
		if (JeeStringUtils.isNotNull(postIds)){
			// 新增用户与岗位管理
			List<SysUserPost> list = new ArrayList<SysUserPost>();
			for (Long postId : postIds){
				SysUserPost up = new SysUserPost();
				up.setUserId(user.getUserId());
				up.setPostId(postId);
				list.add(up);
			}
			if (list.size() > 0){
				userPostMapper.insertBatchUserPost(list);
			}
		}
	}

	/**
	 * 新增用户角色信息
	 *
	 * @param user 用户对象
	 */
	private void insertUserRole(SysUserDTO user) {
		Long[] roleIds = user.getRoleIds();
		if (JeeStringUtils.isNotNull(roleIds)){
			// 新增用户与角色管理
			List<SysUserRole> list = new ArrayList<SysUserRole>();
			for (Long roleId : roleIds){
				SysUserRole up = new SysUserRole();
				up.setUserId(user.getUserId());
				up.setRoleId(roleId);
				list.add(up);
			}
			if (list.size() > 0){
				userRoleMapper.insertBatchUserRole(list);
			}
		}
	}
}
