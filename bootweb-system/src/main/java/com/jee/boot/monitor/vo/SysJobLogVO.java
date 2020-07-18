package com.jee.boot.monitor.vo;

import com.jee.boot.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 定时任务调度日志 VO对象 sys_job_log
 *
 * @author jeeLearner
 * @version V1.0
 */
public class SysJobLogVO extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 任务日志ID */
    private Long jobLogId;
    /** 任务名称 */
    private String jobName;
    /** 任务组名 */
    private String jobGroup;
    /** 调用目标字符串 */
    private String invokeTarget;
    /** 日志信息 */
    private String jobMessage;
    /** 执行状态（0正常 1失败） */
    private String status;
    /** 异常信息 */
    private String exceptionInfo;


    public void setJobLogId(Long jobLogId){
        this.jobLogId = jobLogId;
    }

    public Long getJobLogId(){
        return jobLogId;
    }
    public void setJobName(String jobName){
        this.jobName = jobName;
    }

    public String getJobName(){
        return jobName;
    }
    public void setJobGroup(String jobGroup){
        this.jobGroup = jobGroup;
    }

    public String getJobGroup(){
        return jobGroup;
    }
    public void setInvokeTarget(String invokeTarget){
        this.invokeTarget = invokeTarget;
    }

    public String getInvokeTarget(){
        return invokeTarget;
    }
    public void setJobMessage(String jobMessage){
        this.jobMessage = jobMessage;
    }

    public String getJobMessage(){
        return jobMessage;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
    public void setExceptionInfo(String exceptionInfo){
        this.exceptionInfo = exceptionInfo;
    }

    public String getExceptionInfo(){
        return exceptionInfo;
    }
}
