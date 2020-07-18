package com.jee.boot.support.job.base;

import com.jee.boot.common.constant.Constants;
import com.jee.boot.common.utils.ExceptionUtils;
import com.jee.boot.common.utils.bean.JeeBeanUtils;
import com.jee.boot.common.utils.spring.SpringUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.support.job.constant.JobConstants;
import com.jee.boot.monitor.dto.SysJobDTO;
import com.jee.boot.monitor.dto.SysJobLogDTO;
import com.jee.boot.monitor.service.ISysJobLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 抽象quartz调用
 *
 * @author jeeLearner
 * @version V1.0
 */
public abstract class AbstractQuartzJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 线程本地变量
     */
    private static ThreadLocal<LocalDateTime> threadLocal = new ThreadLocal<>();

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param sysJob 系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, SysJobDTO sysJob) throws Exception;

    public static void main(String[] args) {
        SysJobDTO job = new SysJobDTO();
        job.setJobId(1L);
        job.setJobName("测试");
        SysJobDTO map = JeeBeanUtils.map(job, SysJobDTO.class);
        System.out.println(map);

        //BeanUtils.copyProperties(job, dto);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysJobDTO sysJob = JeeBeanUtils.map(context.getMergedJobDataMap().get(JobConstants.TASK_PROPERTIES), SysJobDTO.class);

        try {
            before(context, sysJob);
            if (sysJob != null){
                doExecute(context, sysJob);
            }
            after(context, sysJob, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(context, sysJob, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param sysJob 系统计划任务
     */
    protected void before(JobExecutionContext context, SysJobDTO sysJob){
        threadLocal.set(LocalDateTime.now());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param sysJob 系统计划任务
     */
    protected void after(JobExecutionContext context, SysJobDTO sysJob, Exception e) {
        LocalDateTime startTime = threadLocal.get();
        threadLocal.remove();

        final SysJobLogDTO sysJobLog = new SysJobLogDTO();
        sysJobLog.setJobName(sysJob.getJobName());
        sysJobLog.setJobGroup(sysJob.getJobGroup());
        sysJobLog.setInvokeTarget(sysJob.getInvokeTarget());
        sysJobLog.setStartTime(startTime);
        sysJobLog.setEndTime(LocalDateTime.now());
        Duration duration = Duration.between(sysJobLog.getStartTime(),sysJobLog.getEndTime());
        sysJobLog.setJobMessage(sysJobLog.getJobName() + " 总共耗时：" + duration.toMillis() + "毫秒");
        if (e != null){
            sysJobLog.setStatus(Constants.FAIL);
            String errorMsg = JeeStringUtils.substring(ExceptionUtils.getExceptionMessage(e), 0, 2000);
            sysJobLog.setExceptionInfo(errorMsg);
        }else {
            sysJobLog.setStatus(Constants.SUCCESS);
        }

        // 写入数据库当中
        SpringUtils.getBean(ISysJobLogService.class).insertJobLog(sysJobLog);
    }
}

