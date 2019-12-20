package com.info.baymax.dsp.job.exec.util;

import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.dataset.entity.core.DataField;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.entity.core.FlowDesc;
import com.info.baymax.dsp.data.dataset.entity.core.FlowField;
import com.info.baymax.dsp.data.dataset.entity.core.Schema;
import com.info.baymax.dsp.data.dataset.entity.core.StepDesc;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.dataset.service.core.FlowDescService;
import com.info.baymax.dsp.data.dataset.service.core.SchemaService;
import com.info.baymax.dsp.data.dataset.service.resource.ResourceManagerService;
import com.info.baymax.dsp.data.dataset.service.security.ResourceDescService;
import com.info.baymax.dsp.data.dataset.utils.ConstantInfo;
import com.info.baymax.dsp.data.dataset.utils.Flows;
import com.info.baymax.dsp.data.platform.bean.TransformRule;
import com.info.baymax.dsp.data.platform.entity.DataPolicy;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataPolicyService;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.internal.jsr166.Flow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
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
    private ResourceManagerService resourceManagerService;
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


    private StepDesc getSourceStep(String stepId, Dataset cdo, List<FlowField> outputFields) {
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

    private StepDesc getFilterStep(String stepId, List<FlowField> inputFields, List<FlowField> outputFields, String condition) {
        Flows.StepBuilder filterStep = Flows.step("filter", stepId, stepId);
        StepDesc step = filterStep.config("condition", condition)
                .config("sessionCache","").config("interceptor", "")
                .withPosition(330, 75).input(inputFields).output(outputFields).build();
        return step;
    }

    private StepDesc getTransformStep(String stepId, DataService dataService, List<FlowField> inputFields, List<FlowField> outputFields){
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


    private StepDesc getSinkStep(String stepId, DataApplication dataApplication, List<FlowField> inputFields){
        CustDataSource custDataSource = custDataSourceService.findOne(dataApplication.getTenantId(),dataApplication.getCustDataSourceId());
        String type = custDataSource.getType();
        Map<String,String> configuration = custDataSource.getOtherConfiguration();

        Flows.StepBuilder sinkStep = Flows.step("sink", stepId, stepId).input(inputFields);
        for( : configuration)

                .config("storage", String.format("#{storage:%s}", "hdfs"))
                .config("format", String.format("#{format:%s}", "parquet"))
                .config("outputDatasetDir",
                        String.format("#{outputDatasetDir:%s}",
                                System.getProperty("zebra.analysis.output.datasetdir",
                                        ConstantInfo.QA_OUTPUT_BASE_DIR)))
                .config("outputBaseDir",
                        String.format("#{outputBaseDir:%s}",
                                System.getProperty("zebra.analysis.output.basedir",
                                        ConstantInfo.QA_OUTPUT_DATASET_DIR)))
                .config("schemaName", qa_schema_current).config("schema", qa_schema_current)
                .config("schemaId", schema.getId())
                .config("withOriginalFields", String.format("#{withOriginalFields:true}"))
                .config("sliceType", sourceDS.getSliceType()).config("sliceTime", sourceDS.getSliceTime())
                .config("expiredTime", "#{expiredTime:0}").config("sourceData", sourceDS.getName())
                .config("sourceDataCount", sourceDS.getRecordNumber()).config("sourceDataSize", sourceDS.getByteSize())
                .config("datasetName", "qa_sink_dataset_" + flowName)// 这里先设定name,后面进行创建
                .config("datasetId", UUID.randomUUID().toString()) // 这里先设定id,后面进行创建
                .withPosition(808, 79).build();

        StepDesc step = sinkStep.withPosition(585, 203).input(fields).output(fields).build();
        return step;
    }


    public FlowDesc genDataServiceFlow(DataService dataService, DataApplication dataApplication, DataResource dataResource, CustDataSource custDataSource)
        throws Exception {
        final String schema_current = ConstantInfo.QA_ANALYSIS_SCHEMA_CURRENT;
        final String schema_dir = ConstantInfo.QA_SCHEMA_DIE;
        final String flowType = "dataservice";

        String flowName = "dataservice_" + dataService.getId() + "_" + getDateStr("yyyyMMdd_HHmmss_SSS");

        Dataset sourceDS = datasetService.selectByPrimaryKey(dataResource.getDatasetId());
        if (sourceDS == null) {
            logger.error("getDataset by id error: id=" + dataResource.getDatasetId());
            throw new NotFoundException("getDataset by id error: id= " + dataResource.getDatasetId());
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
        String condition = "1 == 1";
        if(StringUtils.isNotEmpty(dataResource.getIncrementField()) && StringUtils.isNotEmpty(dataService.getCursorVal())){
            String incrementField = dataResource.getIncrementField();
            for(FlowField flowField : sourceOutputs){
                if(flowField.getColumn().equals(incrementField)){
                    if(flowField.getType().equals("int") || flowField.getType().equals("short") || flowField.getType().equals("bigint")) {
                        condition = incrementField + " > " + dataService.getCursorVal();
                    }else if(flowField.getType().equals("date")){
                        condition = incrementField + " > to_timestamp('" + dataService.getCursorVal() + "')";//支持yyyy-mm-dd格式字符串,已测试
                    }else if(flowField.getType().equals("timestamp")){
                        condition = incrementField + " > to_timestamp('" + dataService.getCursorVal() + "')";//支持yyyy-mm-dd HH:MM:SS格式字符串
                    }else{
                        condition = incrementField + " > " + dataService.getCursorVal();
                    }
                    break;
                }
            }
        }else if(StringUtils.isNotEmpty(dataResource.getIncrementField())){
            String incrementField = dataResource.getIncrementField();
            condition = incrementField + " IS NOT NULL";
        }
        filterStep = getFilterStep("filter_2", filterInputs, filterOutputs ,condition);

        //构建transform step
        List<FlowField> outputFileds = new ArrayList<>();
        StepDesc transStep = getTransformStep("transform_3", dataService, transInputFields, outputFileds);

        //构建sink step
        StepDesc sinkStep = getSinkStep("sink_4", dataApplication, outputFileds);
        // 获取qualityAnalysisSink step


        Flows.FlowBuilder flowBuilder;
        if ("streamflow".equals(model.getFlowType())) {
            flowBuilder = Flows.streamflow(flowName);
        } else {
            flowBuilder = Flows.dataflow(flowName);
        }

        FlowDesc flow = flowBuilder.step(sourceStep).step(step2).step(sinkStep).connect("s1", "s2")
            .connect("s2", "s3_sink").build();

        flow.setSource("qaflow");// 代表flow类型，生成的execution里携带这个属性

        // qa flow都放在Flows/qa_flow目录下
        ResourceDesc flowResource = null;
        flowResource = resourceDescService.findRootsByName(SaasContext.getCurrentTenantId(),
            ConstantInfo.RESOURCE_DIR_ROOT_FLOW);
        ResourceDesc qaFlow = resourceDescService.findByNameAndParent(SaasContext.getCurrentTenantId(),
            ConstantInfo.QA_OUTPUT_FLOW_DIR, flowResource.getId());

        if (qaFlow == null) {
            ResourceDesc qaResDesc = new ResourceDesc();
            qaResDesc.setCreateTime(new Date());
            qaResDesc.setCreator(SaasContext.getCurrentUser().getLoginId());
            qaResDesc.setEnabled(1);
            qaResDesc.setExpiredTime(0L);
            qaResDesc.setLastModifiedTime(qaResDesc.getCreateTime());
            qaResDesc.setLastModifier(SaasContext.getCurrentUserLoginId());
            qaResDesc.setModuleVersion(0);
            qaResDesc.setName(ConstantInfo.QA_OUTPUT_FLOW_DIR);
            qaResDesc.setOwner(SaasContext.getCurrentUserId());
            qaResDesc.setVersion(0);
            qaResDesc.setResType(ConstantInfo.RES_TYPE_FLOW);
            qaResDesc.setTenantId(SaasContext.getCurrentTenantId());
            qaResDesc.setParentId(flowResource.getId());
            qaResDesc.setIsHide(1);// qa flow文件夹不显示在Flows目录下

            resourceDescService.saveOrUpdate(qaResDesc);
            qaFlow = qaResDesc;
        }

        flow.setResource(qaFlow);
        flow.setTenantId(SaasContext.getCurrentTenantId());
        flow.setOwner(SaasContext.getCurrentUserId());

        return flow;
    }

    /**
     * remove useless <key,value> for a ConfigObject
     *
     * @param inputMap
     * @param extraKeysToRemove
     * @return
     */
    public Map removeMaintableProperties(Map<String, Object> inputMap, String[] extraKeysToRemove,
                                                boolean removeFromValues) {
        if (inputMap == null)
            return null;

        Map<String, Object> output = new HashMap<>(inputMap);

        if (removeFromValues) {
            for (String keyName : inputMap.keySet()) {
                removeMaintableProperties((Map) output.get(keyName), extraKeysToRemove);
            }
        } else {
            removeMaintableProperties(output, extraKeysToRemove);
        }

        return output;
    }

    /**
     * remove useless properties for a object-generated JSON string
     *
     * @param inputJson
     * @param extraKeysToRemove
     * @return
     */
    public Map removeMaintableProperties(String inputJson, String[] extraKeysToRemove,
                                                boolean removeFromValues) {
        return removeMaintableProperties(JsonBuilder.getInstance().fromJson(inputJson, ConfigObject.class),
            extraKeysToRemove, removeFromValues);
    }

    /**
     * remove useless <key,value> for elements in a List
     *
     * @param objectList
     * @param extraKeysToRemove
     * @return
     */
    public List removeMaintableProperties(List objectList, String[] extraKeysToRemove) {
        if (objectList == null)
            return null;

        List<Object> outputList = new ArrayList<>();
        for (Object obj : objectList) {
            outputList.add(removeMaintableProperties(JsonBuilder.getInstance().toJson(obj), extraKeysToRemove, false));
        }

        return outputList;
    }

    /**
     * remove useless <key,value> for a ConfigObject
     *
     * @param inputMap
     * @param extraKeysToRemove
     * @return
     */
    public Map removeMaintableProperties(Map inputMap, String[] extraKeysToRemove) {

        Map output = inputMap;

        if (inputMap == null)
            return null;

        output.remove("moduleVersion");
        output.remove("solrdocVersion");
        output.remove("nameDuplicatable");
        output.remove("expiredPeriod");
        output.remove("idPrefix");
        output.remove("tags");
        output.remove("lastModifier");
        output.remove("lastModifiedTime");
        output.remove("creator");
        output.remove("createTime");
        output.remove("description");
        output.remove("tenant");

        if (extraKeysToRemove != null && extraKeysToRemove.length > 0) {
            for (String keyName : extraKeysToRemove)
                output.remove(keyName);
        }

        return output;
    }

    /**
     * remove useless properties for a ConfigObject
     *
     * @param inputMap
     * @param extraKeysToRemove
     * @return
     */
    public String getJsonAfterRemoveProperties(Map inputMap, String[] extraKeysToRemove) {
        Map output = removeMaintableProperties(inputMap, extraKeysToRemove);
        return JsonBuilder.getInstance().toJson(output);
    }

}
