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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/18 20:00
 */
@Component
@Slf4j
public class DataServiceSchedulerRunner {
    @Autowired
    DataShareScheduler dataShareScheduler;

    @Autowired
    DataServiceEntityService dataServiceEntityService;

    @PostConstruct
    private void updateServiceStatus(){
        try {
            Thread.sleep(30*1000);
        }catch (Exception e){

        }
        dataServiceEntityService.recoverDataService();
        dataServiceEntityService.updateFinishedDataService();
        log.info("recover running service and update finished service success.");
    }


    @PostConstruct
    public void runScheduler_push(){
        try {
            Thread.sleep(60*1000);
        }catch (Exception e){
        }

        while (true) {
            sendReadyService();

            try {
                Thread.sleep(5*1000);
            }catch (Exception e){
            }
        }
    }

    @Transactional
    public void sendReadyService(){
        List<DataService> list = dataServiceEntityService.querySpecialDataService(ServiceTypes.SERVICE_TYPE_PUSH, ServiceStatus.SERVICE_STATUS_DEPLOYED, JobStatus.JOB_STATUS_READY);

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
