package com.jee.boot.alone.license.server.controller;

import com.jee.boot.alone.license.server.config.LicenseServerProp;
import com.jee.boot.alone.license.server.creator.LicenseCreator;
import com.jee.boot.alone.license.server.creator.LicenseCreatorParam;
import com.jee.boot.alone.license.server.model.LicenseServerParam;
import com.jee.boot.common.core.result.R;
import com.jee.boot.common.utils.bean.JeeBeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;

/**
 * license控制器
 *      不能放在给客户部署的代码里
 *
 * @author jeeLearner
 * @version V1.0
 */
@RestController
@RequestMapping("/license/server")
public class LicenseServerController {

    @Autowired
    LicenseServerProp serverProp;

    @InitBinder
    public void initBinder(WebDataBinder binder){
        FastDateFormat formater = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport(){
            @Override
            public void setAsText(String text) {
                try {
                    setValue(formater.parse(text));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 生成证书文件
     *  {
     *     "subject":"sub-flowmonitor",
     *     "privateAlias":"privateKey",
     *     "keyPass":"5T7Zz5Y0dJFcqTxvzkH5LDGJJSGMzQ",
     *     "storePass":"3538cef8e7",
     *     "licensePath":"E:/license/license.lic",
     *     "privateKeysStorePath":"E:/license/privateKeys.keystore",
     *     "issuedTime":"2020-01-01 00:00:00",
     *     "expiryTime":"2020-12-31 00:00:00",
     *     "consumerType":"User",
     *     "consumerAmount":1,
     *     "description":"此证书用于XX系统的license授权",
     *     "licenseCheckModel":{
     *         "ipAddress":[
     *             "192.168.33.2",
     *             "10.131.6.132"],
     *         "macAddress":[
     *             "00-13-EF-2F-0D-27",
     *             "50-7B-9D-F9-18-41"],
     *         "cpuSerial":"BFEBFBFF000306A9",
     *         "mainBoardSerial":"WB13252230"
     *     }
     * }
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/generateLicense")
    public R generateLicense(@RequestBody(required = true) LicenseServerParam param) {
        LicenseCreatorParam creatorParam = new LicenseCreatorParam();
        //yml配置
        creatorParam.setSubject(serverProp.getSubject());
        creatorParam.setPrivateAlias(serverProp.getPrivateAlias());
        creatorParam.setKeyPass(serverProp.getKeyPass());
        creatorParam.setStorePass(serverProp.getStorePass());
        creatorParam.setLicensePath(serverProp.getLicensePath());
        creatorParam.setPrivateKeysStorePath(serverProp.getPrivateKeysStorePath());
        creatorParam.setConsumerType(serverProp.getConsumerType());
        creatorParam.setConsumerAmount(serverProp.getConsumerAmount());
        creatorParam.setDescription(serverProp.getDescription());
        //用户输入参数
        creatorParam.setIssuedTime(param.getIssuedTime());
        creatorParam.setExpireTime(param.getExpireTime());
        creatorParam.setLicenseHardwareCheck(param.getLicenseHardwareCheck());

        LicenseCreator licenseCreator = new LicenseCreator(creatorParam);
        boolean result = licenseCreator.generateLicense();
        if (result){
            return R.ok().data("param", param);
        } else {
            return R.error().msg("证书文件生成失败！");
        }
    }
}

