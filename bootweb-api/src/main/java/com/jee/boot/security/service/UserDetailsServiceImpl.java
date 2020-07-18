package com.jee.boot.security.service;

import com.jee.boot.common.enums.UserStatus;
import com.jee.boot.common.exception.base.BaseException;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.security.entity.LoginUser;
import com.jee.boot.system.dto.SysUserDTO;
import com.jee.boot.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 用户验证处理
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserDTO user = userService.getUserByLoginName(username);
        if (JeeStringUtils.isNull(user)){
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已停用");
        }
        return createLoginUser(user);
    }

    private UserDetails createLoginUser(SysUserDTO user) {
        return new LoginUser(user, permissionService.getMenuPermission(user));
    }
}

