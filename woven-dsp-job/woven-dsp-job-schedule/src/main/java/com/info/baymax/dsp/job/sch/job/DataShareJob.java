package com.info.baymax.dsp.job.sch.job;

import com.info.baymax.dsp.data.consumer.constant.ScheduleJobStatus;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import com.info.baymax.dsp.job.sch.ApplicationContextProvider;
import com.info.baymax.dsp.job.sch.client.ExecutorRestClient;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
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

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        /*
         * In Quartz job execution context, no spring context available, make use of the
         * ApplicationContextProvider to perform dependency injection!!
         */
         ApplicationContextProvider.processInjection(this);

        //发送任务消息到对应的执行器,并更新为running状态
        //执行次数和status由执行器更新
        JobKey jobKey = context.getJobDetail().getKey();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Long serviceId = jobDataMap.getLong("serviceId");

        Map<String,Object> body = new HashMap<String,Object>();
        body.putAll(jobDataMap);

        try {
            log.info("job-schedule send execute request to executor, dataserviceId :" + serviceId);
            executorRestClient.deployDataservice(body);
        } catch (Exception e) {
            log.error("call rest api throw Exception: " + serviceId, e);
            //--------------TODO-------如果执行启动失败怎么办-------------
            //更新dataservice为可执行状态
            try {
                dataServiceEntityService.updateDataServiceRunningStatus(serviceId, ScheduleJobStatus.JOB_STATUS_FAILED);
                log.info("dataService [{}] deploy failed, restore running status to 0", serviceId);
            }catch (Exception ex){
                log.error("dataService ["+ serviceId+ "] deploy failed, restore running status exception: ", e);
            }
        }
    }

}
