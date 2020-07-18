package com.jee.boot.shiro.service;

import com.jee.boot.framework.manager.factory.AsyncFactory;
import com.jee.boot.shiro.cache.CacheConstant;
import com.jee.boot.shiro.manager.factory.ShiroAsyncFactory;
import com.jee.boot.common.constant.Constants;
import com.jee.boot.common.exception.user.UserPasswordNotMatchException;
import com.jee.boot.common.exception.user.UserPasswordRetryLimitExceedException;
import com.jee.boot.common.utils.MessageUtils;
import com.jee.boot.framework.manager.AsyncManager;
import com.jee.boot.system.dto.SysUserDTO;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 登录密码方法
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class SysPasswordService {

    @Autowired
    private CacheManager cacheManager;

    private Cache<String, AtomicInteger> loginRecordCache;

    @Value(value = "${user.password.maxRetryCount}")
    private String maxRetryCount;

    @PostConstruct
    public void init(){
        loginRecordCache = cacheManager.getCache(CacheConstant.LOGIN_RECORD_CACHE);
    }

    public void validate(SysUserDTO user, String password){
        String loginName = user.getLoginName();

        AtomicInteger retryCount = loginRecordCache.get(loginName);

        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            loginRecordCache.put(loginName, retryCount);
        }
        if (retryCount.incrementAndGet() > Integer.valueOf(maxRetryCount).intValue()) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount)));
            throw new UserPasswordRetryLimitExceedException(Integer.valueOf(maxRetryCount).intValue());
        }

        if (!matches(user, password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.count", retryCount)));
            loginRecordCache.put(loginName, retryCount);
            throw new UserPasswordNotMatchException();
        } else {
            clearLoginRecordCache(loginName);
        }
    }

    public boolean matches(SysUserDTO user, String newPassword){
        return user.getPassword().equals(encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
    }

    public String encryptPassword(String username, String password, String salt){
        return new Md5Hash(username + password + salt).toHex();
    }

    public void clearLoginRecordCache(String username){
        loginRecordCache.remove(username);
    }

    /**
     * 解锁用户
     *      登录缓存中删除loginName
     * @param loginName
     */
    public void unlock(String loginName) {
        loginRecordCache.remove(loginName);
    }

    public static void main(String[] args) {
        String newPassword = "123456";
        String pass = new Md5Hash("admin" + newPassword + "111111").toHex();
        System.out.println(pass);

    }
}

