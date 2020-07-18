package com.jee.boot.system.mapper;

import com.jee.boot.system.dto.SysUserDTO;
import com.jee.boot.system.domain.SysUser;
import java.util.List;	

/**
 * 用户 数据层
 * 
 * @author jeeLearner
 * @version V1.0
 */
public interface SysUserMapper {

	/**
     * 查询用户列表
     * 
     * @param user 用户信息
     * @return 用户集合
     */
	List<SysUserDTO> selectUserList(SysUser user);

	/**
	 * 查询未已配用户角色的用户列表
	 *
	 * @param user 用户信息
	 * @return 用户信息集合信息
	 */
	List<SysUserDTO> selectAllocatedList(SysUser user);

	/**
	 * 查询未分配用户角色的用户列表
	 *
	 * @param user 用户信息
	 * @return 用户信息集合信息
	 */
	List<SysUserDTO> selectUnallocatedList(SysUser user);

	/**
	 * 查询用户信息
	 *
	 * @param userId 用户ID
	 * @return 用户信息
	 */
	SysUserDTO selectUserById(Long userId);

	/**
	 * 通过用户名查询用户
	 *
	 * @param userName 用户名
	 * @return 用户对象信息
	 */
	SysUserDTO selectUserByLoginName(String userName);
	/**
	 * 通过手机号码查询用户
	 *
	 * @param phoneNumber 手机号码
	 * @return 用户对象信息
	 */
	SysUserDTO selectUserByPhoneNumber(String phoneNumber);

	/**
	 * 通过邮箱查询用户
	 *
	 * @param email 邮箱
	 * @return 用户对象信息
	 */
	SysUserDTO selectUserByEmail(String email);

	/**
     * 新增用户
     * 
     * @param user 用户信息
     * @return 结果
     */
	int insertUser(SysUser user);
	
	/**
     * 修改用户
     * 
     * @param user 用户信息
     * @return 结果
     */
	int updateUser(SysUser user);
	
	/**
     * 删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
	int deleteUserById(Long userId);
	
	/**
     * 批量删除用户
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
	int checkLoginNameUnique(String loginName);

	/**
	 * 校验手机号码是否唯一
	 *
	 * @param phonenumber 手机号码
	 * @return 结果
	 */
	SysUserDTO checkPhoneUnique(String phonenumber);

	/**
	 * 校验email是否唯一
	 *
	 * @param email 用户邮箱
	 * @return 结果
	 */
	SysUserDTO checkEmailUnique(String email);
}