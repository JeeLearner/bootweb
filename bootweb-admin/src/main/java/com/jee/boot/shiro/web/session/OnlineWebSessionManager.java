package com.jee.boot.shiro.web.session;

import com.jee.boot.common.constant.SysConstants;
import com.jee.boot.shiro.session.OnlineSession;
import com.jee.boot.common.utils.DateUtils;
import com.jee.boot.common.utils.bean.JeeBeanUtils;
import com.jee.boot.common.utils.spring.SpringUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.system.domain.SysUserOnline;
import com.jee.boot.system.dto.SysUserOnlineDTO;
import com.jee.boot.system.service.ISysUserOnlineService;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * 主要是在此如果会话的属性修改了 就标识下其修改了 然后方便 OnlineSessionDao同步
 * @author jeeLearner
 * @version V1.0
 */
public class OnlineWebSessionManager extends DefaultWebSessionManager {
    private static final Logger log = LoggerFactory.getLogger(OnlineWebSessionManager.class);

    @Override
    public void setAttribute(SessionKey sessionKey, Object attributeKey, Object value) throws InvalidSessionException{
        super.setAttribute(sessionKey, attributeKey, value);
        if (value != null && needMarkAttributeChanged(attributeKey)){
            OnlineSession session = getOnlineSession(sessionKey);
            session.markAttributeChanged();
        }
    }

    @Override
    public Object removeAttribute(SessionKey sessionKey, Object attributeKey) throws InvalidSessionException{
        Object removed = super.removeAttribute(sessionKey, attributeKey);
        if (removed != null){
            OnlineSession s = getOnlineSession(sessionKey);
            s.markAttributeChanged();
        }
        return removed;
    }

    /**
     * 验证session是否有效 用于删除过期session
     */
    @Override
    public void validateSessions(){
        if (log.isInfoEnabled()){
            log.info("invalidation sessions...");
        }

        int invalidCount = 0;

        int timeout = (int) this.getGlobalSessionTimeout();
        //当前6:30:00  expiredDate 负数 6:20:00  过期的是：<=6:20:00
        LocalDateTime expiredDate = LocalDateTime.now().minusSeconds(timeout/1000);
        ISysUserOnlineService userOnlineService = SpringUtils.getBean(ISysUserOnlineService.class);
        List<SysUserOnlineDTO> userOnlineList = userOnlineService.listOnlineByExpired(expiredDate);
        // 批量过期删除
        List<String> needOfflineIdList = new ArrayList<String>();
        for (SysUserOnline userOnline : userOnlineList){
            try {
                SessionKey key = new DefaultSessionKey(userOnline.getSessionId());
                Session session = retrieveSession(key);
                if (session != null){
                    throw new InvalidSessionException();
                }
            } catch (InvalidSessionException e){
                if (log.isDebugEnabled()){
                    boolean expired = (e instanceof ExpiredSessionException);
                    String msg = "Invalidated session with id [" + userOnline.getSessionId() + "]"
                            + (expired ? " (expired)" : " (stopped)");
                    log.debug(msg);
                }
                invalidCount++;
                needOfflineIdList.add(userOnline.getSessionId());
            }
        }
        if (needOfflineIdList.size() > 0){
            try {
                userOnlineService.deleteUserOnlineByIds(needOfflineIdList.toArray(new String[0]));
            } catch (Exception e){
                log.error("batch delete db session error.", e);
            }
        }
        if (log.isInfoEnabled()){
            String msg = "Finished invalidation session.";
            if (invalidCount > 0){
                msg += " [" + invalidCount + "] sessions were stopped.";
            } else {
                msg += " No sessions were stopped.";
            }
            log.info(msg);
        }
    }

    @Override
    protected Collection<Session> getActiveSessions() {
        throw new UnsupportedOperationException("getActiveSessions method not supported");
    }


    private boolean needMarkAttributeChanged(Object attributeKey) {
        if (attributeKey == null){
            return false;
        }
        String attributeKeyStr = attributeKey.toString();
        // 优化 flash属性没必要持久化
        if (attributeKeyStr.startsWith("org.springframework")){
            return false;
        }
        if (attributeKeyStr.startsWith("javax.servlet")){
            return false;
        }
        if (attributeKeyStr.equals(SysConstants.CURRENT_USERNAME)){
            return false;
        }
        return true;
    }

    private OnlineSession getOnlineSession(SessionKey sessionKey) {
        OnlineSession session = null;
        Object obj = doGetSession(sessionKey);
        if (JeeStringUtils.isNotNull(obj)){
            session = new OnlineSession();
            JeeBeanUtils.copyBeanProp(session, obj);
        }
        return session;
    }

}

