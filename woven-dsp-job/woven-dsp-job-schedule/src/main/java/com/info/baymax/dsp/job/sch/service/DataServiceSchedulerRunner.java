package com.info.baymax.dsp.job.sch.service;

import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import com.info.baymax.dsp.job.sch.constant.JobStatus;
import com.info.baymax.dsp.job.sch.constant.ServiceStatus;
import com.info.baymax.dsp.job.sch.constant.ServiceTypes;
import com.info.baymax.dsp.job.sch.job.DataShareJob;
import com.info.baymax.dsp.job.sch.scheduler.AbstractScheduler;
import com.info.baymax.dsp.job.sch.scheduler.DataShareScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/18 20:00
 */
@Component
@EnableScheduling
@Slf4j
public class DataServiceSchedulerRunner {
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

    @Scheduled(initialDelay = 1000*60, fixedRate = 1000*10)
    public void runScheduler_push(){
        sendReadyService();
    }

    @Transactional
    public void sendReadyService(){
        List<DataService> list = dataServiceEntityService.querySpecialDataService(ServiceTypes.SERVICE_TYPE_PUSH, ServiceStatus.SERVICE_STATUS_DEPLOYED, JobStatus.JOB_STATUS_READY);
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
