package com.info.baymax.dsp.job.sch.job;

import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import com.info.baymax.dsp.job.sch.client.ExecutorRestClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: haijun
 * @Date: 2019/12/18 18:14
 */
@Slf4j
@Component
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
         * 发送任务消息到对应的执行器,并更新为running状态
         *
         * 执行次数和status由执行器更新
         */
        JobKey jobKey = context.getJobDetail().getKey();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Long serviceId = jobDataMap.getLong("serviceId");

        Map<String,Object> body = new HashMap<String,Object>();
        body.putAll(jobDataMap);

        try {
            executorRestClient.deployDataservice(body);
        } catch (Exception e) {
            log.error("call rest api throw Exception:{}", serviceId, e);
            //--------------TODO-------如果执行启动失败怎么办-------------

        }
    }

//    @PostConstruct
    public void testRest(){
        try {
            Thread.sleep(60*1000);
        }catch (Exception e){
        }

        Map<String,Object> body = new HashMap<String,Object>();
        JobDataMap jdm = new JobDataMap();
        jdm.put("type", "push");
        jdm.put("scheduleType", "once");
        jdm.put("jobName", "1111000000");
        jdm.put("jobGroup", "111111_3224244");
        jdm.put("serviceId", "1111000000");
        jdm.put("tenantId", "424335353333");
        jdm.put("owner", "087776666999");
        jdm.put("dataService", "");
        body.putAll(jdm);
        executorRestClient.deployDataservice(body);
    }
}
