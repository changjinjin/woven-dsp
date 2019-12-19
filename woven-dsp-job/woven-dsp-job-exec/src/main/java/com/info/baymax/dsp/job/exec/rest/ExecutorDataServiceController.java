package com.info.baymax.dsp.job.exec.rest;

import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import com.info.baymax.dsp.job.exec.reader.CommonReader;
import com.info.baymax.dsp.job.exec.service.ReaderAndWriterLoader;
import com.info.baymax.dsp.job.exec.writer.CommonWriter;
import com.info.baymax.dsp.job.sch.constant.ServiceTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("dataservice")
@Slf4j
public class ExecutorDataServiceController {
    @Autowired
    DataServiceEntityService dataServiceEntityService;
    @Autowired
    DataApplicationService dataApplicationService;
    @Autowired
    DataResourceService dataResourceService;
    @Autowired
    CustDataSourceService custDataSourceService;

    @PostMapping("/execute")
    public Mono<String> executeDataservice(@RequestBody Map<String, Object> body) {
        log.info("receive execute Dataservice-> {}", body);
        String jobName = body.get("jobName").toString();
        String jobGroup = body.get("jobGroup").toString();
        Integer type = (Integer) body.get("type");
        Integer schedulerType = (Integer)body.get("scheduleType");
        Long serviceId = (Long)body.get("serviceId");
        Long tenantId = (Long)body.get("tenantId");
        Long owner = (Long)body.get("owner");
        DataService dataService = JsonBuilder.getInstance().fromJson(body.get("dataService").toString(), DataService.class);

        if(type == ServiceTypes.SERVICE_TYPE_PUSH){
            DataApplication application = dataApplicationService.findOne(dataService.getTenantId(), serviceId);
            DataResource dataResource = dataResourceService.findOne(application.getTenantId(),application.getDataResId());
            CustDataSource custDataSource = custDataSourceService.findOne(application.getTenantId(),application.getCustDataSourceId());
            executePushServiceAsync(dataService,application, dataResource, custDataSource);
        }

        return Mono.just("OK");
    }

    @PostMapping("/kill/{executionId}")
    public Mono<String> killDataservice(@PathVariable("executionId") String executionId, @RequestBody Map<String, Object> body) {
        killServiceAsync(executionId, body);
        return Mono.just("OK");
    }

    @Async
    public void executePushServiceAsync(DataService dataService, DataApplication dataApplication, DataResource dataResource, CustDataSource custDataSource) {
        try {
            CommonReader reader = ReaderAndWriterLoader.getReader(dataResource.getStorage());


//            LogCollector.enableMDC("workflow", executionId);
            log.info("[executor] run flow rest request:: -> {}", JsonBuilder.getInstance().toJson(custDataSource));
//            messageHandler.deployWorkflow(event);
        } catch (Exception e){
            log.error("execute DataService "+ dataService.getId()+" exception:", e);
        } finally {
        }
    }

    @Async
    public void killServiceAsync(String executionId, Map<String, Object> event) {
        try {
//            LogCollector.enableMDC("workflow", executionId);
            log.info("[executor] kill flow rest request:: {} -> {}", executionId, JsonBuilder.getInstance().toJson(event));
//            messageHandler.killWorkflow(executionId, event);
        } finally {
//            LogCollector.disableMDC();
        }
    }


}
