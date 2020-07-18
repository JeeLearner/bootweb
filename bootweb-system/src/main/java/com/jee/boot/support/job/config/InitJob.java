package com.jee.boot.support.job.config;

import com.jee.boot.common.exception.job.TaskException;
import com.jee.boot.monitor.dto.SysJobDTO;
import com.jee.boot.monitor.service.ISysJobService;
import com.jee.boot.support.job.JobUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Job初始化
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class InitJob {

    @Autowired
    private ISysJobService jobService;
    @Autowired
    private Scheduler scheduler;

    /**
     * 项目启动时，初始化定时器
     主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<SysJobDTO> list = jobService.listJobAll();
        for (SysJobDTO job : list) {
            JobUtils.createScheduleJob(scheduler, job);
        }
    }
}

