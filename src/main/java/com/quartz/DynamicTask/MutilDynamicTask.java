package com.quartz.DynamicTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试数据
 */
public class MutilDynamicTask {

    /**
     * 计划任务map
     */
    private static List<ScheduleJob> jobMap = new ArrayList<ScheduleJob>();

    static {
        for (int i = 0; i < 5; i++){
            ScheduleJob job = new ScheduleJob();
            job.setJobId("10001" + i);
            job.setJobName("data_import" + i);
            job.setJobGroup("dataWork");
            job.setJobStatus("1");
            job.setCronExpression("0/5 * * * * ?");
            job.setDesc("数据导入任务");
            jobMap.add(job);
        }
    }

    public static List<ScheduleJob> getAllJob(){
        return jobMap;
    }
}
