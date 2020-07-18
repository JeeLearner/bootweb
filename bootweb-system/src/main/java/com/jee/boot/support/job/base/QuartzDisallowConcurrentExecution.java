package com.jee.boot.support.job.base;

import com.jee.boot.monitor.dto.SysJobDTO;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（禁止并发执行）
 *
 * @author jeeLearner
 * @version V1.0
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, SysJobDTO sysJob) throws Exception {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
