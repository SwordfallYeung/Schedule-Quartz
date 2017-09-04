package com.quartz.OrdinaryTask;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;


/**
 * 在spring中使用Quartz有两种方式实现：
 * 第一种是任务类继承QuartzJobBean
 */
public class FirstSimpleJob extends QuartzJobBean {

   private static int counter = 0;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println();
        long ms = System.currentTimeMillis();
        System.out.println("\t\t" + new Date(ms));
        System.out.println(ms);
        System.out.println("(" + counter + ")");
        String s = (String) jobExecutionContext.getMergedJobDataMap().get("service");
        System.out.println(s);
        System.out.println();
    }
}
