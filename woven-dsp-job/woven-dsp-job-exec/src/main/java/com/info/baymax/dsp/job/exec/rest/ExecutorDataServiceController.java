package com.info.baymax.dsp.job.exec.rest;

import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.dataset.entity.core.DataField;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.entity.core.FlowDesc;
import com.info.baymax.dsp.data.dataset.entity.core.StepDesc;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.dataset.service.core.SchemaService;
import com.info.baymax.dsp.data.dataset.utils.ConstantInfo;
import com.info.baymax.dsp.data.dataset.utils.Flows;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import com.info.baymax.dsp.job.exec.reader.CommonReader;
import com.info.baymax.dsp.job.exec.service.ReaderAndWriterLoader;
import com.info.baymax.dsp.job.exec.writer.CommonWriter;
import com.info.baymax.dsp.job.sch.constant.ServiceTypes;
import com.info.baymax.dsp.data.dataset.entity.core.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.ws.rs.NotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

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
    @Autowired
    DatasetService datasetService;
    @Autowired
    SchemaService schemaService;

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
            CommonWriter writer = ReaderAndWriterLoader.getWriter(custDataSource.getType());

            //-------------TODO----flow generate------------
/*          根据dataResource组成Source step,
            根据custDataSource组成Sink step
            根据各种配置组成transform step
            Transform:
              blank: '' as colum
              mix: replace(".","*")
              encrypt: encrypt()
            Filter: 处理增量情况, 每次的变量值 -----TODO---- 能不能当一个变量传进去
              date, timestamp类型
              number类型：

            提交flow后返回一个executionId,保存到DB。

            flow加一个标识,标识它是共享数据flow,完成后给平台和用户发一个通知,列表显示通知。
*/



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
