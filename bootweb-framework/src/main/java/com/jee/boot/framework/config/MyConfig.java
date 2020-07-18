package com.jee.boot.framework.config;

import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.common.utils.YamlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局配置类
 *
 * @author jeeLearner
 */
public class MyConfig {

    private static final Logger log = LoggerFactory.getLogger(MyConfig.class);

    private static String NAME = "application.yml";
    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = new HashMap<String, String>();

    /**
     * 当前对象实例
     */
    private static MyConfig global;

    /**
     * 静态工厂方法
     */
    public static synchronized MyConfig getInstance(){
        if(global == null){
            global = new MyConfig();
        }
        return global;
    }

    private MyConfig() {
    }


    /**
     * 获取配置
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null) {
            Map<?, ?> yamlMap = null;
            try {
                yamlMap = YamlUtil.loadYaml(NAME);
                value = String.valueOf(YamlUtil.getProperty(yamlMap, key));
                map.put(key, value != null ? value : JeeStringUtils.EMPTY);
            }
            catch (FileNotFoundException e) {
                log.error("获取全局配置异常 {}", key);
            }
        }
        return value;
    }


    /**
     * 获取项目名称
     */
    public static String getName() {
        return JeeStringUtils.nvl(getConfig("bootweb.name"), "bootweb");
    }
    /**
     * 获取项目版本
     */
    public static String getVersion() {
        return JeeStringUtils.nvl(getConfig("bootweb.version"), "1.0.0");
    }

    /**
     * 获取版权年份
     */
    public static String getCopyrightYear() {
        return JeeStringUtils.nvl(getConfig("bootweb.copyrightYear"), "2019");
    }

    /**
     * 实例演示开关
     */
    public static String isDemoEnabled() {
        return JeeStringUtils.nvl(getConfig("bootweb.demoEnabled"), "true");
    }

    /**
     * 获取ip地址开关
     */
    public static Boolean isAddressEnabled() {
        return Boolean.valueOf(JeeStringUtils.nvl(getConfig("bootweb.addressEnabled"), "true"));
    }

    /**
     * 获取文件上传路径
     */
    public static String getProfile() {
        return getConfig("bootweb.profile");
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getConfig("bootweb.profile") + "avatar/";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getConfig("bootweb.profile") + "download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getConfig("bootweb.profile") + "upload/";
    }
}

