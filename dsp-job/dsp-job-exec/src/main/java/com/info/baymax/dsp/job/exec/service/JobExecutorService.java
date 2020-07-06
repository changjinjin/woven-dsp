package com.info.baymax.dsp.job.exec.service;

import com.alibaba.fastjson.JSONArray;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.data.elasticsearch.entity.DataTransferRecord;
import com.info.baymax.dsp.data.consumer.constant.DataServiceMode;
import com.info.baymax.dsp.data.consumer.constant.DataServiceStatus;
import com.info.baymax.dsp.data.consumer.constant.ScheduleJobStatus;
import com.info.baymax.dsp.data.consumer.constant.ScheduleType;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.dataset.entity.ConfigItem;
import com.info.baymax.dsp.data.dataset.entity.Status;
import com.info.baymax.dsp.data.dataset.entity.core.*;
import com.info.baymax.dsp.data.dataset.service.core.ClusterDbService;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.dataset.service.core.FlowDescService;
import com.info.baymax.dsp.data.dataset.service.core.FlowExecutionService;
import com.info.baymax.dsp.data.platform.bean.GrowthType;
import com.info.baymax.dsp.data.platform.bean.JobInfo;
import com.info.baymax.dsp.data.platform.bean.TransferType;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import com.info.baymax.dsp.data.sys.entity.security.Customer;
import com.info.baymax.dsp.data.sys.service.security.CustomerService;
import com.info.baymax.dsp.job.exec.constant.ExecutorFlowConf;
import com.info.baymax.dsp.job.exec.message.sender.PlatformServerRestClient;
import com.info.baymax.dsp.job.exec.util.FlowGenUtil;
import com.info.baymax.dsp.job.exec.util.HdfsUtil;
import com.merce.woven.metrics.report.MetricsReporter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: haijun
 * @Date: 2020/4/28 20:46
 */
@Component
@Slf4j
public class JobExecutorService {
    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private MetricsReporter metricsReporter;
    @Autowired
    private DataServiceEntityService dataServiceEntityService;
    @Autowired
    private FlowDescService flowDescService;
    @Autowired
    private FlowGenUtil flowGenUtil;
    @Autowired
    private PlatformServerRestClient platformServerRestClient;
    @Autowired
    private FlowExecutionService flowExecutionService;
    @Autowired
    private ClusterDbService clusterDbService;

    @Autowired
    private DataApplicationService dataApplicationService;
    @Autowired
    private DataResourceService dataResourceService;
    @Autowired
    private DatasetService datasetService;
    @Autowired
    private CustomerService customerService;

    // 十分钟更新一次cluster信息,保证cluster最新修改生效
    static Cache<String, ClusterEntity> clusterCache = CacheBuilder.newBuilder().maximumSize(10)
        .expireAfterWrite(10, TimeUnit.MINUTES).build();
    // 存放最近使用的cluster修改时间
    static Map<String, Date> clusterRecords = new HashMap<String, Date>();

    @Value(value = "${execution.checkout.timeout:30}")
    private Long execution_timeout = 30L; // 单位：分钟

    @Async
    public void executePushServiceAsync(Dataset dataset, DataService dataService, DataResource dataResource,
                                        CustDataSource custDataSource) {
        ExampleQuery exampleQuery = null;
        try {
            // -------------TODO----flow generate------------
            /*
             * 根据dataResource组成Source step, 根据custDataSource组成Sink step 根据各种配置组成transform step Transform: blank: '' as
             * colum mix: replace(".","*") encrypt: encrypt() Filter: 处理增量情况, 每次的变量值 -----TODO---- 能不能当一个变量传进去 date,
             * timestamp类型 number类型：
             *
             * 提交flow后返回一个executionId,保存到DB。
             *
             * flow加一个标识,标识它是共享数据flow,完成后给平台和用户发一个通知,列表显示通知。
             */
            // 构建ExampleQuery更新DataService
            exampleQuery = ExampleQuery.builder(DataService.class).fieldGroup().andEqualTo("id", dataService.getId())
                .end();

            FlowDesc flowDesc = null;
            String clusterId = dataset.getStorageConfigurations().get("clusterId");
            if (dataService.getJobInfo() != null && StringUtils.isNotEmpty(dataService.getJobInfo().getFlowId())) {
                String flowId = dataService.getJobInfo().getFlowId();
                flowDesc = flowDescService.selectByPrimaryKey(flowId);
                if (flowDesc != null) {
                    List<FlowField> filterInputs = new ArrayList<>();
                    for (StepDesc step : flowDesc.getSteps()) {
                        if (step.getType().equals("filter")) {
                            JSONArray fmap = (JSONArray) step.getInputConfigurations().get(0).get("fields");
                            for (Object obj : fmap) {
                                FlowField field = JsonBuilder.getInstance()
                                    .fromJson(JsonBuilder.getInstance().toJson(obj), FlowField.class);
                                filterInputs.add(field);
                            }
                            String filterCondition = flowGenUtil.getCondition(dataResource, dataService, filterInputs);
                            if (StringUtils.isNotEmpty(filterCondition)) {
                                step.getOtherConfigurations().put("condition", filterCondition);
                                // 更新flowDesc
                                flowDescService.updateByPrimaryKey(flowDesc);
                            }
                            break;
                        }
                    }
                } else {
                    flowDesc = flowGenUtil.generateDataServiceFlow(dataService, dataResource, custDataSource);
                    dataService.getJobInfo().setFlowId(flowDesc.getId());
                    dataService.setLastModifiedTime(new Date());
                    dataService.setStatus(null);// 不更新status
                    dataServiceEntityService.updateByExampleSelective(dataService, exampleQuery);
                }
            } else {
                flowDesc = flowGenUtil.generateDataServiceFlow(dataService, dataResource, custDataSource);
                JobInfo jobInfo = new JobInfo();
                jobInfo.setFlowId(flowDesc.getId());
                dataService.setJobInfo(jobInfo);
                dataService.setLastModifiedTime(new Date());
                dataService.setStatus(null);// 不更新status
                dataServiceEntityService.updateByExampleSelective(dataService, exampleQuery);
            }

            // 设置上下文
            SaasContext.initSaasContext(dataService.getTenantId(), dataService.getOwner());

            List<ConfigItem> runtimePros = null;
            try {
                runtimePros = platformServerRestClient.getRuntimeProperties(flowDesc.getId());
                log.info("runtimePros is null : " + String.valueOf(runtimePros == null));
            } catch (Exception e) {
                log.error("connect platform to query runtime properties exception: ", e);
                // throw new RuntimeException("connect platform to query runtime properties exception: " , e);
            }

            // 给个默认值
            if (runtimePros == null) {
                String runtimeStr = "[{\"name\":\"all.debug\",\"value\":\"false\",\"input\":\"false\"},{\"name\":\"all.dataset-nullable\",\"value\":\"false\",\"input\":\"false\"},{\"name\":\"all.optimized.enable\",\"value\":\"true\",\"input\":\"true\"},{\"name\":\"all.lineage.enable\",\"value\":\"true\",\"input\":\"true\"},{\"name\":\"all.debug-rows\",\"value\":\"20\",\"input\":\"20\"},{\"name\":\"all.runtime.cluster-id\",\"value\":[\"random\",\"cluster1\"],\"input\":[\"random\",\"cluster1\"]},{\"name\":\"dataflow.master\",\"value\":\"yarn\",\"input\":\"yarn\"},{\"name\":\"dataflow.deploy-mode\",\"value\":[\"client\",\"cluster\"],\"input\":[\"client\",\"cluster\"]},{\"name\":\"dataflow.queue\",\"value\":[\"default\"],\"input\":[\"default\"]},{\"name\":\"dataflow.num-executors\",\"value\":\"2\",\"input\":\"2\"},{\"name\":\"dataflow.driver-memory\",\"value\":\"512M\",\"input\":\"512M\"},{\"name\":\"dataflow.executor-memory\",\"value\":\"1G\",\"input\":\"1G\"},{\"name\":\"dataflow.executor-cores\",\"value\":\"2\",\"input\":\"2\"},{\"name\":\"dataflow.verbose\",\"value\":\"true\",\"input\":\"true\"},{\"name\":\"dataflow.local-dirs\",\"value\":\"\",\"input\":\"\"},{\"name\":\"dataflow.sink.concat-files\",\"value\":\"true\",\"input\":\"true\"}]";
                List<Map<String, Object>> list = (List<Map<String, Object>>) JsonBuilder.getInstance()
                    .fromJson(runtimeStr, List.class);
                runtimePros = new ArrayList<ConfigItem>();
                for (Map<String, Object> map : list) {
                    ConfigItem item = new ConfigItem(map.get("name").toString(), map.get("value"));
                    runtimePros.add(item);
                }
            }
            // Format runtime properties
            for (ConfigItem item : runtimePros) {
                if (item.getValue() instanceof List || item.getValue() instanceof Object[]) {
                    if (item.getName().equals("all.runtime.cluster-id")) {
                        if (StringUtils.isNotEmpty(clusterId)) {
                            item.setValue(clusterId.toString());
                        } else {
                            String value = item.getValue().toString();
                            String[] vals = value.substring(1, value.length() - 1).split(",");
                            clusterId = vals[0].trim();
                            item.setValue(clusterId);
                        }
                    } else {
                        String value = item.getValue().toString();
                        String[] vals = value.substring(1, value.length() - 1).split(",");
                        item.setValue(vals[0].trim());
                    }
                }
            }

            FlowSchedulerDesc scheduler = flowGenUtil.generateScheduler(dataService, flowDesc, runtimePros);
            log.info("flowDesc for DataService [{}] : {}", dataService.getId(),
                JsonBuilder.getInstance().toJson(flowDesc));
            log.info("scheduler for DataService [{}] : {}", dataService.getId(),
                JsonBuilder.getInstance().toJson(scheduler));

            // 提交任务到woven-server平台
            try {
                platformServerRestClient.runScheduler(scheduler);
                // schedule触发成功,更新Dataservice的schedulerId
                dataService.getJobInfo().setScheduleId(scheduler.getId());
                dataService.setLastModifiedTime(new Date());
                // dataService.setExecutedTimes(countAllExecutions(flowDesc.getId()));
                dataService.setLastExecutedTime(new Date());
                dataService.setStatus(null);// 不更新status
                dataServiceEntityService.updateByExampleSelective(dataService, exampleQuery);
            } catch (Exception ex) {
                log.error("send scheduler request to platform exception and recover dataService [" + dataService.getId()
                    + "] status :", ex);
                // 更新dataservice的isRunning为失败状态
                dataServiceEntityService.updateDataServiceRunningStatus(dataService.getId(),
                    ScheduleJobStatus.JOB_STATUS_FAILED);
                throw new RuntimeException("send scheduler request to platform exception: ", ex);
            }

            // 清空上下文
            SaasContext.clear();

            // 检查execution执行进度,超时退出, 获取增量字段的更新值
            Long startTime = System.currentTimeMillis();
            try {
                // 延迟30s再去检查execution
                Thread.sleep(30 * 1000L);

                while (true) {
                    boolean statusComplete = false;
                    boolean statusSuccess = false;
                    List<FlowExecution> flowExecutions = flowExecutionService.findByFlowSchedulerId(scheduler.getId());
                    FlowExecution execution = null;
                    if (flowExecutions != null && flowExecutions.size() > 0) {
                        execution = flowExecutions.get(0);
                        if (!dataService.getJobInfo().equals(execution.getId())) {
                            dataService.getJobInfo().setExecutionId(execution.getId());
                            dataService.setLastModifiedTime(new Date());
                            dataService.setStatus(null);// 不更新status
                            dataServiceEntityService.updateByExampleSelective(dataService, exampleQuery);
                        }
                        log.info("execution {} for dataService {} status is : {}", execution.getId(),
                            dataService.getId(), execution.getStatus().getType());
                        if (execution.getStatus().getType().equals(Status.StatusType.SUCCEEDED.toString())) {
                            if (dataService.getApplyConfiguration().getServiceMode() == DataServiceMode.increment_mode
                                && StringUtils.isNotEmpty(dataResource.getIncrementField())
                                && isCursorFlow(flowDesc)) {
                                HdfsUtil hdfsUtil = null;
                                if (StringUtils.isEmpty(clusterId)) {
                                    if (HdfsUtil.hdfsMap.containsKey("empty")) {
                                        hdfsUtil = HdfsUtil.hdfsMap.get("empty");
                                    } else {
                                        hdfsUtil = new HdfsUtil();
                                        HdfsUtil.hdfsMap.put("empty", hdfsUtil);
                                    }
                                } else {
                                    ClusterEntity clusterEntity = clusterCache.getIfPresent(clusterId);
                                    if (clusterEntity == null) {
                                        clusterEntity = clusterDbService.findOneByName(dataService.getTenantId(),
                                            clusterId, true);
                                        if (clusterEntity != null) {
                                            clusterCache.put(clusterId, clusterEntity);
                                        }
                                    }
                                    boolean toUpdate = false;
                                    if (clusterRecords.containsKey(clusterId)) {
                                        if (clusterEntity.getLastModifiedTime() != clusterRecords.get(clusterId)) {
                                            toUpdate = true;
                                        }
                                    } else {
                                        clusterRecords.put(clusterId, clusterEntity.getLastModifiedTime());
                                        toUpdate = true;
                                    }

                                    if (toUpdate || !HdfsUtil.hdfsMap.containsKey(clusterId)) {
                                        log.info("toUpdate {}, hdfsMap contains {} {}", String.valueOf(toUpdate),
                                            clusterId, String.valueOf(HdfsUtil.hdfsMap.containsKey(clusterId)));
                                        hdfsUtil = new HdfsUtil(clusterEntity.getConfigFile());
                                        HdfsUtil.hdfsMap.put(clusterId, hdfsUtil);
                                    } else {
                                        hdfsUtil = HdfsUtil.hdfsMap.get(clusterId);
                                    }
                                }

                                try {
                                    String path = ExecutorFlowConf.dataset_cursor_tmp_dir + "/" + dataService.getId()
                                        + "/" + ExecutorFlowConf.dataset_cursor_file_dir;
                                    String[] files = hdfsUtil.files(path, new PathFilter() {
                                        @Override
                                        public boolean accept(Path path) {
                                            return path.getName().endsWith(".csv");
                                        }
                                    });
                                    if (files != null && files.length > 0 && hdfsUtil.exist(path + "/" + files[0])) {
                                        List<String> records = hdfsUtil.read(path + "/" + files[0]);
                                        if (records != null && records.size() > 0) {
                                            String cursorVal = records.get(records.size() - 1).trim();
                                            log.info("cursor value for dataservice {} is {}", dataService.getId(),
                                                cursorVal);
                                            if (StringUtils.isNotEmpty(cursorVal)) {
                                                dataService.setCursorVal(cursorVal);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    log.error("update cursor value for dataservice {} failed", dataService.getId());
                                }

                            }

                            // 更新isRunning状态
                            if (ScheduleType.SCHEDULER_TYPE_ONCE.equals(dataService.getScheduleType())
                                || ScheduleType.SCHEDULER_TYPE_EVENT.equals(dataService.getScheduleType())) {
                                dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_SUCCEED);
                            } else if (ScheduleType.SCHEDULER_TYPE_CRON.equals(dataService.getScheduleType())) {
                                dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_RUNNING);
                            }

                            dataService.setLastModifiedTime(new Date());
                            log.info("execution {} for dataService {} success.", execution.getId(),
                                dataService.getId());

                            // 执行成功后判断当前dataService的状态，否则要等到下一个周期执行时才能停止dataService trigger
                            DataService service = dataServiceEntityService.selectByPrimaryKey(dataService.getId());
                            if (service != null && service.getStatus() == DataServiceStatus.SERVICE_STATUS_STOPPED) {
                                log.info("dataService [{}] has stopped,so update job status to_stop",
                                    dataService.getId());
                                dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_TO_STOP);
                            }

                            statusSuccess = true;
                        } else if (execution.getStatus().isComplete()) {
                            log.error("execution {} for dataService {} has failed.", execution.getId(),
                                dataService.getId());
                            dataService.setIsRunning(2);
                            dataService.setLastModifiedTime(new Date());
                            statusComplete = true;
                        } else {
                            try {
                                Thread.sleep(30 * 1000L);
                            } catch (Exception e) {
                            }
                        }
                    } else {
                        try {
                            Thread.sleep(30 * 1000L);
                        } catch (Exception e) {
                        }
                    }

                    long endTime = System.currentTimeMillis();
                    if (endTime - startTime > execution_timeout * 60 * 1000L) {
                        // dataService.setFailedTimes(dataService.getFailedTimes()+1);//需要加锁
                        dataService.setExecutedTimes(countAllExecutions(flowDesc.getId()));
                        dataService.setFailedTimes(countFailedExecutions(flowDesc.getId()));
                        dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_FAILED);
                        dataService.setStatus(null);// 不更新status
                        dataServiceEntityService.updateByExampleSelective(dataService, exampleQuery);
                        log.error("dataservice has timeout, id = {}, scheduler = {}", dataService.getId(),
                            scheduler.getId());
                        pushRecord(dataService, execution, startTime, endTime, 1);
                        break;
                    } else if (statusSuccess) {
                        dataService.setExecutedTimes(countAllExecutions(flowDesc.getId()));
                        dataService.setStatus(null);// 不更新status
                        dataServiceEntityService.updateByExampleSelective(dataService, exampleQuery);
                        pushRecord(dataService, execution, startTime, endTime, 0);
                        break;
                    } else if (statusComplete) {
                        dataService.setExecutedTimes(countAllExecutions(flowDesc.getId()));
                        dataService.setFailedTimes(countFailedExecutions(flowDesc.getId()));
                        dataService.setStatus(null);// 不更新status
                        dataServiceEntityService.updateByExampleSelective(dataService, exampleQuery);
                        pushRecord(dataService, execution, startTime, endTime, 1);
                        break;
                    }
                }
                log.info("dataService {} finished for scheduler {}", dataService.getId(), scheduler.getId());

            } catch (Exception e) {
                log.error("dataservice " + dataService.getId() + " execute has exception", e);
                dataService.setExecutedTimes(countAllExecutions(flowDesc.getId()));
                dataService.setFailedTimes(countFailedExecutions(flowDesc.getId()));
                dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_FAILED);
                dataService.setLastModifiedTime(new Date());
                dataService.setStatus(null);// 不更新status
                dataServiceEntityService.updateByExampleSelective(dataService, exampleQuery);
            }

        } catch (Exception e) {
            log.error("execute DataService " + dataService.getId() + " exception:", e);
            dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_FAILED);
            dataService.setLastModifiedTime(new Date());
            dataService.setStatus(null);// 不更新status
            dataServiceEntityService.updateByExampleSelective(dataService, exampleQuery);
        } finally {
        }
    }

    @Async
    public void killServiceAsync(String executionId, Map<String, Object> event) {
        try {
            // LogCollector.enableMDC("workflow", executionId);
            log.info("[executor] kill flow rest request:: {} -> {}", executionId,
                JsonBuilder.getInstance().toJson(event));
            // messageHandler.killWorkflow(executionId, event);
        } finally {
            // LogCollector.disableMDC();
        }
    }

    private boolean isCursorFlow(FlowDesc flowDesc) {
        for (StepDesc step : flowDesc.getSteps()) {
            if (step.getType().equals("sink") && step.getId().equals("sink_6")) {
                return true;
            }
        }
        return false;
    }

    private Integer countAllExecutions(String flowId) {
        ExampleQuery query = ExampleQuery.builder(FlowExecution.class).fieldGroup(FieldGroup.builder().andEqualTo("flowId", flowId)
            .andEqualTo("tenantId", SaasContext.getCurrentTenantId()));
        return flowExecutionService.selectCount(query);
    }

    private Integer countFailedExecutions(String flowId) {
        ExampleQuery query = ExampleQuery.builder(FlowExecution.class).fieldGroup(FieldGroup.builder().andEqualTo("flowId", flowId)
            .andEqualTo("tenantId", SaasContext.getCurrentTenantId()).andEqualTo("statusType", "FAILED"));
        return flowExecutionService.selectCount(query);
    }

    public void pushRecord(DataService dataService, FlowExecution execution, long startTime, long endTime,
                           int resultCode) {
        try {
            DataApplication app = dataApplicationService.selectByPrimaryKey(dataService.getApplicationId());
            Customer customer = customerService.selectByPrimaryKey(dataService.getCustId());
            DataResource dataResource = dataResourceService.selectByPrimaryKey(dataService.getDataResId());
            if (dataResource == null) {
                return;
            }
            Dataset dataset = datasetService.selectByPrimaryKey(dataResource.getDatasetId());
            if (dataset == null) {
                return;
            }
            DataTransferRecord record = null;
            if (execution != null) {
                record = DataTransferRecord.builder()//
                    .sid(UUID.randomUUID().toString())//
                    .write_time(new Date())//
                    .datasetId(dataset.getId())///
                    .datasetName(dataset.getName())//
                    .custId(dataService.getCustId())//
                    .custName(customer.getName())//
                    .custAppId(app.getId())//
                    .custAppName(app.getName())//
                    .dataServiceId(dataService.getId())//
                    .dataServiceName(dataService.getName())//
                    .startTime(startTime)//
                    .endTime(endTime)//
                    .cost(execution.getCost())//
                    .transferType(TransferType.PUSH.name())//
                    .growthType(GrowthType.valueOf(dataService.getApplyConfiguration().getServiceMode()).name())//
                    .cursorVal(dataService.getCursorVal())//
                    .offset(0L)//
                    .records(execution.getOutputRecords())//
                    .resultCode(resultCode)//
                    .tenantId(app.getTenantId())//
                    .owner(app.getOwner()).build();
            } else {
                record = DataTransferRecord.builder()//
                    .sid(UUID.randomUUID().toString())//
                    .write_time(new Date())//
                    .datasetId(dataset.getId())///
                    .datasetName(dataset.getName())//
                    .custId(dataService.getCustId())//
                    .custName(customer.getName())//
                    .custAppId(app.getId())//
                    .custAppName(app.getName())//
                    .dataServiceId(dataService.getId())//
                    .dataServiceName(dataService.getName())//
                    .startTime(startTime)//
                    .endTime(endTime)//
                    .cost(endTime - startTime)//
                    .transferType(TransferType.PUSH.name())//
                    .growthType(GrowthType.valueOf(dataService.getApplyConfiguration().getServiceMode()).name())//
                    .cursorVal(dataService.getCursorVal())//
                    .offset(0L)//
                    .records(0L)//
                    .resultCode(1)//
                    .tenantId(app.getTenantId())//
                    .owner(app.getOwner()).build();
            }
            metricsReporter.report(DataTransferRecord.TYPE_NAME, appName, startTime, record);
        } catch (Exception e) {
            log.error("record push service error with dataServiceId:" + dataService.getId() + ",executionId:"
                + execution.getId(), e);
        }
    }
}
