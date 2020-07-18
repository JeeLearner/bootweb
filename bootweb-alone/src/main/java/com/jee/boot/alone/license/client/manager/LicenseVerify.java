package com.jee.boot.alone.license.client.manager;

import com.jee.boot.alone.license.client.model.LicenseVerifyParam;
import com.jee.boot.alone.license.server.creator.CustomKeyStoreParam;
import de.schlichtherle.license.*;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.MessageFormat;
import java.util.prefs.Preferences;

/**
 * License校验类
 *
 * @author jeeLearner
 * @version V1.0
 */
public class LicenseVerify {
    private static Logger logger = LoggerFactory.getLogger(LicenseVerify.class);

    /**
     * 安装License证书
     *      其实就是加载license.lic内容到系统中
     *      在项目启动时自动安装 && 提供拥护自助入口
     *
     * @param param
     * @return
     */
    public synchronized LicenseContent install(LicenseVerifyParam param) throws Exception {
        LicenseContent result = null;

        LicenseManager licenseManager = LicenseManagerHolder.getInstance(initLicenseParam(param));
        licenseManager.uninstall();

        result = licenseManager.install(new File(param.getLicensePath()));
        return result;
    }

    /**
     * 校验License证书
     * @author zifangsky
     * @date 2018/4/20 16:26
     * @since 1.0.0
     * @return boolean
     */
    public boolean verify(){
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);
        FastDateFormat formater = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

        try {
            LicenseContent licenseContent = licenseManager.verify();
            logger.info(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}",formater.format(licenseContent.getNotBefore()),formater.format(licenseContent.getNotAfter())));
            return true;
        }catch (Exception e){
            logger.error("证书校验失败！",e);
            return false;
        }
    }


    /**
     * 初始化证书生成参数
     *
     * @return
     */
    private LicenseParam initLicenseParam(LicenseVerifyParam param){
        Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);
        //设置对证书内容加密的秘钥
        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class
                ,param.getStorePath()
                ,param.getPublicAlias()
                ,param.getStorePass()
                ,null);
        LicenseParam licenseParam = new DefaultLicenseParam(param.getSubject()
                ,preferences
                ,publicStoreParam
                ,cipherParam);
        return licenseParam;
    }

}

