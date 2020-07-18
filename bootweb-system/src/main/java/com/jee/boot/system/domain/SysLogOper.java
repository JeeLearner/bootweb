package com.jee.boot.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jee.boot.common.core.domain.BaseEntity;

import java.time.LocalDateTime;

/**
 * 操作日志记录 DO对象 sys_log_oper
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class SysLogOper extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 日志主键 */
	private Long operId;
	/** 模块标题 */
	private String title;
	/** 业务类型（0其它 1新增 2修改 3删除） */
	private Integer businessType;
	/** 方法名称 */
	private String method;
	/** 请求方式 */
	private String requestMethod;
	/** 操作类别（0其它 1后台用户 2手机端用户） */
	private Integer operatorType;
	/** 操作人员 */
	private String operName;
	/** 部门名称 */
	private String deptName;
	/** 请求URL */
	private String operUrl;
	/** 主机地址 */
	private String operIp;
	/** 操作地点 */
	private String operLocation;
	/** 请求参数 */
	private String operParam;
	/** 返回参数 */
	private String jsonResult;
	/** 操作状态（0正常 1异常） */
	private Integer status;
	/** 错误消息 */
	private String errorMsg;
	/** 操作时间 */
	private LocalDateTime operTime;


	public void setOperId(Long operId){
		this.operId = operId;
	}

	public Long getOperId(){
		return operId;
	}
	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}
	public void setBusinessType(Integer businessType){
		this.businessType = businessType;
	}

	public Integer getBusinessType(){
		return businessType;
	}
	public void setMethod(String method){
		this.method = method;
	}

	public String getMethod(){
		return method;
	}
	public void setRequestMethod(String requestMethod){
		this.requestMethod = requestMethod;
	}

	public String getRequestMethod(){
		return requestMethod;
	}
	public void setOperatorType(Integer operatorType){
		this.operatorType = operatorType;
	}

	public Integer getOperatorType(){
		return operatorType;
	}
	public void setOperName(String operName){
		this.operName = operName;
	}

	public String getOperName(){
		return operName;
	}
	public void setDeptName(String deptName){
		this.deptName = deptName;
	}

	public String getDeptName(){
		return deptName;
	}
	public void setOperUrl(String operUrl){
		this.operUrl = operUrl;
	}

	public String getOperUrl(){
		return operUrl;
	}
	public void setOperIp(String operIp){
		this.operIp = operIp;
	}

	public String getOperIp(){
		return operIp;
	}
	public void setOperLocation(String operLocation){
		this.operLocation = operLocation;
	}

	public String getOperLocation(){
		return operLocation;
	}
	public void setOperParam(String operParam){
		this.operParam = operParam;
	}

	public String getOperParam(){
		return operParam;
	}
	public void setJsonResult(String jsonResult){
		this.jsonResult = jsonResult;
	}

	public String getJsonResult(){
		return jsonResult;
	}
	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return status;
	}
	public void setErrorMsg(String errorMsg){
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg(){
		return errorMsg;
	}
	public void setOperTime(LocalDateTime operTime){
		this.operTime = operTime;
	}

	public LocalDateTime getOperTime(){
		return operTime;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("operId", getOperId())
            .append("title", getTitle())
            .append("businessType", getBusinessType())
            .append("method", getMethod())
            .append("requestMethod", getRequestMethod())
            .append("operatorType", getOperatorType())
            .append("operName", getOperName())
            .append("deptName", getDeptName())
            .append("operUrl", getOperUrl())
            .append("operIp", getOperIp())
            .append("operLocation", getOperLocation())
            .append("operParam", getOperParam())
            .append("jsonResult", getJsonResult())
            .append("status", getStatus())
            .append("errorMsg", getErrorMsg())
            .append("operTime", getOperTime())
            .toString();
    }
}
