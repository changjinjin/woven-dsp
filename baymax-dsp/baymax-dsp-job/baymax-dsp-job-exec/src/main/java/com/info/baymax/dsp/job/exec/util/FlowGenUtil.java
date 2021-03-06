package com.info.baymax.dsp.job.exec.util;

import com.google.common.collect.Maps;
import com.info.baymax.common.utils.Snowflake;
import com.info.baymax.dsp.data.consumer.beans.source.DataSourceType;
import com.info.baymax.dsp.data.consumer.constant.DataServiceMode;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import com.info.baymax.dsp.data.dataset.bean.FieldMapping;
import com.info.baymax.dsp.data.dataset.bean.TransformRule;
import com.info.baymax.dsp.data.dataset.entity.ConfigItem;
import com.info.baymax.dsp.data.dataset.entity.core.*;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.dataset.service.core.FlowDescService;
import com.info.baymax.dsp.data.dataset.service.core.FlowHistDescService;
import com.info.baymax.dsp.data.dataset.service.core.SchemaService;
import com.info.baymax.dsp.data.dataset.service.security.ResourceDescService;
import com.info.baymax.dsp.data.dataset.utils.ConstantInfo;
import com.info.baymax.dsp.data.dataset.utils.Flows;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.job.exec.constant.ExecutorFlowConf;
import com.merce.woven.common.ConfigObject;
import com.merce.woven.common.FieldDesc;
import com.merce.woven.common.ParameterDesc;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    private CustDataSourceService custDataSourceService;
    @Autowired
    private FlowHistDescService flowHistDescService;

    /**
     * ??????source dataset???schema????????????????????????
     *
     * @param schemaId
     * @return
     */
    private List<FlowField> getSchemaFiled(String schemaId) {
        Schema s = schemaService.selectByPrimaryKey(schemaId);
        List<FlowField> result = new ArrayList<>();
        for (int i = 0; i < s.getFields().size(); i++) {
        	FieldDesc schField = s.getFields().get(i);
            FlowField flowField = new FlowField(schField.getName(), schField.getType(), "", schField.getDescription());
            result.add(flowField);
        }
        return result;
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param inputFields
     * @param fieldMappings
     * @return
     */
    public List<FlowField> getSourceOutputFields(List<FlowField> inputFields, List<FieldMapping> fieldMappings) {
        if(fieldMappings == null || fieldMappings.size() == 0){
            return inputFields;
        }
        Map<String,FieldMapping> resourceFields = new HashMap<>();
        for(FieldMapping fieldMapping : fieldMappings){
            if(StringUtils.isNotEmpty(fieldMapping.getTargetField())){
                resourceFields.put(fieldMapping.getSourceField(), fieldMapping);
            }
        }

        List<FlowField> result = new ArrayList<>();
        for (int i = 0; i < inputFields.size(); i++) {
            FlowField inputField = inputFields.get(i);
            if(resourceFields.containsKey(inputField.getColumn())) {
                FlowField flowField = new FlowField(inputField.getColumn(), resourceFields.get(inputField.getColumn()).getTargetType(), resourceFields.get(inputField.getColumn()).getTargetField(),inputField.getDescription());
                result.add(flowField);
            }
        }
        return result;
    }

    /**
     * ?????????????????????????????????step???????????????
     *
     * @param outputFields
     * @return
     */
    public List<FlowField> getInputFields(List<FlowField> outputFields) {
        List<FlowField> result = new ArrayList<>();
        for (int i = 0; i < outputFields.size(); i++) {
            FlowField outputField = outputFields.get(i);
            FlowField flowField = new FlowField(
                StringUtils.isNotEmpty(outputField.getAlias()) ? outputField.getAlias() : outputField.getColumn(),
                outputField.getType(), "", outputField.getDescription());
            result.add(flowField);
        }
        return result;
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param inputFields
     * @param fieldMappings
     * @return
     */
    public List<FlowField> getOutputFields(List<FlowField> inputFields, Map<String, String> fieldMappings) {
        List<FlowField> result = new ArrayList<>();
        if (fieldMappings == null || fieldMappings.size() == 0) {
            for (int i = 0; i < inputFields.size(); i++) {
                FlowField inputField = inputFields.get(i);
                FlowField flowField = new FlowField(inputField.getAlias(), inputField.getType(),
                    fieldMappings.get(inputField.getColumn()), inputField.getDescription());
                result.add(flowField);
            }
            return inputFields;
        }

        for (int i = 0; i < inputFields.size(); i++) {
            FlowField inputField = inputFields.get(i);
            if (fieldMappings.containsKey(inputField.getColumn())) {
                FlowField flowField = new FlowField(inputField.getColumn(), inputField.getType(),
                    fieldMappings.get(inputField.getColumn()), inputField.getDescription());
                result.add(flowField);
            }
        }
        return result;
    }

    private StepDesc getSourceStep(String stepId, Dataset cdo, List<FlowField> outputFields,
                                   ConfigObject uiConfigurations) throws Exception {
        Flows.StepBuilder sourceStep = null;
        sourceStep = Flows.step("source", stepId, stepId);

        Object ignoreMissingPath = cdo.getStorageConfigurations().get("ignoreMissingPath") != null
            ? cdo.getStorageConfigurations().get("ignoreMissingPath")
            : false;
        Map<String, Object> datasetInfo = new HashMap<>();
        List<Map<String, Object>> datasetList = new ArrayList<>();
        datasetInfo.put("rule", "set_1");
        datasetInfo.put("dataset", cdo.getName());
        datasetInfo.put("datasetId", cdo.getId());
        datasetInfo.put("ignoreMissingPath", ignoreMissingPath);
        datasetInfo.put("storage", cdo.getStorage());
        datasetList.add(datasetInfo);

        StepDesc step = sourceStep.config("schema", cdo.getSchema().getName())
            .config("schemaId", cdo.getSchema().getId()).config("dataset", datasetList).config("sessionCache", "")
            .config("ignoreMissingPath", ignoreMissingPath).config("interceptor", "").withPosition(100, 200)
            .output(outputFields).uiConfigurations(uiConfigurations).build();
        step.setInputConfigurations(null);// steps??????:(?????????input ???????????????output

        return step;
    }

    private String getDateStr(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    private StepDesc getFilterStep(String stepId, List<FlowField> inputFields, List<FlowField> outputFields,
                                   String condition, ConfigObject uiConfigurations) throws Exception {
        Flows.StepBuilder filterStep = Flows.step("filter", stepId, stepId);
        StepDesc step = filterStep.config("condition", condition).config("sessionCache", "").config("interceptor", "")
            .withPosition(300, 200).input(inputFields).output(outputFields).uiConfigurations(uiConfigurations)
            .build();
        return step;
    }

    private StepDesc getTransformStep(String stepId, DataService dataService, List<FlowField> inputFields,
                                      List<FlowField> outputFields, ConfigObject uiConfigurations) throws Exception {
        Flows.StepBuilder transStep = Flows.step("transform", stepId, stepId);

        List<Map<String, String>> expressions = new ArrayList<>();
        List<FieldMapping> fieldConfiguration = dataService.getFieldMappings();
        Map<String, FieldMapping> fieldMap = new HashMap<>();
        for (FieldMapping mapping : fieldConfiguration) {
            fieldMap.put(mapping.getSourceField(), mapping);
        }

        Map<String, String> transformFields = new HashMap<>();
        for (FlowField field : inputFields) {
            if (fieldMap.containsKey(field.getColumn())) {
                FieldMapping fieldMapping1 = fieldMap.get(field.getColumn());
                if (StringUtils.isNotEmpty(fieldMapping1.getEncrypt()) && fieldMapping1.getTransformRule() != null
                    && StringUtils.isNotEmpty(fieldMapping1.getTransformRule().getExpression())) {
                    TransformRule rule = fieldMapping1.getTransformRule();
                    Map<String, String> exp = new HashMap<>();
                    exp.put("value", rule.getExpression());
                    expressions.add(exp);
                    int index = rule.getExpression().indexOf(" as ");
                    if (index == -1) {
                        index = rule.getExpression().indexOf(" AS ");
                    }
                    String newField = rule.getExpression().substring(index + 4).trim();
                    transformFields.put(field.getColumn(), newField);
                }
            }
        }
        if (expressions.size() > 0) {
            transStep.config("expressions", expressions);
        } else {
            return null;
        }

        for (FlowField field : inputFields) {
            if (transformFields.containsKey(field.getColumn())) {
                FlowField output = new FlowField(transformFields.get(field.getColumn()), "string", "",
                    field.getDescription());
                outputFields.add(output);
            } else {
                outputFields.add(field);
            }
        }

        StepDesc step = transStep.config("sessionCache", "").config("interceptor", "").withPosition(500, 200)
            .input(inputFields).output(outputFields).uiConfigurations(uiConfigurations).build();

        return step;
    }

    private String getDatasetType(String datasourceType) {
        DataSourceType dataSourceType = null;
        try {
            dataSourceType = DataSourceType.valueOf(datasourceType.toUpperCase());
        } catch (Exception e) {
            logger.error("DataSourceType can't convert to dataset type: " + datasourceType);
            return "JDBC";
        }
        if (dataSourceType == DataSourceType.DB) {
            return "JDBC";
        } else if (dataSourceType == DataSourceType.FTP) {
            return "FTP";
        } else if (dataSourceType == DataSourceType.SFTP) {
            return "SFTP";
        } else if (dataSourceType == DataSourceType.ES) {
            return "ElasticSearch";
        } else if (dataSourceType == DataSourceType.KAFKA) {
            return "KAFKA";
        } else if (dataSourceType == DataSourceType.HDFS) {
            return "HDFS";
        }
        return datasourceType.toUpperCase();
    }

    private StepDesc getSinkStep(String stepId, DataService dataService, List<FlowField> inputFields, String timeSuffix,
                                 ConfigObject uiConfigurations) throws Exception {
        CustDataSource custDataSource = custDataSourceService.findOne(dataService.getTenantId(),
            dataService.getApplyConfiguration().getCustDataSourceId());
        if (!DataSourceType.contains(custDataSource.getType())) {
            logger.error("DataSource type not support : " + custDataSource.getType());
            throw new RuntimeException("DataSource type not support : " + custDataSource.getType());
        }

        String storage = getDatasetType(custDataSource.getType());
        Flows.StepBuilder stepBuilder = Flows.step("sink", stepId, stepId);
        Iterator<Map.Entry<String, Object>> iter = custDataSource.getAttributes().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> entry = iter.next();
            String key = entry.getKey();
            Object val = entry.getValue();
            stepBuilder.config(key, val);
        }
        if (!custDataSource.getAttributes().containsKey("mode")) {
            stepBuilder.config("mode", "append");
        }
        if (dataService.getApplyConfiguration() != null
            && StringUtils.isNotEmpty(dataService.getApplyConfiguration().getCustTableName())) {
            stepBuilder.config("table", dataService.getApplyConfiguration().getCustTableName());
        }
        // add type propreties
        stepBuilder.config("type", storage);

        List<FieldMapping> fieldMappings = dataService.getFieldMappings();
        Map<String, FieldMapping> fieldMap = new HashMap<>();
        for (FieldMapping mapping : fieldMappings) {
            if (StringUtils.isNotEmpty(mapping.getSourceField()) && StringUtils.isNotEmpty(mapping.getTargetField())) {
                fieldMap.put(mapping.getSourceField(), mapping);
            }
        }
        List<FlowField> targetFields = new ArrayList<>();
        for (FlowField flowField : inputFields) {
            if (fieldMap.containsKey(flowField.getColumn())) {
                FlowField field = new FlowField(flowField.getColumn(),
                    fieldMap.get(flowField.getColumn()).getTargetType(),
                    fieldMap.get(flowField.getColumn()).getTargetField(), flowField.getDescription());
                targetFields.add(field);
            }
        }
        if (targetFields.size() == 0) {
            targetFields.addAll(inputFields);
        }

        String schemaName = ExecutorFlowConf.schema_sink_prefix + dataService.getId() + "_" + timeSuffix;
        String datasetName = ExecutorFlowConf.dataset_sink_prefix + dataService.getId() + "_" + timeSuffix;
        Schema schema = schemaService.findOneByName(dataService.getTenantId(), schemaName);
        Dataset dataset = datasetService.findOneByName(dataService.getTenantId(), datasetName);
        String schemaId = "";
        String datasetId = "";
        if (schema == null) {
            // ??????schema, ????????????????????????????????????
            schema = createSchema(dataService, targetFields, schemaName);
        }
        schemaId = schema.getId();

        if (dataset == null) {
            dataset = createDataset(schema, dataService, custDataSource, datasetName, storage);
        }
        datasetId = dataset.getId();

        if (StringUtils.isNotEmpty(schemaId) && StringUtils.isNotEmpty(datasetId)) {
            stepBuilder.config("autoSchema", "false");
        } else {
            stepBuilder.config("autoSchema", "true");
        }

        stepBuilder.config("schemaId", schemaId).config("schema", schemaName).config("datasetId", datasetId)
            .config("dataset", datasetName);
        StepDesc step = stepBuilder.withPosition(800, 100).input(targetFields).uiConfigurations(uiConfigurations)
            .build();
        return step;
    }

    public Schema createSchema(DataService dataService, List<FlowField> inputFields, String schemaName) {
        Schema schema = new Schema(schemaName, new ArrayList<FieldDesc>());
        schema.setId(Snowflake.stringId());
        schema.setCreateTime(new Date());
        schema.setLastModifiedTime(schema.getCreateTime());
        ResourceDesc schema_resource = resourceDescService.findRootsByName(dataService.getTenantId(),
            ConstantInfo.RESOURCE_DIR_ROOT_SCHEMA);// ????????????schemas????????????
        schema.setResource(schema_resource);
        for (FlowField flowField : inputFields) {
            FieldDesc dataField = new FieldDesc(flowField.getColumn(), flowField.getType(), flowField.getAlias(),
                flowField.getDescription());
            String fieldName = dataField.getName();
            if (StringUtils.isNotEmpty(dataField.getAlias())) {
                dataField.setName(dataField.getAlias());
                dataField.setAlias(fieldName);
            }
            schema.getFields().add(dataField);
        }
        schema.setTenantId(dataService.getTenantId());
        schema.setOwner(dataService.getOwner());
        schema.setCreator(dataService.getCreator());
        schema.setLastModifier(dataService.getCreator());
        schema.setIsHide(1);
        try {
            schema = schemaService.save(schema);
        } catch (Exception e) {
            logger.error("save schema error: " + e.getMessage());
        }
        return schema;
    }

    public Dataset createDataset(Schema schema, DataService dataService, CustDataSource custDataSource,
                                 String datasetName, String storage) {
        Dataset dataset = new Dataset();
        dataset.setId(UUID.randomUUID().toString());
        dataset.setName(datasetName);
        dataset.setSchema(schema);
        dataset.setSchemaId(schema.getId());
        dataset.setSchemaVersion(schema.getVersion());

        ResourceDesc resourceDesc = resourceDescService.findRootsByName(dataService.getTenantId(),
            ConstantInfo.RESOURCE_DIR_ROOT_DATASET);
        dataset.setResource(resourceDesc);
        dataset.setResourceId(resourceDesc.getId());

        Map<String, Object> storageConfigurations = Maps.newHashMap(custDataSource.getAttributes());
        // ??????schema??????,?????????jdbc???????????????schema??????
        if (storageConfigurations.get("schema") != null
            && storageConfigurations.get("schema").toString().equals(schema.getName())) {
            storageConfigurations.remove("schema");
        }
        if (storageConfigurations.containsKey("schemaId")) {
            storageConfigurations.remove("schemaId");
        }
        if (storageConfigurations.containsKey("dataset")) {
            storageConfigurations.remove("dataset");
        }
        if (storageConfigurations.containsKey("datasetId")) {
            storageConfigurations.remove("datasetId");
        }
        if ("JDBC".equals(storage)) {
            storageConfigurations.put("table", dataService.getApplyConfiguration().getCustTableName());
        }

        dataset.setStorageConfigurations(storageConfigurations);
        dataset.setSliceTime("");
        dataset.setSliceType("H");
        dataset.setStorage(storage);
        dataset.setTenantId(dataService.getTenantId());
        dataset.setOwner(dataService.getOwner());
        dataset.setCreateTime(new Date());
        dataset.setLastModifiedTime(dataset.getCreateTime());
        dataset.setCreator(dataService.getCreator());
        dataset.setLastModifier(dataService.getCreator());
        dataset.setIsHide(1);
        dataset.setVersion(1);
        dataset.setExpiredPeriod(0L);
        try {
            dataset = datasetService.save(dataset);
        } catch (Exception e) {
        }

        return dataset;
    }

    private StepDesc getSQLStep(String stepId, DataResource dataResource, List<FlowField> inputFields,
                                List<FlowField> outputFields, ConfigObject uiConfigurations) throws Exception {
        StepDesc step = null;
        for (FlowField field : inputFields) {
            if (field.getColumn().equals(dataResource.getIncrementField())) {
                Flows.StepBuilder stepBuilder = Flows.step("sql", stepId, stepId);
                stepBuilder.config("sessionCache", "").config("interceptor", "").config("sql",
                    "select max(" + dataResource.getIncrementField() + ") as cursorVal from input");
                outputFields.add(new FlowField("cursorVal", "string", "", ""));
                step = stepBuilder.withPosition(700, 300).input(inputFields).output(outputFields)
                    .uiConfigurations(uiConfigurations).build();
                return step;
            }
        }
        log.info("not found incrementField from output fields: {} ", dataResource.getIncrementField());

        return null;
    }

    private StepDesc getSqlSinkStep(String stepId, DataService dataService, List<FlowField> inputFields,
                                    String timeSuffix, ConfigObject uiConfigurations) {
        Flows.StepBuilder stepBuilder = Flows.step("sink", stepId, stepId);
        String schemaName = ExecutorFlowConf.schema_cursor_name;
        String datasetName = ExecutorFlowConf.dataset_cursor_prefix + dataService.getId() + "_" + timeSuffix;
        Schema schema = schemaService.findOneByName(dataService.getTenantId(), schemaName);
        Dataset dataset = datasetService.findOneByName(dataService.getTenantId(), datasetName);
        // ???????????????
        String schemaId = "";
        String datasetId = "";
        if (schema != null) {
            schemaId = schema.getId();
        } else {
            // ??????schema, ????????????????????????????????????
            schema = createSchema(dataService, inputFields, schemaName);
            schemaId = schema.getId();
        }
        Map<String, Object> storageConfiguration = null;

        if (dataset != null) {
            datasetId = dataset.getId();
            storageConfiguration = dataset.getStorageConfigurations();
        } else {
            storageConfiguration = initSqlSinkConfiguration(dataset, schema, dataService);
            dataset = new Dataset(datasetName, schema, "HDFS", storageConfiguration);
            dataset.setId(UUID.randomUUID().toString());
            dataset.setSchemaId(schema.getId());
            dataset.setSchemaVersion(schema.getVersion());
            ResourceDesc resourceDesc = resourceDescService.findRootsByName(dataService.getTenantId(),
                ConstantInfo.RESOURCE_DIR_ROOT_DATASET);
            dataset.setResource(resourceDesc);
            dataset.setResourceId(resourceDesc.getId());
            dataset.setStorageConfigurations(storageConfiguration);
            dataset.setSliceTime("");
            dataset.setSliceType("H");
            dataset.setTenantId(dataService.getTenantId());
            dataset.setOwner(dataService.getOwner());
            dataset.setCreateTime(new Date());
            dataset.setLastModifiedTime(dataset.getCreateTime());
            dataset.setCreator(dataService.getCreator());
            dataset.setLastModifier(dataService.getCreator());
            dataset.setVersion(1);
            dataset.setExpiredPeriod(0L);
            dataset.setIsHide(1);
            try {
                dataset = datasetService.save(dataset);
            } catch (Exception e) {
            }

            datasetId = dataset.getId();
        }

        stepBuilder.config("mode", "overwrite");
        stepBuilder.config("schemaId", schemaId).config("schema", schemaName).config("datasetId", datasetId)
            .config("dataset", datasetName);
        Iterator<Map.Entry<String, Object>> iterator = storageConfiguration.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object val = entry.getValue();
            stepBuilder.config(key, val);
        }
        stepBuilder.config("schemaVersion", schema.getVersion()).config("sliceTimeColumn", "").config("description", "")
            .config("maxFileSize", "").config("type", "HDFS").config("maxFileNumber", "").config("nullValue", "")
            .config("expiredTime", "0").config("countWrittenRecord", "").config("sliceType", "");

        StepDesc step = stepBuilder.withPosition(900, 300).input(inputFields).uiConfigurations(uiConfigurations)
            .build();
        return step;
    }

    private Map<String, Object> initSqlSinkConfiguration(Dataset dataset, Schema schema, DataService dataService) {
        Map<String, Object> storageConfiguration = new HashMap<String, Object>();
        storageConfiguration.put("path", ExecutorFlowConf.dataset_cursor_tmp_dir + "/" + dataService.getId() + "/"
            + ExecutorFlowConf.dataset_cursor_file_dir);
        storageConfiguration.put("relativePath", ExecutorFlowConf.dataset_cursor_tmp_dir + "/" + dataService.getId()
            + "/" + ExecutorFlowConf.dataset_cursor_file_dir);
        storageConfiguration.put("quoteChar", "\"");
        storageConfiguration.put("escapeChar", "\\");
        storageConfiguration.put("format", "csv");
        storageConfiguration.put("separator", ",");
        storageConfiguration.put("pathMode", "exact");
        storageConfiguration.put("header", "false");

        return storageConfiguration;
    }

    public FlowDesc generateDataServiceFlow(DataService dataService, DataResource dataResource,
                                            CustDataSource custDataSource) throws Exception {
        // final String schema_current = ConstantInfo.QA_ANALYSIS_SCHEMA_CURRENT;
        // final String schema_dir = ConstantInfo.DS_SCHEMA_DIR;
        // final String flowType = "dataflow";
        String timestamp = getDateStr("yyyyMMddHHmmss");

        String flowName = "dataservice_" + dataService.getId() + "_" + timestamp;
        Dataset sourceDataset = null;
        try {
            sourceDataset = datasetService.selectByPrimaryKey(dataResource.getDatasetId());
        } catch (Exception e) {
            logger.error("", e);
        }

        if (sourceDataset == null) {
            logger.error("getDataset by id error: id=" + dataResource.getDatasetId());
            throw new RuntimeException("getDataset by id error: id= " + dataResource.getDatasetId());
        }

        // ??????dataset?????????????????????????????????????????????????????????????????????????????????
        if (StringUtils.isNotEmpty(sourceDataset.getSchemaId())) {
            sourceDataset.setSchema(schemaService.selectByPrimaryKey(sourceDataset.getSchemaId()));
        }

        // Source step input
        List<FlowField> sourceInputs = getSchemaFiled(sourceDataset.getSchemaId());
        // source step output
        List<FlowField> sourceOutputs = getSourceOutputFields(sourceInputs, dataResource.getFieldMappings());
        // filter step input
        List<FlowField> filterInputs = getInputFields(sourceOutputs);
        // filter step output
        List<FlowField> filterOutputs = filterInputs;
        // transform step input
        List<FlowField> transInputFields = filterOutputs;

        // ??????source step,??????datasetId,schemaId
        StepDesc sourceStep = getSourceStep("source_1", sourceDataset, sourceOutputs,
            new ConfigObject().withConfig("output", new String[]{"output"}));

        // ??????filter step
        StepDesc filterStep = null;
        String condition = getCondition(dataResource, dataService, filterInputs);
        filterStep = getFilterStep("filter_2", filterInputs, filterOutputs, condition, new ConfigObject()
            .withConfig("input", new String[]{"input"}).withConfig("output", new String[]{"output"}));

        // ??????transform step
        StepDesc transStep = null;
        List<FlowField> outputFields = new ArrayList<>();
        if (dataService.getFieldMappings() != null && dataService.getFieldMappings().size() > 0) {
            transStep = getTransformStep("transform_3", dataService, transInputFields, outputFields, new ConfigObject()
                .withConfig("input", new String[]{"input"}).withConfig("output", new String[]{"output"}));
            if (transStep == null) {
                outputFields = filterOutputs;
            }
        } else {
            outputFields = filterOutputs;
        }

        // ??????sink step
        StepDesc sinkStep = getSinkStep("sink_4", dataService, outputFields, timestamp,
            new ConfigObject().withConfig("input", new String[]{"input"}));

        // ??????sql step???sink step 2
        StepDesc sqlStep = null;
        StepDesc sinkStep_2 = null;
        if (dataService.getApplyConfiguration().getServiceMode() == DataServiceMode.increment_mode
            && getIncrementFieldType(dataResource.getIncrementField(), outputFields) != null) {
            List<FlowField> sqlOutFields = new ArrayList<>();

            sqlStep = getSQLStep("sql_5", dataResource, outputFields, sqlOutFields, new ConfigObject()
                .withConfig("input", new String[]{"input"}).withConfig("output", new String[]{"output"}));
            if (sqlStep != null) {
                sinkStep_2 = getSqlSinkStep("sink_6", dataService, sqlOutFields, timestamp,
                    new ConfigObject().withConfig("input", new String[]{"input"}));
            }
        }

        Flows.FlowBuilder flowBuilder = Flows.dataflow(flowName);
        FlowDesc flow = null;
        try {
            if (sqlStep != null) {
                if (transStep != null) {
                    flowBuilder.step(sourceStep).step(filterStep).step(transStep).step(sinkStep).step(sqlStep)
                        .step(sinkStep_2).connect("source_1", "filter_2").connect("filter_2", "transform_3")
                        .connect("transform_3", "sink_4").connect("transform_3", "sql_5")
                        .connect("sql_5", "sink_6");
                } else {
                    flowBuilder.step(sourceStep).step(filterStep).step(sinkStep).step(sqlStep).step(sinkStep_2)
                        .connect("source_1", "filter_2").connect("filter_2", "sink_4").connect("filter_2", "sql_5")
                        .connect("sql_5", "sink_6");
                }
            } else {
                if (transStep != null) {
                    flowBuilder.step(sourceStep).step(filterStep).step(transStep).step(sinkStep)
                        .connect("source_1", "filter_2").connect("filter_2", "transform_3")
                        .connect("transform_3", "sink_4");
                } else {
                    flowBuilder.step(sourceStep).step(filterStep).step(sinkStep).connect("source_1", "filter_2")
                        .connect("filter_2", "sink_4");
                }
            }

            flow = flowBuilder.build();
        } catch (Exception e) {
            logger.error("build flow exception: ", e);
        }
        flow.setSource("dsflow");// ??????flow??????????????????execution?????????????????????
        // ??????????????????????????????
        if (sourceDataset.getStorage().equals("JDBC")) {
            if (sourceDataset.getStorageConfigurations() != null
                && StringUtils.isNotEmpty((String) sourceDataset.getStorageConfigurations().get("jarPath"))) {
                ParameterDesc param = new ParameterDesc();
                param.setName((String) sourceDataset.getStorageConfigurations().get("jarPath"));
                param.setCategory("ref");
                if (flow.getDependencies() != null) {
                    flow.getDependencies().add(param);
                } else {
                    List<ParameterDesc> depenList = new ArrayList<>();
                    depenList.add(param);
                    flow.setDependencies(depenList);
                }
            } else {
                logger.error("not found jarPath for Jdbc dataset: " + sourceDataset.getName());
                throw new RuntimeException("not found jarPath for Jdbc dataset: " + sourceDataset.getName());
            }

        }
        // ?????????sink jdbc??????????????????
        if (custDataSource.getType().equalsIgnoreCase("DB")) {
            if (StringUtils.isNotEmpty(custDataSource.getAttributes().getOrDefault("jarPath", "").toString())) {
                ParameterDesc param = new ParameterDesc();
                param.setName(custDataSource.getAttributes().getOrDefault("jarPath", "").toString());
                param.setCategory("ref");
                if (flow.getDependencies() != null) {
                    flow.getDependencies().add(param);
                } else {
                    List<ParameterDesc> depenList = new ArrayList<>();
                    depenList.add(param);
                    flow.setDependencies(depenList);
                }
            } else {
                logger.error("not found jarPath for custDataSource : " + custDataSource.getName());
                throw new RuntimeException("not found jarPath for custDataSource : " + custDataSource.getName());
            }
        }

        // dataservice flow?????????Flows/ds_flow?????????
        ResourceDesc flowResource = null;
        flowResource = resourceDescService.findRootsByName(dataService.getTenantId(),
            ConstantInfo.RESOURCE_DIR_ROOT_FLOW);
        ResourceDesc dataserviceFlowRes = resourceDescService.findByNameAndParent(dataService.getTenantId(),
            ConstantInfo.DS_OUTPUT_FLOW_DIR, flowResource.getId());

        if (dataserviceFlowRes == null) {
            ResourceDesc dsResDesc = new ResourceDesc();
            dsResDesc.setCreateTime(new Date());
            dsResDesc.setCreator(dataService.getCreator());
            dsResDesc.setEnabled(1);
            dsResDesc.setExpiredTime(0L);
            dsResDesc.setLastModifiedTime(dsResDesc.getCreateTime());
            dsResDesc.setLastModifier(dataService.getCreator());
            dsResDesc.setName(ConstantInfo.DS_OUTPUT_FLOW_DIR);
            dsResDesc.setTenantId(dataService.getTenantId());
            dsResDesc.setOwner(dataService.getOwner());
            dsResDesc.setVersion(0);
            dsResDesc.setResType(ConstantInfo.RES_TYPE_FLOW);
            dsResDesc.setParentId(flowResource.getId());
            dsResDesc.setIsHide(1);// Ds_flow?????????????????????Flows?????????

            resourceDescService.saveOrUpdate(dsResDesc);
            dataserviceFlowRes = dsResDesc;
        }

        flow.setResource(dataserviceFlowRes);
        flow.setResourceId(dataserviceFlowRes.getId());
        flow.setTenantId(dataService.getTenantId());
        flow.setOwner(dataService.getOwner());
        flow.setIsHide(1);// qa flow????????????Flows?????????
        flow.setVersion(1);

        FlowDesc flowCreated = flowDescService.saveOrUpdate(flow);

        // flow?????????????????????????????????history??????
        // logger.info("copy history flow begin ...");
        FlowHistDesc fh = copyToHistory(flowCreated);

        return flowCreated;
    }

    public String getCondition(DataResource dataResource, DataService dataService, List<FlowField> filterInputs) {
        String condition = "1 == 1";

        if (dataService.getApplyConfiguration().getServiceMode() == DataServiceMode.increment_mode) {
            String filterType = getIncrementFieldType(dataResource.getIncrementField(), filterInputs);
            if (filterType != null) {// ???type?????????????????????
                if (StringUtils.isNotEmpty(dataService.getCursorVal())) {
                    String filterCol = dataResource.getIncrementField();
                    if (filterType.equals("int") || filterType.equals("short") || filterType.equals("bigint")
                        || filterType.equals("float") || filterType.equals("double")
                        || filterType.startsWith("decimal")) {
                        condition = filterCol + " > " + dataService.getCursorVal();
                    } else if (filterType.equals("date")) {
                        condition = filterCol + " > to_timestamp('" + dataService.getCursorVal() + "')";// ??????yyyy-mm-dd???????????????,?????????
                    } else if (filterType.equals("timestamp")) {
                        condition = filterCol + " > to_timestamp('" + dataService.getCursorVal() + "')";// ??????yyyy-mm-dd
                        // HH:MM:SS???????????????
                    } else {
                        log.error("you have selected error increment field {} and type is {}", filterCol, filterType);
                    }
                } else {
                    String filterCol = dataResource.getIncrementField();
                    condition = filterCol + " IS NOT NULL";
                }
            }
        }
        return condition;
    }

    private String getIncrementFieldType(String fieldName, List<FlowField> inputFields) {
        if (StringUtils.isNotEmpty(fieldName)) {
            for (FlowField flowField : inputFields) {
                if (flowField.getColumn().equals(fieldName)) {
                    return flowField.getType();
                }
            }
        }

        return null;
    }

    private FlowHistDesc copyToHistory(FlowDesc flow) {
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
        fh.setTenantId(flow.getTenantId());
        fh.setOwner(flow.getOwner());
        fh = flowHistDescService.saveOrUpdate(fh);
        return fh;
    }

    public FlowSchedulerDesc generateScheduler(DataService dataService, FlowDesc flowDesc,
                                               List<ConfigItem> runtime_properties) {
        String schedulerName = "dataservice_" + dataService.getId() + "_" + getDateStr("yyyyMMddHHmmss");
        ConfigObject configurations = new ConfigObject();
        configurations.put("properties", runtime_properties);
        Long startTime = System.currentTimeMillis();
        try {
            startTime = Long.parseLong(dataService.getServiceConfiguration().get("startTime"));
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        configurations.put("startTime", startTime);
        if (flowDesc.getDependencies() != null && flowDesc.getDependencies().size() > 0) {
            Map<String, String> jars = new HashMap<>();
            for (ParameterDesc param : flowDesc.getDependencies()) {
                jars.put(param.getName(), "");
            }
            configurations.put("dependencies", jars.keySet().stream().collect(Collectors.joining(",")));
        }

        FlowSchedulerDesc scheduler = new FlowSchedulerDesc();
        scheduler.setId(UUID.randomUUID().toString());
        scheduler.setName(schedulerName);
        scheduler.setSource("dsflow");
        scheduler.setSchedulerId("once");
        scheduler.setFlowId(flowDesc.getId());
        scheduler.setFlowName(flowDesc.getName());
        scheduler.setConfigurations(configurations);
        scheduler.setFlowType("dataflow");
        scheduler.setTotalExecuted(0);
        scheduler.setTenantId(dataService.getTenantId());
        scheduler.setOwner(dataService.getOwner());

        return scheduler;
    }

}
