package com.jee.boot.monitor.vo;

import com.jee.boot.common.core.domain.BaseEntity;
import com.jee.boot.support.job.constant.JobConstants;

import java.io.Serializable;

/**
 * 定时任务调度 VO对象 sys_job
 *
 * @author jeeLearner
 * @version V1.0
 */
public class SysJobVO extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private Long jobId;
    /** 任务名称 */
    private String jobName;
    /** 任务组名 */
    private String jobGroup;
    /** 调用目标字符串 */
    private String invokeTarget;
    /** cron执行表达式 */
    private String cronExpression;
    /** 计划执行错误策略（0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行） */
    private String misfirePolicy = JobConstants.MISFIRE_DEFAULT;
    /** 是否并发执行（0允许 1禁止） */
    private String concurrent;
    /** 状态（0正常 1暂停） */
    private String status;


    public void setJobId(Long jobId){
        this.jobId = jobId;
    }

    public Long getJobId(){
        return jobId;
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
    public void setCronExpression(String cronExpression){
        this.cronExpression = cronExpression;
    }

    public String getCronExpression(){
        return cronExpression;
    }
    public void setMisfirePolicy(String misfirePolicy){
        this.misfirePolicy = misfirePolicy;
    }

    public String getMisfirePolicy(){
        return misfirePolicy;
    }
    public void setConcurrent(String concurrent){
        this.concurrent = concurrent;
    }

    public String getConcurrent(){
        return concurrent;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
