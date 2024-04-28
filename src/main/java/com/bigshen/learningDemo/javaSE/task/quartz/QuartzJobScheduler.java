package com.bigshen.learningDemo.javaSE.task.quartz;//package kl.gcrp.crlcache.service;

import org.apache.commons.collections4.MapUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author byj
 * @date 2024/1/17
 * @Description 使用quartz(偏重) 替换spring的ThreadPoolTaskScheduler
 */
public class QuartzJobScheduler {

    private static final Logger log = LoggerFactory.getLogger(QuartzJobScheduler.class);

    private static final ConcurrentHashMap<String, JobKey> JOB_TASK_MAP = new ConcurrentHashMap<>();
    private static final String CRL_JOB_GROUP = "crlJobGroup";

    public static Scheduler getCrlDownloadSchedulerTask() {
        Scheduler scheduler;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            throw new RuntimeException("开启定时下载调度任务异常：", e);
        }
        return scheduler;
    }

    public static void addJob(Scheduler scheduler, String jobName, String cronExpression) throws SchedulerException {
        // JobDetail 代表具体的可执行的调度程序，Job是这个可执行程调度程序所要执行的内容。
        JobDetail jobDetail = JobBuilder.newJob(CrlCacheDownLoadJob.class)
                .withIdentity(jobName, CRL_JOB_GROUP)
                .build();

        // Trigger 代表调度触发器，决定什么时候去调。
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("Trigger_" + jobName, CRL_JOB_GROUP)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        if (MapUtils.isEmpty(JOB_TASK_MAP)) {
            scheduler.start();
            log.info("开启定时下载crl的任务！");
        }

        scheduler.scheduleJob(jobDetail, trigger);

        JOB_TASK_MAP.put(jobName, scheduler.getJobDetail(JobKey.jobKey(jobName, CRL_JOB_GROUP)).getKey());
    }

    public static void removeJob(Scheduler scheduler, String jobName) throws SchedulerException {
        if (JOB_TASK_MAP.containsKey(jobName)) {
            // 获取任务的JobKey
            JobKey jobKey = JOB_TASK_MAP.get(jobName);

            // 删除任务
            scheduler.deleteJob(jobKey);
            JOB_TASK_MAP.remove(jobName);

            if (MapUtils.isEmpty(JOB_TASK_MAP)) {
                scheduler.shutdown();
                log.info("crl Job任务为空，定时任务关闭！");
            }
        }
    }

    public static long getNextFireTime(Scheduler scheduler, String jobName) {
        if (!JOB_TASK_MAP.containsKey(jobName)) {
            return 0;
        }
        // 获取任务的JobKey
        JobKey jobKey = JOB_TASK_MAP.get(jobName);

        // 获取下一次执行时间
        List<? extends Trigger> triggers;
        try {
            triggers = scheduler.getTriggersOfJob(jobKey);
        } catch (SchedulerException e) {
            return 0;
        }
        if (triggers.isEmpty()) {
            return 0;
        }
        // 获取下一次执行时间
        Date nextFireTime = triggers.get(0).getNextFireTime();
        return nextFireTime.getTime();
    }

    public static class CrlCacheDownLoadJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            // 执行任务的逻辑
            String jobName = context.getJobDetail().getKey().getName();

            System.out.println("执行定时任务：" + jobName + " " + System.currentTimeMillis());
        }
    }

    public static void main(String[] args) {
        try {
            // 创建调度器
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.shutdown();
            scheduler = QuartzJobScheduler.getCrlDownloadSchedulerTask();
            scheduler.start();
            // 启动调度器
            removeJob(scheduler, "Job1");

            // 添加任务1，使用Cron表达式每5秒执行一次
            addJob(scheduler, "Job1", "0/5 * * * * ?");

            // 添加任务2，使用Cron表达式每10秒执行一次
            addJob(scheduler, "Job2", "0/10 * * * * ?");
            // 等待一段时间
            Thread.sleep(20000);

            // 删除任务1
            removeJob(scheduler, "Job1");
            Thread.sleep(20000);

            removeJob(scheduler, "Job2");


        } catch (SchedulerException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
