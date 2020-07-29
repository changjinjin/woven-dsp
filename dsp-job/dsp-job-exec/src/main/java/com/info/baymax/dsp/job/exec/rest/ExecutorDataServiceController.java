package com.info.baymax.dsp.job.exec.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.info.baymax.common.utils.JsonUtils;
import com.info.baymax.dsp.data.consumer.constant.DataServiceType;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceService;
import com.info.baymax.dsp.job.exec.service.JobExecutorService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("dataservice")
@Slf4j
public class ExecutorDataServiceController {
    @Autowired
    private DataServiceService dataServiceEntityService;
    @Autowired
    private DataResourceService dataResourceService;
    @Autowired
    private CustDataSourceService custDataSourceService;
    @Autowired
    private DatasetService datasetService;

    @Autowired
    private JobExecutorService jobExecutorService;

    @PostMapping("/execute")
    public Mono<String> executeDataservice(@RequestBody Map<String, Object> body) {
        log.info("receive execute Dataservice-> {}", body);
        String jobName = body.get("jobName").toString();
        String jobGroup = body.get("jobGroup").toString();
        Integer type = Integer.parseInt(body.get("type")+"");
        String schedulerType = body.get("scheduleType")+"";
        Long serviceId = Long.parseLong(body.get("serviceId")+"");
        String tenantId = body.get("tenantId")+"";
        String owner = body.get("owner")+"";
//        DataService dataService = JsonUtils.fromJson(body.get("dataService").toString(), DataService.class);

        DataService dataService = dataServiceEntityService.findOne(tenantId, serviceId);
        if(type == DataServiceType.SERVICE_TYPE_PUSH && dataService != null){
            DataResource dataResource = dataResourceService.findOne(dataService.getTenantId(),dataService.getApplyConfiguration().getDataResId());
            Dataset dataset = datasetService.findOne(dataResource.getDatasetId());
            CustDataSource custDataSource = custDataSourceService.findOne(dataService.getTenantId(),dataService.getApplyConfiguration().getCustDataSourceId());
            jobExecutorService.executePushServiceAsync(dataset, dataService, dataResource, custDataSource);
        }
        log.info("response to job-scheduler OK, dataService id = "+ serviceId);
        return Mono.just("OK");
    }

    /**
     * 暂时先用不到,Dataservice失效时允许执行完最后一次
     * @param executionId
     * @param body
     * @return
     */
    @PostMapping("/kill/{executionId}")
    public Mono<String> killDataservice(@PathVariable("executionId") String executionId, @RequestBody Map<String, Object> body) {
        jobExecutorService.killServiceAsync(executionId, body);
        return Mono.just("OK");
    }


    public static void main(String[] args){
        String runtimeStr = "[{\"name\":\"all.debug\",\"value\":\"false\",\"input\":\"false\"},{\"name\":\"all.dataset-nullable\",\"value\":\"false\",\"input\":\"false\"},{\"name\":\"all.optimized.enable\",\"value\":\"true\",\"input\":\"true\"},{\"name\":\"all.lineage.enable\",\"value\":\"true\",\"input\":\"true\"},{\"name\":\"all.debug-rows\",\"value\":\"20\",\"input\":\"20\"},{\"name\":\"all.runtime.cluster-id\",\"value\":[\"random\",\"cluster1\"],\"input\":[\"random\",\"cluster1\"]},{\"name\":\"dataflow.master\",\"value\":\"yarn\",\"input\":\"yarn\"},{\"name\":\"dataflow.deploy-mode\",\"value\":[\"client\",\"cluster\"],\"input\":[\"client\",\"cluster\"]},{\"name\":\"dataflow.queue\",\"value\":[\"default\"],\"input\":[\"default\"]},{\"name\":\"dataflow.num-executors\",\"value\":\"2\",\"input\":\"2\"},{\"name\":\"dataflow.driver-memory\",\"value\":\"512M\",\"input\":\"512M\"},{\"name\":\"dataflow.executor-memory\",\"value\":\"1G\",\"input\":\"1G\"},{\"name\":\"dataflow.executor-cores\",\"value\":\"2\",\"input\":\"2\"},{\"name\":\"dataflow.verbose\",\"value\":\"true\",\"input\":\"true\"},{\"name\":\"dataflow.local-dirs\",\"value\":\"\",\"input\":\"\"},{\"name\":\"dataflow.sink.concat-files\",\"value\":\"true\",\"input\":\"true\"}]";
        List<Object> list = JsonUtils.fromJson(runtimeStr, List.class);
        System.out.println(list.size());
    }


}
