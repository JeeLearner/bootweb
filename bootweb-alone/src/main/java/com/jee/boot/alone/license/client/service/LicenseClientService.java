package com.jee.boot.alone.license.client.service;

import com.jee.boot.alone.license.client.config.LicenseClientProp;
import com.jee.boot.alone.license.client.manager.LicenseVerify;
import com.jee.boot.alone.license.client.model.LicenseClientHardwareCheck;
import com.jee.boot.alone.license.client.model.LicenseVerifyParam;
import com.jee.boot.alone.license.client.support.AbstractServerInfos;
import com.jee.boot.alone.license.client.support.LinuxServerInfos;
import com.jee.boot.alone.license.client.support.WindowsServerInfos;
import com.jee.boot.common.core.result.R;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * 客户端license服务
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class LicenseClientService {
    private static final Logger logger = LoggerFactory.getLogger(LicenseClientService.class);

    @Autowired
    LicenseClientProp clientProp;

    FastDateFormat formater = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    /**
     * 安装证书
     */
    public R install(MultipartFile file){
        if (StringUtils.isNotBlank(clientProp.getLicensePath())){
            //如果用户上传了自己的证书，则覆盖原证书
            if (file != null && !file.isEmpty()){
                try {
                    File desc = new File(clientProp.getLicensePath());
                    if (!desc.getParentFile().exists()){
                        desc.getParentFile().mkdirs();
                    }
                    file.transferTo(new File(clientProp.getLicensePath()));
                } catch (IOException e) {
                    return R.error().msg("证书安装失败！==> 证书上传出现错误！");
                }
            }

            logger.info("++++++++ 开始安装证书 ++++++++");
            LicenseVerifyParam param = new LicenseVerifyParam();
            param.setSubject(clientProp.getSubject());
            param.setPublicAlias(clientProp.getPublicAlias());
            param.setStorePass(clientProp.getStorePass());
            param.setLicensePath(clientProp.getLicensePath());
            param.setStorePath(clientProp.getStorePath());
            LicenseVerify licenseVerify = new LicenseVerify();
            //安装证书
            try {
                LicenseContent result = licenseVerify.install(param);
                logger.info(MessageFormat.format("证书安装成功，证书有效期：{0} - {1}",formater.format(result.getNotBefore()),formater.format(result.getNotAfter())));
            } catch (Exception e) {
                logger.error("证书安装失败！", e);
                e.printStackTrace();
                return R.error().msg("证书安装失败！==> license安装发生错误！");
            }
            logger.info("++++++++ 证书安装结束 ++++++++");
            return R.ok();
        } else {
            return R.error().msg("证书安装失败！==> license文件路径未配置！");
        }
    }
}

