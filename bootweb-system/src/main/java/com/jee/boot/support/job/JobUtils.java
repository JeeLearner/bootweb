package com.jee.boot.support.job;

import com.jee.boot.common.exception.job.TaskException;
import com.jee.boot.common.exception.job.TaskException.Code;
import com.jee.boot.support.job.base.QuartzDisallowConcurrentExecution;
import com.jee.boot.support.job.base.QuartzJobExecution;
import com.jee.boot.support.job.constant.JobConstants;
import com.jee.boot.monitor.dto.SysJobDTO;
import org.quartz.*;

import java.util.Date;

/**
 * 定时任务工具类
 *
 * @author jeeLearner
 * @version V1.0
 */
public class JobUtils {

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, SysJobDTO job) throws SchedulerException, TaskException {
        Class<? extends Job> jobClass = getQuartzJobClass(job);
        //构建job信息
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(getJobKey(jobId, jobGroup))
                .build();
        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job, cronScheduleBuilder);

        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(jobId, jobGroup))
                .withSchedule(cronScheduleBuilder)
                .build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(JobConstants.TASK_PROPERTIES, job);

        // 判断是否存在
        if (scheduler.checkExists(getJobKey(jobId, jobGroup))){
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(getJobKey(jobId, jobGroup));
        }

        scheduler.scheduleJob(jobDetail, trigger);

        // 暂停任务
        if (job.getStatus().equals(JobConstants.Status.PAUSE.getValue())){
            scheduler.pauseJob(getJobKey(jobId, jobGroup));
        }
    }


    /**
     * 构建任务键对象JobKey
     */
    public static JobKey getJobKey(Long jobId, String jobGroup){
        return JobKey.jobKey(JobConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * 构建任务触发对象
     */
    private static TriggerKey getTriggerKey(Long jobId, String jobGroup) {
        return TriggerKey.triggerKey(JobConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * 得到quartz任务类
     *
     * @param sysJob 执行计划
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getQuartzJobClass(SysJobDTO sysJob){
        boolean isConcurrent = JobConstants.TASK_CONCURRENT.equals(sysJob.getConcurrent());
        return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    /**
     * 设置定时任务策略
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(SysJobDTO job, CronScheduleBuilder cb)
            throws TaskException {
        switch (job.getMisfirePolicy()) {
            case JobConstants.MISFIRE_DEFAULT:
                return cb;
            case JobConstants.MISFIRE_IGNORE_MISFIRES:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case JobConstants.MISFIRE_FIRE_AND_PROCEED:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case JobConstants.MISFIRE_DO_NOTHING:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                throw new TaskException("The task misfire policy '" + job.getMisfirePolicy()
                        + "' cannot be used in cron schedule tasks", Code.CONFIG_ERROR);
        }
    }












    /**
     *
     * @param scheduler quartz调度器
     * @param startAtTime 任务执行时刻
     * @param name 任务名称
     * @param group 任务组名称
     * @param jobBean 具体任务
     */
    public static void createJobByStartAt(Scheduler scheduler, long startAtTime, String name, String group, Class jobBean){
        //创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(name, group)
                .startAt(new Date(startAtTime))
                .build();
        createJob(scheduler, name, group, trigger,jobBean);
    }

    /**
     *
     * @param scheduler quartz调度器
     * @param name 任务名称
     * @param group 任务组名称
     * @param cron cron表达式
     * @param jobBean 具体任务
     */
    public static void createJobByCron(Scheduler scheduler, String name, String group,String cron,Class jobBean){
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        //创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(name, group)
                .withSchedule(scheduleBuilder)
                .build();
        createJob(scheduler,name,group,trigger,jobBean);
    }

    private static void createJob(Scheduler scheduler, String name, String group, Trigger trigger, Class jobBean) {
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(jobBean)
                .withIdentity(name, group)
                .build();
        try {
            //将触发器与任务绑定到调度器内
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}

