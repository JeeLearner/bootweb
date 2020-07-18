package com.jee.boot.shiro.service;

import com.jee.boot.framework.service.ISysService;
import com.jee.boot.shiro.utils.ShiroUtils;
import com.jee.boot.system.dto.SysUserDTO;
import org.springframework.stereotype.Component;

/**
 * 系统服务 实现类
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class SysService implements ISysService {

    @Override
    public SysUserDTO getCurrentUser() {
        return ShiroUtils.getSysUser();
    }

    @Override
    public String getCurrentUserIp() {
        return ShiroUtils.getIp();
    }
}

