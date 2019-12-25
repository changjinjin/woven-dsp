package com.info.baymax.dsp.job.exec.rest;

import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.dsp.data.consumer.constant.DataServiceMode;
import com.info.baymax.dsp.data.consumer.constant.DataServiceType;
import com.info.baymax.dsp.data.consumer.constant.ExecutorFlowConf;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
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
import com.info.baymax.dsp.job.exec.message.sender.PlatformServerRestClient;
import com.info.baymax.dsp.job.exec.util.FlowGenUtil;
import com.info.baymax.dsp.data.dataset.entity.core.*;
import com.info.baymax.dsp.job.exec.util.HdfsUtil;
import lombok.extern.slf4j.Slf4j;
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
    DataApplicationService dataApplicationService;
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
        Long tenantId = Long.parseLong(body.get("tenantId")+"");
        Long owner = Long.parseLong(body.get("owner")+"");
        DataService dataService = JsonBuilder.getInstance().fromJson(body.get("dataService").toString(), DataService.class);

        if(type == DataServiceType.SERVICE_TYPE_PUSH){
            DataApplication application = dataApplicationService.findOne(dataService.getTenantId(), dataService.getApplicationId());
            DataResource dataResource = dataResourceService.findOne(application.getTenantId(),application.getDataResId());
            CustDataSource custDataSource = custDataSourceService.findOne(application.getTenantId(),application.getCustDataSourceId());
            executePushServiceAsync(dataService,application, dataResource, custDataSource);
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
    public void executePushServiceAsync(DataService dataService, DataApplication dataApplication, DataResource dataResource, CustDataSource custDataSource) {
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

            if(dataService.getJobInfo() != null && StringUtils.isNotEmpty(dataService.getJobInfo().getFlowId())){
                String flowId = dataService.getJobInfo().getFlowId();
                flowDesc = flowDescService.selectByPrimaryKey(flowId);
                if(flowDesc != null) {
                    List<FlowField> filterInputs = new ArrayList<>();
                    for (StepDesc step : flowDesc.getSteps()) {
                        if (step.getType().equals("filter")) {
                            List<Map<String, String>> fmap = (ArrayList<Map<String, String>>) step.getInputConfigurations().get(0).get("fields");
                            for (Map<String, String> f : fmap) {
                                FlowField field = new FlowField(f.get("column"), f.get("type"), f.get("alias"), f.get("description"));
                                filterInputs.add(field);
                            }
                            String filterCondition = flowGenUtil.getCondition(dataResource, dataApplication, dataService, filterInputs);
                            if (StringUtils.isNotEmpty(filterCondition)) {
                                step.getOtherConfigurations().put("condition", filterCondition);
                                //更新flowDesc
                                flowDescService.updateByPrimaryKey(flowDesc);
                            }
                            break;
                        }
                    }
                }else{
                    flowDesc = flowGenUtil.generateDataServiceFlow(dataService,dataApplication, dataResource, custDataSource);
                    dataService.getJobInfo().setFlowId(flowDesc.getId());
                    dataService.setLastModifiedTime(new Date());
                    dataServiceEntityService.saveOrUpdate(dataService);
                }
            }else{
                flowDesc = flowGenUtil.generateDataServiceFlow(dataService,dataApplication, dataResource, custDataSource);
                JobInfo jobInfo = new JobInfo();
                jobInfo.setFlowId(flowDesc.getId());
                dataService.setJobInfo(jobInfo);
                dataService.setLastModifiedTime(new Date());
                dataServiceEntityService.saveOrUpdate(dataService);
            }

            boolean commitSchedule = false;
            List<ConfigItem> runtimePros = platformServerRestClient.getRuntimeProperties(flowDesc.getId());
            FlowSchedulerDesc scheduler = flowGenUtil.generateScheduler(dataService, flowDesc, runtimePros);
            //提交任务到woven-server平台
            try {
                Response rs = platformServerRestClient.runScheduler(scheduler);
                if(rs.getStatus() == 200 || rs.getStatus() == 201) {
                    //schedule触发成功,更新Dataservice的schedulerId
                    dataService.getJobInfo().setScheduleId(scheduler.getId());
                    dataService.setLastModifiedTime(new Date());
                    dataServiceEntityService.updateByPrimaryKey(dataService);
                    commitSchedule = true;
                }
            }catch (Exception ex){
                log.error("send scheduler request error: ", ex);
            }

            try {
                //检查execution执行进度,超时退出, 获取增量字段的更新值
                Long startTime = System.currentTimeMillis();
                if(commitSchedule) {
                    try {//生成execution有时间差
                        Thread.sleep(30 * 1000L);
                    } catch (Exception e) {
                    }
                    while (true) {
                        List<FlowExecution> flowExecutions = flowExecutionService.findByFlowSchedulerId(scheduler.getId());
                        FlowExecution execution = null;
                        if (flowExecutions != null && flowExecutions.size() > 0) {
                            execution = flowExecutions.get(0);
                            if (execution.getStatus().getType().equals(Status.StatusType.SUCCEEDED.toString())) {
                                dataService.getJobInfo().setExecutionId(execution.getId());
                                dataService.setLastModifiedTime(new Date());
                                if (StringUtils.isNotEmpty(dataResource.getIncrementField()) && dataApplication.getServiceMode() == DataServiceMode.increment_mode) {
                                    String path = ExecutorFlowConf.dataset_cursor_dir + ExecutorFlowConf.dataset_cursor_file_prefix + dataService.getId();
                                    if (HdfsUtil.getInstance().exist(path)) {
                                        List<String> records = HdfsUtil.getInstance().read(path);
                                        if (records != null && records.size() > 0) {
                                            String cursorVal = records.get(records.size() - 1).trim();
                                            dataService.setCursorVal(cursorVal);
                                        }
                                    }
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

                        if (System.currentTimeMillis() - startTime > execution_timeout) {
                            log.warn("dataservice has timeout, id = {}, scheduler = {}, execution = {}", dataService.getId(), scheduler.getId(), execution != null ? execution.getId() : "");
                            break;
                        }
                    }
                }
            }catch (Exception e){

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


}
