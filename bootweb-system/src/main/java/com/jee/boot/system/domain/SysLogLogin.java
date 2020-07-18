package com.jee.boot.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jee.boot.common.core.domain.BaseEntity;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 系统访问记录 DO对象 sys_log_login
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class SysLogLogin extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 访问ID */
	private Long infoId;
	/** 登录账号 */
	private String loginName;
	/** 登录IP地址 */
	private String ipaddr;
	/** 登录地点 */
	private String loginLocation;
	/** 浏览器类型 */
	private String browser;
	/** 操作系统 */
	private String os;
	/** 登录状态（0成功 1失败） */
	private String status;
	/** 提示消息 */
	private String msg;
	/** 访问时间 */
	private LocalDateTime loginTime;


	public void setInfoId(Long infoId){
		this.infoId = infoId;
	}

	public Long getInfoId(){
		return infoId;
	}
	public void setLoginName(String loginName){
		this.loginName = loginName;
	}

	public String getLoginName(){
		return loginName;
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
	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}
	public void setLoginTime(LocalDateTime loginTime){
		this.loginTime = loginTime;
	}

	public LocalDateTime getLoginTime(){
		return loginTime;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("infoId", getInfoId())
            .append("loginName", getLoginName())
            .append("ipaddr", getIpaddr())
            .append("loginLocation", getLoginLocation())
            .append("browser", getBrowser())
            .append("os", getOs())
            .append("status", getStatus())
            .append("msg", getMsg())
            .append("loginTime", getLoginTime())
            .toString();
    }
}
