package com.quartz.DynamicTask;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 任务入口
 * 定时任务运行工厂类
 *
 * 无注解是无状态的
 * 有注解是有状态的
 */
@DisallowConcurrentExecution
public class QuartzJobFactory implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("任务成功运行");
        ScheduleJob scheduleJob = (ScheduleJob)jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        System.out.println("任务名称 = [" + scheduleJob.getJobName() + "]");
    }
}
