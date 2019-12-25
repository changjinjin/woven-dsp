package com.info.baymax.dsp.job.exec.util;

import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.dsp.data.consumer.constant.DataServiceMode;
import com.info.baymax.dsp.data.consumer.constant.ExecutorFlowConf;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.dataset.entity.ConfigItem;
import com.info.baymax.dsp.data.dataset.entity.ConfigObject;
import com.info.baymax.dsp.data.dataset.entity.core.DataField;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.entity.core.FlowDesc;
import com.info.baymax.dsp.data.dataset.entity.core.FlowField;
import com.info.baymax.dsp.data.dataset.entity.core.FlowHistDesc;
import com.info.baymax.dsp.data.dataset.entity.core.FlowSchedulerDesc;
import com.info.baymax.dsp.data.dataset.entity.core.ParameterDesc;
import com.info.baymax.dsp.data.dataset.entity.core.Schema;
import com.info.baymax.dsp.data.dataset.entity.core.StepDesc;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.dataset.service.core.FlowDescService;
import com.info.baymax.dsp.data.dataset.service.core.FlowHistDescService;
import com.info.baymax.dsp.data.dataset.service.core.SchemaService;
import com.info.baymax.dsp.data.dataset.service.security.ResourceDescService;
import com.info.baymax.dsp.data.dataset.utils.ConstantInfo;
import com.info.baymax.dsp.data.dataset.utils.Flows;
import com.info.baymax.dsp.data.platform.bean.TransformRule;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataPolicyService;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class FlowGenUtil {

    private static Logger logger = LoggerFactory.getLogger(FlowGenUtil.class);

    @Autowired
    private DatasetService datasetService;
    @Autowired
    private ResourceDescService resourceDescService;
    @Autowired
    private FlowDescService flowDescService;
    @Autowired
    private SchemaService schemaService;
    @Autowired
    private DataResourceService dataResourceService;
    @Autowired
    private DataApplicationService dataApplicationService;
    @Autowired
    private DataServiceEntityService dataServiceEntityService;
    @Autowired
    private DataPolicyService dataPolicyService;
    @Autowired
    private CustDataSourceService custDataSourceService;
    @Autowired
    private FlowHistDescService flowHistDescService;

    /**
     * 根据source dataset的schema获取输入字段列表
     * @param schemaId
     * @return
     */
    private List<FlowField> getSchemaFiled(String schemaId){
        Schema s = schemaService.selectByPrimaryKey(schemaId);
        List<FlowField> result = new ArrayList<>();
        for (int i = 0; i < s.getFields().size(); i++) {
            DataField schField = s.getFields().get(i);
            FlowField flowField = new FlowField(schField.getName(), schField.getType(), "", schField.getDescription());
            result.add(flowField);
        }
        return result;
    }

    /**
     * 由输入字段列表和过滤关系获取输出字段
     * @param inputFields
     * @param fieldMappings
     * @return
     */
    public List<FlowField> getSourceOutputFields(List<FlowField> inputFields, Map<String,String> fieldMappings) {
        if(fieldMappings == null || fieldMappings.size() == 0){
            return inputFields;
        }

        List<FlowField> result = new ArrayList<>();
        for (int i = 0; i < inputFields.size(); i++) {
            FlowField inputField = inputFields.get(i);
            if(fieldMappings.containsKey(inputField.getColumn())) {
                FlowField flowField = new FlowField(inputField.getColumn(), inputField.getType(), fieldMappings.get(inputField.getColumn()),inputField.getDescription());
                result.add(flowField);
            }
        }
        return result;
    }

    /**
     * 根据输出字段获取下一个step的输入字段
     * @param outputFields
     * @return
     */
    public List<FlowField> getInputFields(List<FlowField> outputFields) {
        List<FlowField> result = new ArrayList<>();
        for (int i = 0; i < outputFields.size(); i++) {
            FlowField outputField = outputFields.get(i);
            FlowField flowField = new FlowField(StringUtils.isNotEmpty(outputField.getAlias())? outputField.getAlias() : outputField.getColumn(), outputField.getType(), "", outputField.getDescription());
            result.add(flowField);
        }
        return result;
    }

    /**
     * 由输入字段列表和过滤关系获取输出字段
     * @param inputFields
     * @param fieldMappings
     * @return
     */
    public List<FlowField> getOutputFields(List<FlowField> inputFields, Map<String,String> fieldMappings) {
        List<FlowField> result = new ArrayList<>();
        if(fieldMappings == null || fieldMappings.size() == 0){
            for (int i = 0; i < inputFields.size(); i++) {
                FlowField inputField = inputFields.get(i);
                FlowField flowField = new FlowField(inputField.getAlias(), inputField.getType(), fieldMappings.get(inputField.getColumn()),inputField.getDescription());
                result.add(flowField);
            }
            return inputFields;
        }

        for (int i = 0; i < inputFields.size(); i++) {
            FlowField inputField = inputFields.get(i);
            if(fieldMappings.containsKey(inputField.getColumn())) {
                FlowField flowField = new FlowField(inputField.getColumn(), inputField.getType(), fieldMappings.get(inputField.getColumn()),inputField.getDescription());
                result.add(flowField);
            }
        }
        return result;
    }


    private StepDesc getSourceStep(String stepId, Dataset cdo, List<FlowField> outputFields) throws Exception {
        Flows.StepBuilder sourceStep = null;
        sourceStep = Flows.step("source", stepId, stepId);
        StepDesc step = sourceStep
                .config("schema", cdo.getSchema().getName()).config("schemaId", cdo.getSchema().getId())
                .config("dataset", cdo.getName()).config("datasetId", cdo.getId())
                .config("sessionCache","").config("ignoreMissingPath", false).config("interceptor", "")
                .withPosition(129, 216).output(outputFields).build();
        step.setInputConfigurations(null);// steps规范:(左）无input （右）多个output

        return step;
    }

    private String getDateStr(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    private StepDesc getFilterStep(String stepId, List<FlowField> inputFields, List<FlowField> outputFields, String condition) throws Exception {
        Flows.StepBuilder filterStep = Flows.step("filter", stepId, stepId);
        StepDesc step = filterStep.config("condition", condition)
                .config("sessionCache","").config("interceptor", "")
                .withPosition(330, 75).input(inputFields).output(outputFields).build();
        return step;
    }

    private StepDesc getTransformStep(String stepId, DataService dataService, List<FlowField> inputFields, List<FlowField> outputFields) throws Exception{
        Flows.StepBuilder transStep = Flows.step("transform", stepId, stepId);

        List<String> expressions = new ArrayList<>();
        Map<String, TransformRule> fieldConfiguration = dataService.getFieldConfiguration();
        Map<String,String> fieldMapping = new HashMap<>();
        for(FlowField field : inputFields){
            if(fieldConfiguration.containsKey(field.getColumn())){
                TransformRule rule = fieldConfiguration.get(field.getColumn());
                expressions.add(rule.getExpression());
                int index = rule.getExpression().indexOf(" as ");
                if(index == -1){
                    index = rule.getExpression().indexOf(" AS ");
                }
                String newField = rule.getExpression().substring(index+4).trim();
                fieldMapping.put(field.getColumn(), newField);
            }
        }
        if(expressions.size() > 0){
            transStep.config("expressions", expressions);
        }
        outputFields = new ArrayList<>();
        for(FlowField field : inputFields){
            if(fieldMapping.containsKey(field.getColumn())){
                FlowField output = new FlowField(fieldMapping.get(field.getColumn()),"string","",field.getDescription());
                outputFields.add(output);
            }else{
                outputFields.add(field);
            }
        }

        StepDesc step = transStep
                .config("sessionCache","").config("interceptor", "")
                .withPosition(585, 203).input(inputFields).output(outputFields).build();

        return step;

    }


    private StepDesc getSinkStep(String stepId, Dataset sourceDataset, DataService dataService, DataApplication dataApplication, List<FlowField> inputFields) throws Exception{
        CustDataSource custDataSource = custDataSourceService.findOne(dataApplication.getTenantId(),dataApplication.getCustDataSourceId());
        Flows.StepBuilder stepBuilder = Flows.step("sink", stepId, stepId);
        Iterator<Map.Entry<String,Object>> iter = custDataSource.getAttributes().entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry<String,Object> entry = iter.next();
            String key = entry.getKey();
            Object val = entry.getValue();
            stepBuilder.config(key, val);
        }
        if(!custDataSource.getAttributes().containsKey("mode")){
            stepBuilder.config("mode", "append");
        }
        Schema schema = schemaService.findOneByName(sourceDataset.getTenantId(), ExecutorFlowConf.schema_sink_prefix +dataService.getId());
        Dataset dataset = datasetService.findOneByName(sourceDataset.getTenantId(), ExecutorFlowConf.dataset_sink_prefix +dataService.getId());
        String schemaId = "";
        String datasetId = "";
        if(schema != null){
            schemaId = schema.getId();
        }
        if(dataset != null){
            datasetId = dataset.getId();
        }
        if(StringUtils.isNotEmpty(schemaId) && StringUtils.isNotEmpty(datasetId)){
            stepBuilder.config("autoSchema","false");
        }else{
            stepBuilder.config("autoSchema","true");
        }

        stepBuilder.config("schemaId", schemaId).config("schema", ExecutorFlowConf.schema_sink_prefix +dataService.getId()).config("datasetId", datasetId).config("dataset", ExecutorFlowConf.dataset_sink_prefix + dataService.getId());
        StepDesc step = stepBuilder.withPosition(585, 203).input(inputFields).build();
        return step;
    }

    private StepDesc getSQLStep(String stepId, DataResource dataResource, List<FlowField> inputFields, List<FlowField> outputFields) throws Exception{
        StepDesc step = null;
        for(FlowField field : inputFields){
            if(field.getColumn().equals(dataResource.getIncrementField())){
                Flows.StepBuilder stepBuilder = Flows.step("sql", stepId, stepId);
                stepBuilder
                        .config("sessionCache","").config("interceptor", "")
                        .config("sql","select max("+dataResource.getIncrementField()+") as cursorVal from input");
                outputFields = new ArrayList<>();
                outputFields.add(new FlowField("cursorVal","string","", ""));
                step = stepBuilder.withPosition(700,290).input(inputFields).output(outputFields).build();
                return step;
            }
        }
        log.info("not found incrementField from output fields: {} ", dataResource.getIncrementField());

        return step;
    }

    private StepDesc getSqlSinkStep(String stepId, Dataset sourceDS, DataService dataService,List<FlowField> inputFields){
        Flows.StepBuilder stepBuilder = Flows.step("sink", stepId, stepId);
        Schema schema = schemaService.findOneByName(sourceDS.getTenantId(), ExecutorFlowConf.schema_cursor_name);
        Dataset dataset = datasetService.findOneByName(sourceDS.getTenantId(), ExecutorFlowConf.dataset_cursor_prefix + dataService.getId());
        //没有则创建
        String schemaId = "";
        String datasetId = "";
        if(schema != null){
            schemaId = schema.getId();
        }
        if(dataset != null){
            datasetId = dataset.getId();
        }
        if(StringUtils.isNotEmpty(schemaId) && StringUtils.isNotEmpty(datasetId)){
            stepBuilder.config("autoSchema","false");
        }else{
            stepBuilder.config("autoSchema","true");
        }
        stepBuilder.config("mode", "overwrite");

        stepBuilder.config("schemaId", schemaId).config("schema", ExecutorFlowConf.schema_cursor_name).config("datasetId", datasetId).config("dataset", ExecutorFlowConf.dataset_cursor_prefix + dataService.getId());
        stepBuilder.config("quoteChar", "\"")
                .config("escapeChar", "\\")
                .config("schemaVersion", "")
                .config("sliceTimeColumn", "")
                .config("format", "csv")
                .config("description", "")
                .config("maxFileSize", "")
                .config("type", "HDFS")
                .config("maxFileNumber", "")
                .config("autoSchema", "true")
                .config("separator", "")
                .config("nullValue", "")
                .config("expiredTime", "0")
                .config("mode", "overwrite")
                .config("path", ExecutorFlowConf.dataset_cursor_dir + ExecutorFlowConf.dataset_cursor_file_prefix + dataService.getId())
                .config("countWrittenRecord", "")
                .config("sliceType", "");

        StepDesc step = stepBuilder.withPosition(912, 200).input(inputFields).build();
        return step;
    }

    public FlowDesc generateDataServiceFlow(DataService dataService, DataApplication dataApplication, DataResource dataResource, CustDataSource custDataSource)
        throws Exception {
        final String schema_current = ConstantInfo.QA_ANALYSIS_SCHEMA_CURRENT;
        final String schema_dir = ConstantInfo.DS_SCHEMA_DIR;
        final String flowType = "dataflow";

        String flowName = "dataservice_" + dataService.getId() + "_" + getDateStr("yyyyMMdd_HHmmss_SSS");
        Dataset sourceDS = null;
        try {
            sourceDS = datasetService.selectByPrimaryKey(dataResource.getDatasetId());
        }catch (Exception e){
            logger.error("", e);
        }

        if (sourceDS == null) {
            logger.error("getDataset by id error: id=" + dataResource.getDatasetId());
            throw new RuntimeException("getDataset by id error: id= " + dataResource.getDatasetId());
        }

        // 如果dataset有自己的元数据，则在这里查出来并设置上避免后面引用出错
        if (StringUtils.isNotEmpty(sourceDS.getSchemaId())) {
            sourceDS.setSchema(schemaService.selectByPrimaryKey(sourceDS.getSchemaId()));
        }

        //Source step input
        List<FlowField> sourceInputs = getSchemaFiled(sourceDS.getSchemaId());
        //source step output
        List<FlowField> sourceOutputs = getSourceOutputFields(sourceInputs, dataResource.getFieldMappings());
        //filter step input
        List<FlowField> filterInputs = getInputFields(sourceOutputs);
        //filter step output
        List<FlowField> filterOutputs = filterInputs;
        //transform step input
        List<FlowField> transInputFields = filterOutputs;

        // 获取source step,赋值datasetId,schemaId
        StepDesc sourceStep = getSourceStep("source_1", sourceDS, sourceOutputs);

        //构建filter step
        StepDesc filterStep = null;
        String condition = getCondition(dataResource, dataApplication, dataService, sourceOutputs);
        filterStep = getFilterStep("filter_2", filterInputs, filterOutputs ,condition);

        //构建transform step
        StepDesc transStep = null;
        List<FlowField> outputFileds = null;
        if(dataService.getFieldConfiguration()!=null && dataService.getFieldConfiguration().size()>0){
            outputFileds = new ArrayList<>();
            transStep = getTransformStep("transform_3", dataService, transInputFields, outputFileds);
        }else{
            outputFileds = transInputFields;
        }

        //构建sink step
        StepDesc sinkStep = getSinkStep("sink_4", sourceDS, dataService, dataApplication, outputFileds);

        //构建sql step和sink step 2
        StepDesc sqlStep = null;
        StepDesc sinkStep_2 = null;
        if(StringUtils.isNotEmpty(dataResource.getIncrementField()) && dataApplication.getServiceMode() == DataServiceMode.increment_mode){
            List<FlowField> sqlOutFields = null;
            sqlStep = getSQLStep("sql_5", dataResource, outputFileds, sqlOutFields);
            if(sqlStep != null){
                sinkStep_2 = getSqlSinkStep("sink_6", sourceDS, dataService, sqlOutFields);
            }
        }

        Flows.FlowBuilder flowBuilder = Flows.dataflow(flowName);
        FlowDesc flow = null;
        try {
            if(sqlStep != null){
                if (transStep != null) {
                    flowBuilder.step(sourceStep).step(filterStep).step(transStep).step(sinkStep).step(sqlStep).step(sinkStep_2)
                            .connect("source_1", "filter_2").connect("filter_2", "transform_3").connect("transform_3", "sink_4")
                            .connect("transform_3", "sql_5").connect("sql_5", "sink_6");
                } else {
                    flowBuilder.step(sourceStep).step(filterStep).step(sinkStep).step(sqlStep).step(sinkStep_2)
                            .connect("source_1", "filter_2").connect("filter_2", "sink_4")
                            .connect("filter_2", "sql_5").connect("sql_5", "sink_6");
                }
            }else{
                if (transStep != null) {
                    flowBuilder.step(sourceStep).step(filterStep).step(transStep).step(sinkStep).connect("source_1", "filter_2")
                            .connect("filter_2", "transform_3").connect("transform_3", "sink_4");
                } else {
                    flowBuilder.step(sourceStep).step(filterStep).step(sinkStep).connect("source_1", "filter_2")
                            .connect("filter_2", "sink_4");
                }
            }

            flow = flowBuilder.build();
        }catch (Exception e){
            logger.error("build flow exception: ", e);
        }
        flow.setSource("dsflow");// 代表flow类型，生成的execution里携带这个属性
        if(custDataSource.getType().equalsIgnoreCase("jdbc") && custDataSource.getAttributes().containsKey("jarPath")){
            ParameterDesc param = new ParameterDesc();
            param.setName(custDataSource.getAttributes().getOrDefault("jarPath","").toString());
            param.setCategory("ref");
            List<ParameterDesc> depenList = new ArrayList<>();
            depenList.add(param);
            flow.setDependencies(depenList);
        }

        //dataservice flow都放在Flows/ds_flow目录下
        ResourceDesc flowResource = null;
        flowResource = resourceDescService.findRootsByName(sourceDS.getTenantId(), ConstantInfo.RESOURCE_DIR_ROOT_FLOW);
        ResourceDesc dataserviceFlowRes = resourceDescService.findByNameAndParent(sourceDS.getTenantId(), ConstantInfo.DS_OUTPUT_FLOW_DIR, flowResource.getId());

        if (dataserviceFlowRes == null) {
            ResourceDesc dsResDesc = new ResourceDesc();
            dsResDesc.setCreateTime(new Date());
            dsResDesc.setCreator(sourceDS.getCreator());
            dsResDesc.setEnabled(1);
            dsResDesc.setExpiredTime(0L);
            dsResDesc.setLastModifiedTime(dsResDesc.getCreateTime());
            dsResDesc.setLastModifier(sourceDS.getCreator());
            dsResDesc.setModuleVersion(0);
            dsResDesc.setName(ConstantInfo.DS_OUTPUT_FLOW_DIR);
            dsResDesc.setOwner(sourceDS.getOwner());
            dsResDesc.setVersion(0);
            dsResDesc.setResType(ConstantInfo.RES_TYPE_FLOW);
            dsResDesc.setTenantId(sourceDS.getTenantId());
            dsResDesc.setParentId(flowResource.getId());
            dsResDesc.setIsHide(1);// qa flow文件夹不显示在Flows目录下

            resourceDescService.saveOrUpdate(dsResDesc);
            dataserviceFlowRes = dsResDesc;
        }

        flow.setResource(dataserviceFlowRes);
        flow.setResourceId(dataserviceFlowRes.getId());
        flow.setTenantId(sourceDS.getTenantId());
        flow.setOwner(sourceDS.getOwner());
        flow.setDescription("dataserviceId:" + dataService.getId());
        flow.setOid("$null");
        flow.setIsHide(1);// qa flow不显示在Flows目录下

        logger.info("begin to save ds flow: " + JsonBuilder.getInstance().toJson(flow));
        FlowDesc flowCreated = flowDescService.saveOrUpdate(flow);
        logger.info("save ds flow success : id=" + flowCreated.getId());

        // flow创建成功，关联拷贝一份history记录
        logger.info("copy history flow begin ...");
        FlowHistDesc fh = copyToHistory(flowCreated, sourceDS);
        logger.info("copy history flow success: " + JsonBuilder.getInstance().toJson(fh));

        return flowCreated;
    }

    public String getCondition(DataResource dataResource, DataApplication dataApplication, DataService dataService, List<FlowField> sourceOutputs){
        String condition = "1 == 1";
        if(dataApplication.getServiceMode() == DataServiceMode.increment_mode) {
            if (StringUtils.isNotEmpty(dataResource.getIncrementField()) && StringUtils.isNotEmpty(dataService.getCursorVal())) {
                String incrementField = dataResource.getIncrementField();
                for (FlowField flowField : sourceOutputs) {
                    if (flowField.getColumn().equals(incrementField)) {
                        if (flowField.getType().equals("int") || flowField.getType().equals("short") || flowField.getType().equals("bigint")) {
                            condition = incrementField + " > " + dataService.getCursorVal();
                        } else if (flowField.getType().equals("date")) {
                            condition = incrementField + " > to_timestamp('" + dataService.getCursorVal() + "')";//支持yyyy-mm-dd格式字符串,已测试
                        } else if (flowField.getType().equals("timestamp")) {
                            condition = incrementField + " > to_timestamp('" + dataService.getCursorVal() + "')";//支持yyyy-mm-dd HH:MM:SS格式字符串
                        } else {
                            condition = incrementField + " > " + dataService.getCursorVal();
                        }
                        break;
                    }
                }
            } else if (StringUtils.isNotEmpty(dataResource.getIncrementField())) {
                String incrementField = dataResource.getIncrementField();
                condition = incrementField + " IS NOT NULL";
            }
        }
        return condition;
    }

    private FlowHistDesc copyToHistory(FlowDesc flow, Dataset sourceDS) {
        FlowHistDesc fh = new FlowHistDesc();
        BeanUtils.copyProperties(flow, fh);
        Integer version = flowHistDescService.findMaxVersionByFlowId(flow.getId());
        if (version == null) {
            fh.setVersion(fh.getVersion());
        } else {
            fh.setVersion(version + 1);
        }
        fh.setId(null);
        fh.setOid(flow.getId());
        fh.setTenantId(sourceDS.getTenantId());
        fh = flowHistDescService.saveOrUpdate(fh);
        return fh;
    }

    public FlowSchedulerDesc generateScheduler(DataService dataService, FlowDesc flowDesc, List<ConfigItem> runtime_properties){
        String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String name = "ds_"+ dataService.getName()+ "_" + time;
        ConfigObject configurations = new ConfigObject();
        configurations.put("properties", runtime_properties);
        Long startTime = System.currentTimeMillis();
        try {
            startTime = Long.parseLong(dataService.getServiceConfiguration().get("startTime"));
        } catch (NumberFormatException ex) {
            log.error("NumberFormatException: For input string : {}", dataService.getServiceConfiguration().get("startTime"));
        }
        configurations.put("startTime", startTime);
        FlowSchedulerDesc scheduler = new FlowSchedulerDesc(name, "dsflow", "once", flowDesc.getId(), flowDesc.getName(), configurations);
        scheduler.setFlowType("dataflow");
        scheduler.setTotalExecuted(0);
        scheduler.setId(UUID.randomUUID().toString());

        logger.info("generate dataservice {} scheduler success : {}", dataService.getId(), JsonBuilder.getInstance().toJson(scheduler) );

        return scheduler;
    }

}
