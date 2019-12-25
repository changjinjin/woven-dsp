package com.info.baymax.dsp.job.sch.service;

import com.info.baymax.dsp.data.consumer.constant.DataServiceStatus;
import com.info.baymax.dsp.data.consumer.constant.DataServiceType;
import com.info.baymax.dsp.data.consumer.constant.ScheduleJobStatus;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import com.info.baymax.dsp.job.sch.job.DataShareJob;
import com.info.baymax.dsp.job.sch.scheduler.DataShareScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: haijun
 * @Date: 2019/12/18 20:00
 */
@Component
@EnableScheduling
@Slf4j
public class DataServiceSchedulerRunner {
    @Value(value = "${scheduler.scan.dataservice.rate:10000}")
    private final Integer scheduler_scan_rate = 10000;

    @Autowired
    DataShareScheduler dataShareScheduler;

    @Autowired
    DataServiceEntityService dataServiceEntityService;

    @PostConstruct
    public void updateServiceStatus(){
        try {
            log.info("start to recover running service and update finished service.");
            dataServiceEntityService.recoverDataService();
            dataServiceEntityService.updateFinishedDataService();

            log.info("end to recover running service and update finished service success.");
        }catch (Exception e){

        }
    }

//    @Scheduled(initialDelay = 1000*60, fixedRate = scheduler_scan_rate)
    @PostConstruct
    public void runScheduler_push(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendReadyService();
            }
        }, 1000*60, scheduler_scan_rate);
    }



    @Transactional
    public void sendReadyService(){
        List<DataService> list = dataServiceEntityService.querySpecialDataService(DataServiceType.SERVICE_TYPE_PUSH, DataServiceStatus.SERVICE_STATUS_DEPLOYED, ScheduleJobStatus.JOB_STATUS_READY);
        log.debug("select ready dataservice size is " + list.size());
        for (DataService pushService : list) {
            dataShareScheduler.schedule(pushService, DataShareJob.class);
        }

        if (list != null && list.size() > 0) {
            for (DataService dataService : list) {
                dataServiceEntityService.updateDataServiceToRunning(dataService.getId());
            }
            log.info("start to run push service, count : {}", list.size());
        }
    }

}
