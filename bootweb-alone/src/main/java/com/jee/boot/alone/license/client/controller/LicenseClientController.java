package com.jee.boot.alone.license.client.controller;

import com.jee.boot.alone.license.client.model.LicenseClientHardwareCheck;
import com.jee.boot.alone.license.client.service.LicenseClientService;
import com.jee.boot.alone.license.client.utils.ServerInfoUtils;
import com.jee.boot.common.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * license客户端控制器
 *      放在给客户部署的代码里
 *
 * @author jeeLearner
 * @version V1.0
 */
@RestController
@RequestMapping("/license/client")
public class LicenseClientController {

    @Autowired
    LicenseClientService clientService;

    /**
     * 获取服务器硬件信息
     * @param osName
     * @return
     */
    @GetMapping(value = "/getServerInfos")
    public R getServerInfos(@RequestParam(value = "osName",required = false) String osName) {
        LicenseClientHardwareCheck infos = ServerInfoUtils.getHardwareInfo(osName);
        return R.ok().data("info",infos);
    }

    /**
     * 安装证书
     * @param osName
     * @return
     */
    @PostMapping(value = "/install")
    public R install(@RequestParam(value = "osName",required = false) String osName
            , @RequestParam(value = "file", required = false) MultipartFile file) {
        return clientService.install(file);
    }
}

