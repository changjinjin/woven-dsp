package com.info.baymax.dsp.job.sch.job;

import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.dsp.data.consumer.constant.DataServiceStatus;
import com.info.baymax.dsp.data.consumer.constant.ScheduleJobStatus;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import com.info.baymax.dsp.job.sch.ApplicationContextProvider;
import com.info.baymax.dsp.job.sch.client.ExecutorRestClient;
import com.info.baymax.dsp.job.sch.scheduler.DataShareScheduler;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author: haijun
 * @Date: 2019/12/18 18:14
 */

@Slf4j
public class DataShareJob implements Job {
    @Autowired
    private ExecutorRestClient executorRestClient;

    @Autowired
    private DataServiceEntityService dataServiceEntityService;

    @Autowired
    DataShareScheduler dataShareScheduler;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        /*
         * In Quartz job execution context, no spring context available, make use of the
         * ApplicationContextProvider to perform dependency injection!!
         */
         ApplicationContextProvider.processInjection(this);

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Long serviceId = jobDataMap.getLong("serviceId");

        Map<String,Object> body = new HashMap<String,Object>();
        body.putAll(jobDataMap);

        try {
            log.info("jobSchedule send request to executor, dataserviceId :" + serviceId);
            //对于周期任务,当前running状态可能是2或3,在执行之前更新为1
            DataService dataService = dataServiceEntityService.selectByPrimaryKey(serviceId);
            //DataService虽然已经停止,但是trigger还没有停止,状态更新会混乱
            if(dataService.getStatus() == DataServiceStatus.SERVICE_STATUS_STOPPED){
                log.info("dataService [{}] has been stopped, go to cancel job scheduler.", dataService.getId());
                dataShareScheduler.cancel(dataService);
                dataServiceEntityService.updateDataServiceRunningStatus(serviceId, ScheduleJobStatus.JOB_STATUS_STOPPED);
                return;
            }
            dataServiceEntityService.updateDataServiceRunningStatus(serviceId, ScheduleJobStatus.JOB_STATUS_RUNNING);

            //设置上下文
            SaasContext.initSaasContext(dataService.getTenantId(), dataService.getOwner());
            executorRestClient.deployDataservice(body);
            //清空上下文
            SaasContext.clear();

            log.info("jobSchedule success to send request to executor, dataserviceId :" + serviceId);
        } catch (Exception e) {
            log.error("call rest api throw Exception: " + serviceId, e);
            //--------------TODO-------如果执行启动失败怎么办-------------
            //更新dataservice为可执行状态
            try {
                dataServiceEntityService.updateDataServiceRunningStatus(serviceId, ScheduleJobStatus.JOB_STATUS_FAILED);
                log.info("dataService [{}] deploy failed, restore running status to failed", serviceId);
            }catch (Exception ex){
                log.error("dataService ["+ serviceId+ "] deploy failed, restore running status exception: ", e);
            }
        }
    }

}
