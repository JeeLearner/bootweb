package com.jee.boot.shiro.utils;

import com.jee.boot.framework.verifycode.kaptcha.VerifyCodeContants;
import com.jee.boot.shiro.realm.UserRealm;
import com.jee.boot.common.utils.bean.JeeBeanUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.system.dto.SysUserDTO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 * shiro 工具类
 *
 * @author jeeLearner
 * @version V1.0
 */
public class ShiroUtils {

    public static String getVerifyCode(){
        Object code = getSession().getAttribute(VerifyCodeContants.KAPTCHA_SESSION_KEY);
        return code == null ? "" : String.valueOf(code);
    }

    public static Long getUserId(){
        return getSysUser().getUserId().longValue();
    }

    public static String getLoginName(){
        return getSysUser().getLoginName();
    }

    public static String getIp() {
        return getSession().getHost();
    }

    public static String getSessionId() {
        return String.valueOf(getSession().getId());
    }

    public static SysUserDTO getSysUser(){
        SysUserDTO user = null;
        Object obj = getSubject().getPrincipal();
        if (JeeStringUtils.isNotNull(obj)){
            user = new SysUserDTO();
            JeeBeanUtils.copyBeanProp(user, obj);
        }
        return user;

    }

    public static void setSysUser(SysUserDTO user) {
        Subject subject = getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realmName);
        // 重新加载Principal
        subject.runAs(newPrincipalCollection);
    }

    public static void clearCachedAuthorizationInfo() {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm realm = (UserRealm) rsm.getRealms().iterator().next();
        realm.clearCachedAuthorizationInfo();
    }

    public static void logout() {
        getSubject().logout();
    }

    public static Session getSession(){
        return getSubject().getSession();
    }

    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    /**
     * 生成随机盐
     */
    public static String randomSalt() {
        // 一个Byte占两个字节，此处生成的3字节，字符串长度为6
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        String hex = secureRandom.nextBytes(3).toHex();
        return hex;
    }
}

