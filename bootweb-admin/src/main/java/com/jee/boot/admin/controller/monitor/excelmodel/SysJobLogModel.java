package com.jee.boot.admin.controller.monitor.excelmodel;

import com.jee.boot.common.poi.annotation.Excel;
import com.jee.boot.common.utils.spring.SpringUtils;
import com.jee.boot.system.dto.SysDictDataDTO;
import com.jee.boot.system.service.ISysDictDataService;

import java.util.List;

/**
 * 定时任务日志 excel导出实体
 *
 * @author jeeLearner
 * @version V1.0
 */
public class SysJobLogModel {

    /** ID */
    @Excel(name = "日志序号")
    private Long jobLogId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String jobName;

    /** 任务组名 */
    @Excel(name = "任务组名")
    private String jobGroup;

    /** 调用目标字符串 */
    @Excel(name = "调用目标字符串", width = 25)
    private String invokeTarget;

    /** 日志信息 */
    @Excel(name = "日志信息", width = 25)
    private String jobMessage;

    /** 执行状态（0正常 1失败） */
    @Excel(name = "执行状态", readConverterExp = "0=正常,1=失败")
    private String status;

    /** 异常信息 */
    @Excel(name = "异常信息")
    private String exceptionInfo;



    public Long getJobLogId() {
        return jobLogId;
    }

    public void setJobLogId(Long jobLogId) {
        this.jobLogId = jobLogId;
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

    public String getJobMessage() {
        return jobMessage;
    }

    public void setJobMessage(String jobMessage) {
        this.jobMessage = jobMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }
}

