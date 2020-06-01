package com.info.baymax.dsp.job.sch.service.clean;

import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.consumer.constant.DataServiceStatus;
import com.info.baymax.dsp.data.consumer.constant.ScheduleJobStatus;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import com.info.baymax.dsp.job.sch.scheduler.DataShareScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/24 14:59
 */
@Slf4j
@Service
@EnableScheduling
public class DataEntityCleaner {
    @Autowired
    private DataServiceEntityService dataServiceEntityService;
    @Autowired
    private DataResourceService dataResourceService;
    @Autowired
    DataShareScheduler dataShareScheduler;

    @Scheduled(cron="0 */30 * * * ?")
    public void cleanDB(){
        cleanDataService();
        cleanDataResource();
    }

    @Transactional
    public void cleanDataService(){
        try {
            long expireTime = System.currentTimeMillis();
            int expireCount = dataServiceEntityService.countExpired(expireTime);
            if (expireCount <= 0) {
                return;
            }

            List<DataService> dataServiceList = dataServiceEntityService.findExpired(expireTime);
            if (ICollections.hasNoElements(dataServiceList)) {
                return;
            }

            long count = 0;
            List<String> needDeleteIds = new ArrayList<>();
            for (DataService dataService : dataServiceList) {
                count++;
                dataService.setStatus(DataServiceStatus.SERVICE_STATUS_EXPIRED);
                dataService.setLastModifiedTime(new Date());
                dataService.setLastModifier(SaasContext.getCurrentUsername());
                //删除已经启动的job
                dataShareScheduler.cancel(dataService);
                dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_STOPPED);
            }

            //更新DB
            dataServiceEntityService.updateListByPrimaryKey(dataServiceList);

            log.info("clean: update dataService success : " + count);
        } catch (Throwable e) {
            log.error("clean dataService error", e);
        }
    }

    @Transactional
    public void cleanDataResource(){
        try {
            long expireTime = System.currentTimeMillis();
            int expireCount = dataResourceService.countExpired(expireTime);
            if (expireCount <= 0) {
                return;
            }

            List<DataResource> dataResourceList = dataResourceService.findExpired(expireTime);
            if (ICollections.hasNoElements(dataResourceList)) {
                return;
            }

            long count = 0;
            List<String> needDeleteIds = new ArrayList<>();
            for (DataResource dataResource : dataResourceList) {
                count++;
                dataResource.setOpenStatus(2);
                dataResource.setLastModifiedTime(new Date());
                dataResource.setLastModifier(SaasContext.getCurrentUsername());
            }

            //更新DB
            dataResourceService.updateListByPrimaryKey(dataResourceList);

            log.info("clean: update dataResource successed : {}", count);
        } catch (Throwable e) {
            log.error("clean dataResource error", e);
        }
    }
}
