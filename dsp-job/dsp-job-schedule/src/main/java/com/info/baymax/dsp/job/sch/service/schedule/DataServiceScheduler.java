package com.info.baymax.dsp.job.sch.service.schedule;

import com.info.baymax.common.saas.SaasContext;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: haijun
 * @Date: 2019/12/18 20:00
 */
@Component
@Slf4j
public class DataServiceScheduler {
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

    @PostConstruct
    public void stopDataServiceScheduler(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    cancelDataServiceScheduler();
                }catch (Exception e){
                    log.error("schedule data service exception: ", e);
                }

            }
        }, 1000*60, scheduler_scan_rate*3);
    }


    @PostConstruct
    public void runScheduler_push(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendReadyService();
                }catch (Exception e){
                    log.error("schedule data service exception: ", e);
                }

            }
        }, 1000*60, scheduler_scan_rate);
    }



    @Transactional(rollbackFor = Exception.class)
    public void sendReadyService(){
        List<DataService> list =
            dataServiceEntityService.querySpecialDataService (
                        DataServiceType.SERVICE_TYPE_PUSH,
                        DataServiceStatus.SERVICE_STATUS_DEPLOYED,
                        ScheduleJobStatus.JOB_STATUS_READY
                );
        log.debug("select ready dataservice size is " + list.size());

        if (list != null && list.size() > 0) {
            //发送任务到执行器
            for (DataService pushService : list) {
                dataShareScheduler.schedule(pushService, DataShareJob.class);
            }

            //更新DB
            for (DataService dataService : list) {
                dataServiceEntityService.updateDataServiceRunningStatus(dataService.getId(), ScheduleJobStatus.JOB_STATUS_RUNNING);//防止被其他timer重复调用
            }
            log.info("start to run push dataservice, count : {}", list.size());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelDataServiceScheduler(){
        List<DataService> list =
                dataServiceEntityService.querySpecialDataService (
                        DataServiceType.SERVICE_TYPE_PUSH,
                        DataServiceStatus.SERVICE_STATUS_STOPPED,
                        null
                );
        log.debug("stop dataservice schedule size is " + list.size());

        if (list != null && list.size() > 0) {
            //删除job trigger
            for (DataService dataService : list) {
                //删除已经启动的job
                dataShareScheduler.cancel(dataService);
            }

            //更新DB status=3
            for (DataService dataService : list) {
                dataServiceEntityService.stopDataServiceScheduler(dataService.getId());//防止被其他timer重复调用
            }
            log.info("stop push dataservice, count : {}", list.size());
        }
    }

}
