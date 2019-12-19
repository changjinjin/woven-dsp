package com.info.baymax.dsp.job.sch.scheduler;

import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.job.sch.constant.SchedulerTypes;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

/**
 * @Author: haijun
 * @Date: 2019/12/18 17:46
 */
@Component
@Slf4j
public class DataShareScheduler implements AbstractScheduler<DataService> {
    private Scheduler scheduler;

    @PostConstruct
    public void ready() {
        try {
            StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException("can't create scheduler", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        try {
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdown(true);
            }
        } catch (SchedulerException e) {
            log.error("shutdown QuartzScheduler error：", e);
        }
    }

    @Override
    public void schedule(DataService ds, Class<? extends Job> jobClass) {
        try {
            String jobName = ds.getId() + "";
            String jobGroup = ds.getTenantId()+"_" + ds.getOwner();
            TriggerKey triggerKey = new TriggerKey("tg-" + jobName, jobGroup);

            log.info("begin to run scheduler: {}" ,triggerKey);

            if (scheduler.checkExists(triggerKey)) {
                log.info("skip schedule {}", jobName);
                return;
            }

            log.info("try to schedule {}", jobName);
            JobDataMap jdm = new JobDataMap();
            jdm.put("type", ds.getType());
            jdm.put("scheduleType", ds.getScheduleType());
            jdm.put("jobName", jobName);
            jdm.put("jobGroup", jobGroup);
            jdm.put("serviceId", ds.getId());
            jdm.put("tenantId", ds.getTenantId());
            jdm.put("owner", ds.getOwner());
            jdm.put("dataService", JsonBuilder.getInstance().toJson(ds));

            // name & group
            JobDetail myJob = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).usingJobData(jdm).build();

            long startMiss = System.currentTimeMillis();
            try {
                Long startTime = Long.parseLong(ds.getServiceConfiguration().get("startTime"));
                startMiss =  startTime;
            } catch (NumberFormatException ex) {
                log.error("NumberFormatException: For input string : {}", ds.getServiceConfiguration().get("startTime"));
            }

            if (ds.getLastExecutedTime() != null && ds.getLastExecutedTime().getTime() > startMiss) {
                startMiss = ds.getLastExecutedTime().getTime() + 1;
            }

            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(triggerKey).startAt(new Date(startMiss)).forJob(myJob);

            if (ds.getServiceConfiguration().get("endTime") != null) {
                long lastMiss = Long.parseLong(ds.getServiceConfiguration().get("endTime"));

                if (ds.getServiceConfiguration().get("endTime") != null && lastMiss < startMiss) {
                    log.warn("{} is out time range, skip it now. [{}--{}]", ds.getId(), new Date(startMiss), new Date(lastMiss));
                    return;
                }
                triggerBuilder.endAt(new Date(lastMiss));
            }

            if (SchedulerTypes.SCHEDULER_TYPE_CRON.equals(ds.getScheduleType())) {
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule((String) ds.getServiceConfiguration().get("cron"))
                        .withMisfireHandlingInstructionDoNothing());
            }

            Trigger trigger = triggerBuilder.build();

            scheduler.scheduleJob(myJob, trigger);
        } catch (Exception e) {
            throw new RuntimeException("schedule flow failed", e);
        }
    }

    @Override
    public void cancel(DataService service) {
        try {
            String jobName = service.getId() + "";
            String jobGroup = service.getTenantId()+"_" + service.getOwner();
            log.info("try to cancel DataShareScheduler {}", service.getId());
            scheduler.deleteJob(new JobKey(jobName, jobGroup));
        } catch (Exception e) {
            throw new RuntimeException("cancel flow failed", e);
        }
    }

}
