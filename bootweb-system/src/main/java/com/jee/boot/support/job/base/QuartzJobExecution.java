package com.jee.boot.support.job.base;

import com.jee.boot.monitor.dto.SysJobDTO;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author jeeLearner
 * @version V1.0
 */
public class QuartzJobExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobExecutionContext context, SysJobDTO sysJob) throws Exception {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}

