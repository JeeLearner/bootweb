package com.jee.boot.framework.service;

import com.jee.boot.system.dto.SysUserDTO;

/**
 * 系统服务 接口
 *
 * @author jeeLearner
 * @version V1.0
 */
public interface ISysService {

    /**
     * 获取当前用户
     * @return
     */
    SysUserDTO getCurrentUser();

    /**
     * 获取当前用户的ip
     * @return
     */
    String getCurrentUserIp();
}

