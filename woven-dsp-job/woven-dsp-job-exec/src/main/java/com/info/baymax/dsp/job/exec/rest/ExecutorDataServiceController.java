package com.info.baymax.dsp.job.exec.rest;

import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.dsp.data.consumer.constant.DataServiceMode;
import com.info.baymax.dsp.data.consumer.constant.DataServiceType;
import com.info.baymax.dsp.data.consumer.constant.ExecutorFlowConf;
import com.info.baymax.dsp.data.consumer.constant.ScheduleJobStatus;
import com.info.baymax.dsp.data.consumer.constant.ScheduleType;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import com.info.baymax.dsp.data.dataset.entity.ConfigItem;
import com.info.baymax.dsp.data.dataset.entity.Status;
import com.info.baymax.dsp.data.dataset.entity.core.FlowDesc;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.dataset.service.core.FlowDescService;
import com.info.baymax.dsp.data.dataset.service.core.FlowExecutionService;
import com.info.baymax.dsp.data.dataset.service.core.FlowSchedulerDescService;
import com.info.baymax.dsp.data.dataset.service.core.SchemaService;
import com.info.baymax.dsp.data.platform.bean.JobInfo;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import com.info.baymax.dsp.data.sys.entity.security.*;
import com.info.baymax.dsp.data.sys.service.security.TenantService;
import com.info.baymax.dsp.data.sys.service.security.UserService;
import com.info.baymax.dsp.job.exec.message.sender.PlatformServerRestClient;
import com.info.baymax.dsp.job.exec.util.FlowGenUtil;
import com.info.baymax.dsp.data.dataset.entity.core.*;
import com.info.baymax.dsp.job.exec.util.HdfsUtil;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dataservice")
@Slf4j
public class ExecutorDataServiceController {
    @Autowired
    DataServiceEntityService dataServiceEntityService;
    @Autowired
    DataResourceService dataResourceService;
    @Autowired
    CustDataSourceService custDataSourceService;
    @Autowired
    FlowDescService flowDescService;
    @Autowired
    DatasetService datasetService;
    @Autowired
    SchemaService schemaService;
    @Autowired
    FlowGenUtil flowGenUtil;
    @Autowired
    FlowSchedulerDescService flowSchedulerDescService;
    @Autowired
    private PlatformServerRestClient platformServerRestClient;
    @Autowired
    private FlowExecutionService flowExecutionService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private UserService userService;

    @Value(value = "${execution.checkout.timeout:1800000}")
    private Long execution_timeout = 30 * 60 * 1000L;

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
//        DataService dataService = JsonBuilder.getInstance().fromJson(body.get("dataService").toString(), DataService.class);

        DataService dataService = dataServiceEntityService.findOne(tenantId, serviceId);
        if(type == DataServiceType.SERVICE_TYPE_PUSH && dataService != null){
            DataResource dataResource = dataResourceService.findOne(dataService.getTenantId(),dataService.getApplyConfiguration().getDataResId());
            Dataset dataset = datasetService.findOne(dataResource.getDatasetId());
            CustDataSource custDataSource = custDataSourceService.findOne(dataService.getTenantId(),dataService.getApplyConfiguration().getCustDataSourceId());
            executePushServiceAsync(dataset, dataService, dataResource, custDataSource);
        }

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
        killServiceAsync(executionId, body);
        return Mono.just("OK");
    }

    @Async
    public void executePushServiceAsync(Dataset dataset, DataService dataService, DataResource dataResource, CustDataSource custDataSource) {
        try {
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
            FlowDesc flowDesc = null;
            String clusterId = dataset.getStorageConfigurations().get("clusterId");
            if(dataService.getJobInfo() != null && StringUtils.isNotEmpty(dataService.getJobInfo().getFlowId())){
                String flowId = dataService.getJobInfo().getFlowId();
                flowDesc = flowDescService.selectByPrimaryKey(flowId);
                if(flowDesc != null) {
                    List<FlowField> filterInputs = new ArrayList<>();
                    for (StepDesc step : flowDesc.getSteps()) {
                        if (step.getType().equals("filter")) {
                            JSONArray fmap = (JSONArray) step.getInputConfigurations().get(0).get("fields");
                            for (Object obj : fmap) {
                                FlowField field = JsonBuilder.getInstance().fromJson(JsonBuilder.getInstance().toJson(obj), FlowField.class);
                                filterInputs.add(field);
                            }
                            String filterCondition = flowGenUtil.getCondition(dataResource, dataService, filterInputs);
                            if (StringUtils.isNotEmpty(filterCondition)) {
                                step.getOtherConfigurations().put("condition", filterCondition);
                                //更新flowDesc
                                flowDescService.updateByPrimaryKey(flowDesc);
                            }
                            break;
                        }
                    }
                }else{
                    flowDesc = flowGenUtil.generateDataServiceFlow(dataService, dataResource, custDataSource);
                    dataService.getJobInfo().setFlowId(flowDesc.getId());
                    dataService.setLastModifiedTime(new Date());
                    dataServiceEntityService.saveOrUpdate(dataService);
                }
            }else{
                flowDesc = flowGenUtil.generateDataServiceFlow(dataService, dataResource, custDataSource);
                JobInfo jobInfo = new JobInfo();
                jobInfo.setFlowId(flowDesc.getId());
                dataService.setJobInfo(jobInfo);
                dataService.setLastModifiedTime(new Date());
                dataServiceEntityService.saveOrUpdate(dataService);
            }

            //initSaasContext()
            if(StringUtils.isEmpty(SaasContext.getCurrentUserId()) || StringUtils.isEmpty(SaasContext.getCurrentTenantId())){
                initSaasContext(flowDesc.getTenantId(), flowDesc.getOwner());
            }

            List<ConfigItem> runtimePros = null;
            try {
                runtimePros = platformServerRestClient.getRuntimeProperties(flowDesc.getId());
                log.info("runtimePros is null : " + String.valueOf(runtimePros==null));
            }catch (Exception e){
                log.error("connect platform to query runtime properties exception: " , e);
//                throw new RuntimeException("connect platform to query runtime properties exception: " , e);
            }

            //给个默认值
            if(runtimePros == null) {
                String runtimeStr = "[{\"name\":\"all.debug\",\"value\":\"false\",\"input\":\"false\"},{\"name\":\"all.dataset-nullable\",\"value\":\"false\",\"input\":\"false\"},{\"name\":\"all.optimized.enable\",\"value\":\"true\",\"input\":\"true\"},{\"name\":\"all.lineage.enable\",\"value\":\"true\",\"input\":\"true\"},{\"name\":\"all.debug-rows\",\"value\":\"20\",\"input\":\"20\"},{\"name\":\"all.runtime.cluster-id\",\"value\":[\"random\",\"cluster1\"],\"input\":[\"random\",\"cluster1\"]},{\"name\":\"dataflow.master\",\"value\":\"yarn\",\"input\":\"yarn\"},{\"name\":\"dataflow.deploy-mode\",\"value\":[\"client\",\"cluster\"],\"input\":[\"client\",\"cluster\"]},{\"name\":\"dataflow.queue\",\"value\":[\"default\"],\"input\":[\"default\"]},{\"name\":\"dataflow.num-executors\",\"value\":\"2\",\"input\":\"2\"},{\"name\":\"dataflow.driver-memory\",\"value\":\"512M\",\"input\":\"512M\"},{\"name\":\"dataflow.executor-memory\",\"value\":\"1G\",\"input\":\"1G\"},{\"name\":\"dataflow.executor-cores\",\"value\":\"2\",\"input\":\"2\"},{\"name\":\"dataflow.verbose\",\"value\":\"true\",\"input\":\"true\"},{\"name\":\"dataflow.local-dirs\",\"value\":\"\",\"input\":\"\"},{\"name\":\"dataflow.sink.concat-files\",\"value\":\"true\",\"input\":\"true\"}]";
                List<Map<String, Object>> list = (List<Map<String, Object>>) JsonBuilder.getInstance().fromJson(runtimeStr, List.class);
                runtimePros = new ArrayList<ConfigItem>();
                for (Map<String, Object> map : list) {
                    ConfigItem item = new ConfigItem(map.get("name").toString(), map.get("value"));
                    runtimePros.add(item);
                }
            }
            //Format runtime properties
            for(ConfigItem item : runtimePros){
                if(item.getValue() instanceof List || item.getValue() instanceof Object[]){
                    if(item.getName().equals("all.runtime.cluster-id") && StringUtils.isNotEmpty(clusterId)){
                          item.setValue(clusterId.toString());
                    }else {
                        String value = item.getValue().toString();
                        String[] vals = value.substring(1, value.length() - 1).split(",");
                        item.setValue(vals[0].trim());
                    }
                }
            }

            FlowSchedulerDesc scheduler = flowGenUtil.generateScheduler(dataService,custDataSource, flowDesc, runtimePros);
            log.info("flowDesc for DataService [{}] : {}", dataService.getId(), JsonBuilder.getInstance().toJson(flowDesc));
            log.info("scheduler for DataService [{}] : {}", dataService.getId(), JsonBuilder.getInstance().toJson(scheduler));


            //提交任务到woven-server平台
            try {
                platformServerRestClient.runScheduler(scheduler);
                //schedule触发成功,更新Dataservice的schedulerId
                dataService.getJobInfo().setScheduleId(scheduler.getId());
                dataService.setLastModifiedTime(new Date());
                dataServiceEntityService.updateByPrimaryKey(dataService);
            }catch (Exception ex){
                log.error("send scheduler request to platform exception: ", ex);
                //更新dataservice的isRunning为失败状态
                dataServiceEntityService.updateDataServiceRunningStatus(dataService.getId(), ScheduleJobStatus.JOB_STATUS_FAILED);
                log.error("send scheduler request to platform exception and restore dataService status : {}", dataService.getId());
                throw new RuntimeException("send scheduler request to platform exception: ", ex);
            }

            //检查execution执行进度,超时退出, 获取增量字段的更新值
            Long startTime = System.currentTimeMillis();
            try {
                //延迟30s再去检查execution
                Thread.sleep(30 * 1000L);
            } catch (Exception e) {
            }
            while (true) {
                try {
                    List<FlowExecution> flowExecutions = flowExecutionService.findByFlowSchedulerId(scheduler.getId());
                    FlowExecution execution = null;
                    if (flowExecutions != null && flowExecutions.size() > 0) {
                        execution = flowExecutions.get(0);
                        if (execution.getStatus().getType().equals(Status.StatusType.SUCCEEDED.toString())) {
                            dataService.getJobInfo().setExecutionId(execution.getId());
                            dataService.setLastModifiedTime(new Date());
                            if (StringUtils.isNotEmpty(dataResource.getIncrementField()) && dataService.getApplyConfiguration().getServiceMode() == DataServiceMode.increment_mode) {
                                String path = ExecutorFlowConf.dataset_cursor_dir + ExecutorFlowConf.dataset_cursor_file_prefix + dataService.getId();
                                if (HdfsUtil.getInstance().exist(path)) {
                                    List<String> records = HdfsUtil.getInstance().read(path);
                                    if (records != null && records.size() > 0) {
                                        String cursorVal = records.get(records.size() - 1).trim();
                                        dataService.setCursorVal(cursorVal);
                                    }
                                }
                            }

                            //更新isRunning状态
                            if(ScheduleType.SCHEDULER_TYPE_ONCE.equals(dataService.getScheduleType())) {
                                dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_SUCCEED);
                            }else if(ScheduleType.SCHEDULER_TYPE_CRON.equals(dataService.getScheduleType())){
                                dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_RUNNING);
                            }
                            dataServiceEntityService.updateByPrimaryKey(dataService);
                            break;
                        } else {
                            try {
                                Thread.sleep(30 * 1000L);
                            } catch (Exception e) {
                            }
                        }
                    }
                }catch (Exception e){

                }

                if (System.currentTimeMillis() - startTime > execution_timeout) {
                    log.warn("dataservice has timeout, id = {}, scheduler = {}", dataService.getId(), scheduler.getId());
                    dataServiceEntityService.updateDataServiceRunningStatus(dataService.getId(), ScheduleJobStatus.JOB_STATUS_FAILED);
                    break;
                }
            }

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

    public static void main(String[] args){
        String runtimeStr = "[{\"name\":\"all.debug\",\"value\":\"false\",\"input\":\"false\"},{\"name\":\"all.dataset-nullable\",\"value\":\"false\",\"input\":\"false\"},{\"name\":\"all.optimized.enable\",\"value\":\"true\",\"input\":\"true\"},{\"name\":\"all.lineage.enable\",\"value\":\"true\",\"input\":\"true\"},{\"name\":\"all.debug-rows\",\"value\":\"20\",\"input\":\"20\"},{\"name\":\"all.runtime.cluster-id\",\"value\":[\"random\",\"cluster1\"],\"input\":[\"random\",\"cluster1\"]},{\"name\":\"dataflow.master\",\"value\":\"yarn\",\"input\":\"yarn\"},{\"name\":\"dataflow.deploy-mode\",\"value\":[\"client\",\"cluster\"],\"input\":[\"client\",\"cluster\"]},{\"name\":\"dataflow.queue\",\"value\":[\"default\"],\"input\":[\"default\"]},{\"name\":\"dataflow.num-executors\",\"value\":\"2\",\"input\":\"2\"},{\"name\":\"dataflow.driver-memory\",\"value\":\"512M\",\"input\":\"512M\"},{\"name\":\"dataflow.executor-memory\",\"value\":\"1G\",\"input\":\"1G\"},{\"name\":\"dataflow.executor-cores\",\"value\":\"2\",\"input\":\"2\"},{\"name\":\"dataflow.verbose\",\"value\":\"true\",\"input\":\"true\"},{\"name\":\"dataflow.local-dirs\",\"value\":\"\",\"input\":\"\"},{\"name\":\"dataflow.sink.concat-files\",\"value\":\"true\",\"input\":\"true\"}]";
        List<Object> list = JsonBuilder.getInstance().fromJson(runtimeStr, List.class);
        System.out.println(list.size());
    }

    private void initSaasContext(String tenantId, String userId){
        SaasContext ctx = SaasContext.getCurrentSaasContext();
        Tenant tenant = null;
        if (StringUtils.isNotEmpty(tenantId)) {
            tenant = tenantService.selectByPrimaryKey(tenantId);
        }
        if (tenant == null) {
            return;
        }

        // 查询用户信息
        User user = null;
        if (StringUtils.isNotEmpty(userId)) {
            user = userService.selectByPrimaryKey(userId);
        }
        if (user == null) {
            return;
        }

        ctx.setTenantId(tenantId);
        ctx.setTenantName(tenant.getName());
        ctx.setUserId(userId);
        ctx.setUsername(user.getUsername());
    }


}
