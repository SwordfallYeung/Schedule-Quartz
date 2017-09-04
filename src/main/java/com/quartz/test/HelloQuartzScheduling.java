package com.quartz.test;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class HelloQuartzScheduling {
    public static void main(String[] args) throws Exception {
        SchedulerFactory schedulerFactory=new StdSchedulerFactory();
        Scheduler scheduler=schedulerFactory.getScheduler();


    }
}
