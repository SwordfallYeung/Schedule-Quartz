package com.quartz.DynamicTask;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OperateTasks {

    public static void main(String[] args) throws Exception{
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("schedule-dynamic-quartz.xml");

        //schedulerFactoryBean 由spring创建注入
        Scheduler scheduler = (Scheduler) context.getBean("scheduler");

        //这里获取任务信息数据
        List<ScheduleJob> jobList = MutilDynamicTask.getAllJob();

        // ### 创建并启动job ###
        for (ScheduleJob job : jobList){
            TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(),job.getJobGroup());

            //获取trigger, 即在spring配置文件中定义的bean id="myTrigger"
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            //不存在，创建一个
            if (null == trigger){

                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

                //按新的cronExpression表达式构建一个新的trigger
                trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(),job.getJobGroup()).withSchedule(scheduleBuilder).build();

                // 构建job信息
                JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(job.getJobName(),job.getJobGroup()).build();
                // 放入参数，运行时的方法可以获取
                jobDetail.getJobDataMap().put("scheduleJob",job);

                //启动
                scheduler.scheduleJob(jobDetail,trigger);
            }else {
                //Trigger已存在，那么更新相应的定时设置
                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

                //按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey,trigger);
            }
        }
    }

    //计划中的任务
    //指那些已经添加到quartz调度器的任务，因为quartz并没有直接提供这样的查询接口，
    // 所以我们需要结合JobKey和Trigger来实现，核心代码：
    //需要注意一个job可能会有多个trigger的情况
    public void RunPlanTask() throws Exception{
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("schedule-dynamic-quartz.xml");

        //schedulerFactoryBean 由spring创建注入
        Scheduler scheduler = (Scheduler) context.getBean("scheduler");

        GroupMatcher<JobKey> matcher = GroupMatcher.anyGroup();

        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();

        for (JobKey jobKey : jobKeys){
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);

            for (Trigger trigger : triggers){
                ScheduleJob job = new ScheduleJob();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setDesc("触发器："+jobKey.getGroup());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger){
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobList.add(job);
            }
        }
    }

    //暂停任务
    public void PauseTask() throws Exception{
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("schedule-dynamic-quartz.xml");

        //schedulerFactoryBean 由spring创建注入
        Scheduler scheduler = (Scheduler) context.getBean("scheduler");

        //这里获取任务信息数据
        List<ScheduleJob> jobList = MutilDynamicTask.getAllJob();

        for (ScheduleJob scheduleJob : jobList){
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            scheduler.pauseJob(jobKey);
        }
    }

    //恢复任务
    public void ResumeTask() throws Exception{
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("schedule-dynamic-quartz.xml");

        //schedulerFactoryBean 由spring创建注入
        Scheduler scheduler = (Scheduler) context.getBean("scheduler");

        //这里获取任务信息数据
        List<ScheduleJob> jobList = MutilDynamicTask.getAllJob();

        for (ScheduleJob scheduleJob : jobList){
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            scheduler.resumeJob(jobKey);
        }
    }

    public void RunOnlyOne() throws Exception{
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("schedule-dynamic-quartz.xml");

        //schedulerFactoryBean 由spring创建注入
        Scheduler scheduler = (Scheduler) context.getBean("scheduler");

        //这里获取任务信息数据
        List<ScheduleJob> jobList = MutilDynamicTask.getAllJob();

        for (ScheduleJob scheduleJob : jobList){
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            scheduler.triggerJob(jobKey);
        }
    }

    public void deleteTask() throws Exception{
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("schedule-dynamic-quartz.xml");

        //schedulerFactoryBean 由spring创建注入
        Scheduler scheduler = (Scheduler) context.getBean("scheduler");

        String jobGroup = "JobGroup1";

        System.out.println("### 移除job3 ###");
        scheduler.deleteJob(JobKey.jobKey("Job3", jobGroup));
    }
}
