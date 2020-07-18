package com.jee.boot.admin.controller.system;

import com.jee.boot.common.annotation.Log;
import com.jee.boot.common.config.BWProp;
import com.jee.boot.common.core.result.R;
import com.jee.boot.common.core.web.BaseController;
import com.jee.boot.common.enums.BusinessType;
import com.jee.boot.common.utils.file.FileUploadUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.shiro.service.SysPasswordService;
import com.jee.boot.shiro.utils.ShiroUtils;
import com.jee.boot.system.dto.SysUserDTO;
import com.jee.boot.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jeeLearner
 * @version V1.0
 */
@Controller
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(SysProfileController.class);

    private String prefix = "system/user/profile";

    @Autowired
    private ISysUserService userService;
    @Autowired
    private SysPasswordService passwordService;

    /**
     * 个人信息
     */
    @GetMapping()
    public String profile(ModelMap mmap){
        SysUserDTO user = ShiroUtils.getSysUser();
        mmap.put("user", user);
        mmap.put("roleGroup", userService.getUserRoleGroup(user.getUserId()));
        mmap.put("postGroup", userService.getUserPostGroup(user.getUserId()));
        return prefix + "/profile";
    }


    /** =========================修改密码======================== */
    @GetMapping("/resetPwd")
    public String resetPwd(ModelMap mmap){
        SysUserDTO user = ShiroUtils.getSysUser();
        mmap.put("user", userService.getUserById(user.getUserId()));
        return prefix + "/resetPwd";
    }

    @GetMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password){
        SysUserDTO user = ShiroUtils.getSysUser();
        if (passwordService.matches(user, password)){
            return true;
        }
        return false;
    }

    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public R resetPwd(String oldPassword, String newPassword){
        SysUserDTO user = ShiroUtils.getSysUser();
        if (JeeStringUtils.isNotEmpty(newPassword) && passwordService.matches(user, oldPassword)){
            user.setSalt(ShiroUtils.randomSalt());
            user.setPassword(passwordService.encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
            if (userService.updateUserPwd(user) > 0){
                ShiroUtils.setSysUser(userService.getUserById(user.getUserId()));
                return R.ok();
            }
            return R.error();
        } else {
            return R.error().msg("修改密码失败，旧密码错误");
        }
    }


    /** ========================= 修改密码 ======================== */
    /**
     * 修改用户
     */
    @GetMapping("/edit")
    public String edit(ModelMap mmap) {
        SysUserDTO user = ShiroUtils.getSysUser();
        mmap.put("user", userService.getUserById(user.getUserId()));
        return prefix + "/edit";
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public R update(SysUserDTO user){
        SysUserDTO currentUser = ShiroUtils.getSysUser();
        currentUser.setUserName(user.getUserName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setSex(user.getSex());
        if (userService.updateUserInfo(currentUser) > 0) {
            ShiroUtils.setSysUser(userService.getUserById(currentUser.getUserId()));
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改头像
     */
    @GetMapping("/avatar")
    public String avatar(ModelMap mmap){
        SysUserDTO user = ShiroUtils.getSysUser();
        mmap.put("user", userService.getUserById(user.getUserId()));
        return prefix + "/avatar";
    }

    /**
     * 保存头像
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateAvatar")
    @ResponseBody
    public R updateAvatar(@RequestParam("avatarfile") MultipartFile file){
        SysUserDTO currentUser = ShiroUtils.getSysUser();
        try {
            if (!file.isEmpty()){
                String avatar = FileUploadUtils.upload(BWProp.getAvatarPath(), file);
                currentUser.setAvatar(avatar);
                if (userService.updateUserInfo(currentUser) > 0){
                    ShiroUtils.setSysUser(userService.getUserById(currentUser.getUserId()));
                    return R.ok();
                }
            }
            return R.error();
        } catch (Exception e){
            log.error("修改头像失败！", e);
            return R.error().msg(e.getMessage());
        }
    }

}

