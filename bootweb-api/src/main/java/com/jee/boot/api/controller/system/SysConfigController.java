package com.jee.boot.api.controller.system;

import com.jee.boot.common.constant.UserConstants;
import com.jee.boot.common.core.page.PageUtil;
import com.jee.boot.common.core.result.R;
import com.jee.boot.common.core.web.BaseController;
import com.jee.boot.framework.cacheservice.ICacheConfigService;
import com.jee.boot.system.dto.SysConfigDTO;
import com.jee.boot.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 参数配置 控制器
 *
 * @author jeeLearner
 * @version V1.0
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {

    @Autowired
    ISysConfigService configService;
    @Autowired
    private ICacheConfigService cacheConfigService;

    /**
     * 查询参数配置列表
     */
    @GetMapping("/list")
    public R list(SysConfigDTO config){
        return PageUtil.page(() -> {
            return configService.listConfig(config);
        });
    }

    /**
     * 根据参数编号获取详细信息
     */
    @GetMapping(value = "/info/{configId}")
    public R getInfo(@PathVariable Long configId) {
        SysConfigDTO dto = configService.getConfigById(configId);
        return info(dto);
    }

    /**
     * 新增参数配置
     */
    @PostMapping(value = "/add")
    public R add(@Validated @RequestBody SysConfigDTO config){
        boolean result = configService.checkConfigKeyUnique(config);
        if (!result){
            return R.error().msg("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy("");
        return res(cacheConfigService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PutMapping(value = "edit")
    public R edit(@Validated @RequestBody SysConfigDTO config){
        boolean result = configService.checkConfigKeyUnique(config);
        if (!result){
            return R.error().msg("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy("");
        return res(cacheConfigService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @DeleteMapping("/remove{ids}")
    public R remove(@PathVariable Long[] ids) {
        return res(cacheConfigService.deleteConfigByIds(ids));
    }

    /**
     * 清空缓存
     */
    @DeleteMapping("/clearCache")
    public R clearCache(){
        cacheConfigService.clearCache();
        return R.ok();
    }
}

