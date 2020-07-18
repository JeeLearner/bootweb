package com.jee.boot.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * BootWeb全局自定义配置项
 *      调用方式：BWProp.getName();
 *      注意：set方法不加static，否则无法获取正确值
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
@ConfigurationProperties(prefix = "bootweb")
public class BWProp {
    /**
     * 项目名称
     */
    private static String name;

    /**
     * 版本
     */
    private static String version;

    /**
     * 版权年份
     */
    private static String copyrightYear;

    /**
     * 实例演示开关
     */
    private static boolean demoEnabled;

    /**
     * 获取地址开关
     */
    private static boolean addressEnabled;

    /**
     * 文件上传路径
     */
    private static String profile;

    /**
     * 头像上传路径
     */
    private static String avatarPath;

    /**
     * 下载路径
     */
    private static String downloadPath;

    /**
     * 上传路径
     */
    private static String uploadPath;


    public static String getName() {
        return name;
    }

    public void setName(String name) {
        BWProp.name = name;
    }

    public static String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        BWProp.version = version;
    }

    public static String getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear) {
        BWProp.copyrightYear = copyrightYear;
    }

    public static boolean isDemoEnabled() {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled) {
        BWProp.demoEnabled = demoEnabled;
    }

    public static boolean isAddressEnabled() {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        BWProp.addressEnabled = addressEnabled;
    }

    public static String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        BWProp.profile = profile;
    }

    public static String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        BWProp.avatarPath = avatarPath;
    }

    public static String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        BWProp.downloadPath = downloadPath;
    }

    public static String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        BWProp.uploadPath = uploadPath;
    }
}

