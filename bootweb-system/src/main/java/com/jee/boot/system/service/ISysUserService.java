package com.jee.boot.system.service;

import com.jee.boot.system.domain.SysUserRole;
import com.jee.boot.system.dto.SysUserDTO;
import java.util.List;

/**
 * 用户 服务层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface ISysUserService {

	/**
	 * 导入用户数据
	 *
	 * @param userList 用户数据列表
	 * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
	 * @param operName 操作用户
	 * @return 结果
	 */
	String importUser(List<SysUserDTO> userList, boolean isUpdateSupport, String operName);

	/**
     * 查询用户列表
     * 
     * @param user 用户信息
     * @return 用户集合
     */
	List<SysUserDTO> listUser(SysUserDTO user);

	/**
	 * 查询已分配用户角色的用户列表
	 *
	 * @param user 用户信息
	 * @return 用户信息集合信息
	 */
	List<SysUserDTO> listAllocatedRole(SysUserDTO user);

	/**
	 * 根据条件分页查询未分配用户角色列表
	 *
	 * @param user 用户信息
	 * @return 用户信息集合信息
	 */
	List<SysUserDTO> listUnallocatedRole(SysUserDTO user);

	/**
	 * 通过用户ID查询用户和角色关联
	 *
	 * @param userId 用户ID
	 * @return 用户和角色关联列表
	 */
	List<SysUserRole> listUserRoleByUserId(Long userId);

	/**
	 * 查询用户信息
	 *
	 * @param userId 用户ID
	 * @return 用户信息
	 */
	SysUserDTO getUserById(Long userId);

	/**
	 * 通过用户名查询用户
	 *
	 * @param userName 用户名
	 * @return 用户对象信息
	 */
	SysUserDTO getUserByLoginName(String userName);
	/**
	 * 通过手机号码查询用户
	 *
	 * @param phoneNumber 手机号码
	 * @return 用户对象信息
	 */
	SysUserDTO getUserByPhoneNumber(String phoneNumber);

	/**
	 * 通过邮箱查询用户
	 *
	 * @param email 邮箱
	 * @return 用户对象信息
	 */
	SysUserDTO getUserByEmail(String email);

	/**
	 * 根据用户ID查询用户所属角色组
	 *
	 * @param userId 用户ID
	 * @return 结果
	 */
	String getUserRoleGroup(Long userId);

	/**
	 * 根据用户ID查询用户所属岗位组
	 *
	 * @param userId 用户ID
	 * @return 结果
	 */
	String getUserPostGroup(Long userId);
	
	/**
     * 新增用户
     * 
     * @param user 用户信息
     * @return 结果
     */
	int insertUser(SysUserDTO user);

	/**
	 * 用户授权角色
	 *
	 * @param userId 用户ID
	 * @param roleIds 角色组
	 */
	void insertUserRoles(Long userId, Long[] roleIds);
	
	/**
     * 修改用户
     * 
     * @param user 用户信息
     * @return 结果
     */
	int updateUser(SysUserDTO user);

	/**
	 * 修改用户详细信息
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	int updateUserInfo(SysUserDTO user);

	/**
	 * 修改用户密码信息
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	int updateUserPwd(SysUserDTO user);

	/**
     * 删除用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteUserByIds(Long[] ids);

	/**
	 * 校验用户名称是否唯一
	 *
	 * @param loginName 登录名称
	 * @return 结果
	 */
	boolean checkLoginNameUnique(String loginName);

	/**
	 * 校验手机号码是否唯一
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	boolean checkPhoneUnique(SysUserDTO user);

	/**
	 * 校验email是否唯一
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	boolean checkEmailUnique(SysUserDTO user);

	/**
	 * 校验用户是否允许操作
	 *
	 * @param user 用户信息
	 */
	void checkUserAllowed(SysUserDTO user);
}
