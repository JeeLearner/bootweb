package com.jee.boot.admin.controller.common;

import com.jee.boot.common.config.BWProp;
import com.jee.boot.common.core.result.R;
import com.jee.boot.shiro.cache.service.CacheConfigService;
import com.jee.boot.shiro.utils.ShiroUtils;
import com.jee.boot.system.dto.SysMenuDTO;
import com.jee.boot.system.dto.SysUserDTO;
import com.jee.boot.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 首页 控制器
 *
 * @author jeeLearner
 * @version V1.0
 */
@Controller
public class SysIndexController {

    @Autowired
    ISysMenuService menuService;
    @Autowired
    CacheConfigService cacheConfigService;

    /**
     * 系统首页
     */
    @GetMapping("/index")
    public String index(ModelMap mmap){
        // 取身份信息
        SysUserDTO user = ShiroUtils.getSysUser();
        // 根据用户id取出菜单
        List<SysMenuDTO> menus = menuService.listMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("sideTheme", cacheConfigService.getConfigByKey("sys.index.sideTheme"));
        mmap.put("skinName", cacheConfigService.getConfigByKey("sys.index.skinName"));
        mmap.put("copyrightYear", BWProp.getCopyrightYear());
        mmap.put("demoEnabled", BWProp.isDemoEnabled());
        return "index";
    }

    /**
     * 切换主题
     */
    @GetMapping("/system/switchSkin")
    public String switchSkin(){
        return "skin";
    }

    /**
     * 系统介绍
     */
    @GetMapping("/system/main")
    public String main(ModelMap mmap){
        mmap.put("version", BWProp.getVersion());
        return "main";
    }
}

