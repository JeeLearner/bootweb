package com.jee.boot.system.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jee.boot.common.core.domain.BaseEntity;
                                        import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
                                                    
/**
 * 在线用户记录 VO对象 sys_user_online
 *
 * @author jeeLearner
 * @version V1.0
 */
public class SysUserOnlineVO extends BaseEntity implements Serializable {
private static final long serialVersionUID = 1L;

        /** 用户会话id */
    private String sessionId;
            /** 登录账号 */
    private String loginName;
            /** 部门名称 */
    private String deptName;
            /** 登录IP地址 */
    private String ipaddr;
            /** 登录地点 */
    private String loginLocation;
            /** 浏览器类型 */
    private String browser;
            /** 操作系统 */
    private String os;
            /** 在线状态on_line在线off_line离线 */
    private String status;
            /** session创建时间 */
    private LocalDateTime startTimestamp;
            /** session最后访问时间 */
    private LocalDateTime lastAccessTime;
            /** 超时时间，单位为分钟 */
    private Integer expireTime;
    

        public void setSessionId(String sessionId){
            this.sessionId = sessionId;
            }

    public String getSessionId(){
            return sessionId;
            }
            public void setLoginName(String loginName){
            this.loginName = loginName;
            }

    public String getLoginName(){
            return loginName;
            }
            public void setDeptName(String deptName){
            this.deptName = deptName;
            }

    public String getDeptName(){
            return deptName;
            }
            public void setIpaddr(String ipaddr){
            this.ipaddr = ipaddr;
            }

    public String getIpaddr(){
            return ipaddr;
            }
            public void setLoginLocation(String loginLocation){
            this.loginLocation = loginLocation;
            }

    public String getLoginLocation(){
            return loginLocation;
            }
            public void setBrowser(String browser){
            this.browser = browser;
            }

    public String getBrowser(){
            return browser;
            }
            public void setOs(String os){
            this.os = os;
            }

    public String getOs(){
            return os;
            }
            public void setStatus(String status){
            this.status = status;
            }

    public String getStatus(){
            return status;
            }
            public void setStartTimestamp(LocalDateTime startTimestamp){
            this.startTimestamp = startTimestamp;
            }

    public LocalDateTime getStartTimestamp(){
            return startTimestamp;
            }
            public void setLastAccessTime(LocalDateTime lastAccessTime){
            this.lastAccessTime = lastAccessTime;
            }

    public LocalDateTime getLastAccessTime(){
            return lastAccessTime;
            }
            public void setExpireTime(Integer expireTime){
            this.expireTime = expireTime;
            }

    public Integer getExpireTime(){
            return expireTime;
            }
            }
