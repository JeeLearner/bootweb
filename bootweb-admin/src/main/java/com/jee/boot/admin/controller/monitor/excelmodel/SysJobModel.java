package com.jee.boot.admin.controller.monitor.excelmodel;

import com.jee.boot.common.poi.annotation.Excel;
import com.jee.boot.common.poi.annotation.Excel.ColumnType;
import com.jee.boot.common.utils.spring.SpringUtils;
import com.jee.boot.support.job.constant.JobConstants;
import com.jee.boot.system.dto.SysDictDataDTO;
import com.jee.boot.system.service.ISysDictDataService;

import java.util.List;

/**
 * @author jeeLearner
 * @version V1.0
 */
public class SysJobModel {
    /** 任务ID */
    @Excel(name = "任务序号", cellType = ColumnType.NUMERIC)
    private Long jobId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String jobName;

    /** 任务组名 */
    @Excel(name = "任务组名")
    private String jobGroup;

    /** 调用目标字符串 */
    @Excel(name = "调用目标字符串", width = 25)
    private String invokeTarget;

    /** cron执行表达式 */
    @Excel(name = "执行表达式 ")
    private String cronExpression;

    /** 计划执行错误策略（0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行） */
    @Excel(name = "计划策略 ", readConverterExp = "0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    private String misfirePolicy = JobConstants.MISFIRE_DEFAULT;

    /** 是否并发执行（0允许 1禁止） */
    @Excel(name = "并发执行", readConverterExp = "0=允许,1=禁止")
    private String concurrent;

    /** 状态（0正常 1暂停） */
    @Excel(name = "任务状态", readConverterExp = "0=正常,1=暂停")
    private String status;



    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        String temp = "";
        List<SysDictDataDTO> list = SpringUtils.getBean(ISysDictDataService.class).listDictDataByType("sys_job_group");
        for (SysDictDataDTO data : list) {
            if (jobGroup != null && jobGroup.equals(data.getDictValue())){
                temp = data.getDictLabel();
                break;
            }
        }
        if("".equals(temp)){
            this.jobGroup = jobGroup;
        } else {
            this.jobGroup = temp;
        }
    }

    public String getInvokeTarget() {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget) {
        this.invokeTarget = invokeTarget;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getMisfirePolicy() {
        return misfirePolicy;
    }

    public void setMisfirePolicy(String misfirePolicy) {
        this.misfirePolicy = misfirePolicy;
    }

    public String getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(String concurrent) {
        this.concurrent = concurrent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

