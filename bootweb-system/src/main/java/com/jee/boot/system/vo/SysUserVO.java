package com.jee.boot.system.vo;

import com.jee.boot.common.core.domain.BaseEntity;
import com.jee.boot.system.dto.SysDeptDTO;
import com.jee.boot.system.dto.SysRoleDTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 用户 VO
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysUserVO  extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;
    /** 部门ID */
    private Long deptId;
    /** 登录账号 */
    private String loginName;
    /** 用户昵称 */
    private String userName;
    /** 用户类型（00系统用户） */
    private String userType;
    /** 用户邮箱 */
    private String email;
    /** 手机号码 */
    private String phonenumber;
    /** 用户性别（0男 1女 2未知） */
    private String sex;
    /** 头像路径 */
    private String avatar;
    /** 帐号状态（0正常 1停用） */
    private String status;
    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
    /** 最后登陆IP */
    private String loginIp;
    /** 最后登陆时间 */
    private LocalDateTime loginDate;

    /** 部门父ID */
    private Long parentId;

    /** 角色ID */
    private Long roleId;

    /** 部门对象 */
    private SysDeptDTO dept;

    private List<SysRoleDTO> roles;

    /** 角色组 */
    private Long[] roleIds;

    /** 岗位组 */
    private Long[] postIds;


    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public SysDeptDTO getDept() {
        return dept;
    }

    public void setDept(SysDeptDTO dept) {
        this.dept = dept;
    }

    public List<SysRoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRoleDTO> roles) {
        this.roles = roles;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds() {
        return postIds;
    }

    public void setPostIds(Long[] postIds) {
        this.postIds = postIds;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public Long getUserId(){
        return userId;
    }
    public void setDeptId(Long deptId){
        this.deptId = deptId;
    }

    public Long getDeptId(){
        return deptId;
    }
    public void setLoginName(String loginName){
        this.loginName = loginName;
    }

    public String getLoginName(){
        return loginName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
    }
    public void setUserType(String userType){
        this.userType = userType;
    }

    public String getUserType(){
        return userType;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }
    public void setPhonenumber(String phonenumber){
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber(){
        return phonenumber;
    }
    public void setSex(String sex){
        this.sex = sex;
    }

    public String getSex(){
        return sex;
    }
    public void setAvatar(String avatar){
        this.avatar = avatar;
    }

    public String getAvatar(){
        return avatar;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public String getDelFlag(){
        return delFlag;
    }
    public void setLoginIp(String loginIp){
        this.loginIp = loginIp;
    }

    public String getLoginIp(){
        return loginIp;
    }
    public void setLoginDate(LocalDateTime loginDate){
        this.loginDate = loginDate;
    }

    public LocalDateTime getLoginDate(){
        return loginDate;
    }

}
