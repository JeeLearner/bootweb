package com.jee.boot.monitor.dto;

import com.jee.boot.monitor.domain.SysJobLog;

import java.time.LocalDateTime;

/**
 * 定时任务调度日志 DTO对象
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysJobLogDTO extends SysJobLog{
    /** 开始时间 */
    private LocalDateTime startTime;
    /** 结束时间 */
    private LocalDateTime endTime;



    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
