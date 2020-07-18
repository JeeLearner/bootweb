package com.jee.boot.framework.manager;

import com.jee.boot.common.utils.spring.SpringUtils;
import com.jee.boot.common.utils.thread.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理器
 *
 * @author jeeLearner
 * @version V1.0
 */
public class AsyncManager{

    /*** 操作延迟10毫秒 */
    private final int OPERATE_DELAY_TIME = 10;
    /*** 异步操作任务调度线程池 */
    private ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");

    /**
     * 单例模式
     */
    private AsyncManager(){}

    private static class AsyncManagerHolder{
        private static final AsyncManager INSTANCE = new AsyncManager();
    }

    public static AsyncManager me(){
        return AsyncManagerHolder.INSTANCE;
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(TimerTask task){
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown(){
        ThreadUtils.shutdownAndAwaitTermination(executor);
    }
}

