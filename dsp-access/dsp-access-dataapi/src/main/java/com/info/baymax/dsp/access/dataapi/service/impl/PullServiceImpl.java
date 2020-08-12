package com.info.baymax.dsp.access.dataapi.service.impl;

import com.google.common.collect.Maps;
import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.mybatis.mapper.example.Example.CriteriaItem;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.service.criteria.agg.AggField;
import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.common.service.criteria.field.Field;
import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.common.service.criteria.field.Sort;
import com.info.baymax.common.service.criteria.query.RecordQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.access.dataapi.data.DataReader;
import com.info.baymax.dsp.access.dataapi.data.MapEntity;
import com.info.baymax.dsp.access.dataapi.data.StorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcQuery;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.AggQuerySql;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.RecordQuerySql;
import com.info.baymax.dsp.access.dataapi.service.PullService;
import com.info.baymax.dsp.access.dataapi.web.request.AggRequest;
import com.info.baymax.dsp.access.dataapi.web.request.DataRequest;
import com.info.baymax.dsp.access.dataapi.web.request.RecordRequest;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import com.info.baymax.dsp.data.dataset.bean.FieldMapping;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceService;
import com.inforefiner.repackaged.com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: guofeng.wu
 * @Date: 2020/1/8
 */
@Service
@Slf4j
public class PullServiceImpl implements PullService {

    @Autowired
    private DataServiceService dataServiceEntityService;
    @Autowired
    private DataCustAppService custAppService;
    @Autowired
    private DataResourceService dataResourceService;
    @Autowired
    private DatasetService datasetService;
    @Autowired
    private DataReader<MapEntity, MapEntity> dataReader;

    @Override
    public IPage<MapEntity> pullRecords(RecordRequest request, String hosts) {
        try {
            QueryConf queryConf = doQuery(request, hosts);
            RecordQuery query = request.getQuery();
            validate(queryConf.getFieldMap(), query);
            return dataReader.readRecord(StorageConf.from(queryConf.getConf()), query);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public IPage<MapEntity> pullAggs(AggRequest request, String hosts) {
        try {
            QueryConf queryConf = doQuery(request, hosts);
            AggQuery query = request.getQuery();
            validate(queryConf.getFieldMap(), query);
            return dataReader.readAgg(StorageConf.from(queryConf.getConf()), query);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public String pullRecordsSql(RecordRequest request, String hosts) {
        try {
            QueryConf queryConf = doQuery(request, hosts);
            RecordQuery query = request.getQuery();
            validate(queryConf.getFieldMap(), query);
            return RecordQuerySql.builder(JdbcQuery.from(query).table(queryConf.getDataServiceName())).getExecuteSql();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public String pullAggsSql(AggRequest request, String hosts) {
        try {
            QueryConf queryConf = doQuery(request, hosts);
            AggQuery query = request.getQuery();
            validate(queryConf.getFieldMap(), query);
            return AggQuerySql.builder(query.table(queryConf.getDataServiceName())).getExecuteSql();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
        }
    }

    public boolean validate(Map<String, FieldMapping> fieldMap, RecordQuery query) {
        Set<String> keySet = fieldMap.keySet();

        validateFieldGroup(keySet, query.getFieldGroup());
        validateSorts(keySet, query.getOrdSort());

        // 校验query参数
        LinkedHashSet<String> selectProperties = query.getSelectProperties();
        if (ICollections.hasElements(selectProperties)) {
            for (String fieldName : selectProperties) {
                if (!keySet.contains(fieldName)) {
                    throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR,
                        "Wrong query attribute 'selectProperties'. The target data service does not have a field called '"
                            + fieldName + "'. The allowed field list is: " + keySet.toString());
                }
            }
        }
        LinkedHashSet<String> excludeProperties = query.getExcludeProperties();
        if (ICollections.hasElements(excludeProperties)) {
            for (String fieldName : excludeProperties) {
                if (!keySet.contains(fieldName)) {
                    throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR,
                        "Wrong query attribute 'excludeProperties'. The target data service does not have a field called '"
                            + fieldName + "'. The allowed field list is: " + keySet.toString());
                }
            }
        }
        return true;
    }

    private void validateFieldGroup(Set<String> keySet, FieldGroup fieldGroup) {
        if (fieldGroup == null) {
            return;
        }
        List<CriteriaItem> ordItems = fieldGroup.ordItems();
        if (ICollections.hasElements(ordItems)) {
            for (CriteriaItem item : ordItems) {
                if (item instanceof FieldGroup) {
                    validateFieldGroup(keySet, (FieldGroup) item);
                } else {
                    String fieldName = ((Field) item).getName();
                    if (!keySet.contains(fieldName)) {
                        throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR,
                            "Wrong query attribute 'fieldGroup' or 'havingFieldGroup'. The data service fields or result fields does not have a field called '"
                                + fieldName + "'. The allowed field list may be is: " + keySet.toString());
                    }
                }
            }
        }
    }

    private void validateSorts(Set<String> keySet, LinkedHashSet<Sort> sorts) {
        // 排序字段可以是别名，这里没有给别名的选项，所以这里用原字段判断即可
        if (ICollections.hasElements(sorts)) {
            for (Sort sort : sorts) {
                if (!keySet.contains(sort.getName())) {
                    throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR,
                        "Wrong query attribute 'sorts' or 'havingSorts'. The data service fields or result fields does not have a field called '"
                            + sort.getName() + "'. The allowed field list may be is: " + keySet.toString());
                }
            }
        }
    }

    public boolean validate(Map<String, FieldMapping> fieldMap, AggQuery query) {
        Set<String> keySet = fieldMap.keySet();
        validateFieldGroup(keySet, query.getFieldGroup());

        LinkedHashSet<AggField> aggFields = query.getAggFields();
        if (ICollections.hasElements(aggFields)) {
            for (AggField field : aggFields) {
                String fieldName = field.getName();
                if (!keySet.contains(fieldName)) {
                    throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR,
                        "Wrong query attribute 'aggFields'. The target data service does not have a field called '"
                            + fieldName + "'. The allowed field list is: " + keySet.toString());
                } else {
                    Set<String> supportAggs = fieldMap.get(fieldName).getSupportAggs();
                    if (ICollections.hasElements(supportAggs) && !supportAggs.contains(field.getAggType().getValue())) {
                        throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR,
                            "Wrong query attribute 'aggFields'. Not supported function '"
                                + field.getAggType().getValue() + "' for field '" + fieldName
                                + "'. The supported aggregate functions are: " + supportAggs.toString());
                    }
                }
            }
        }

        // validate groupFields
        LinkedHashSet<String> groupFields = query.getGroupFields();
        if (ICollections.hasElements(groupFields)) {
            for (String fieldName : groupFields) {
                if (!keySet.contains(fieldName)) {
                    throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR,
                        "Wrong query attribute 'groupFields'. The target data service does not have a field called '"
                            + fieldName + "'. The allowed field list is: " + keySet.toString());
                }
            }
        }

        // validate havingFieldGroup
        Set<String> havingFieldsSet = Sets.newLinkedHashSet();
        havingFieldsSet.addAll(groupFields);
        havingFieldsSet.addAll(aggFields.stream().map(t -> t.getAlias()).collect(Collectors.toSet()));
        FieldGroup havingFieldGroup = query.getHavingFieldGroup();
        if (havingFieldGroup != null) {
            validateFieldGroup(havingFieldsSet, havingFieldGroup);
        }

        // validate havingSorts
        LinkedHashSet<Sort> havingSorts = query.getOrdSort();
        if (ICollections.hasElements(havingSorts)) {
            validateSorts(havingFieldsSet, havingSorts);
        }
        return true;
    }

    private QueryConf doQuery(DataRequest<?> request, String hosts) {
        Long dataServiceId = request.getDataServiceId();
        String requestKey = request.getAccessKey();
        String host = hosts.split(",")[0];

        DataService dataService = dataServiceEntityService.selectByPrimaryKey(Long.valueOf(dataServiceId));
        if (dataService == null) {
            throw new ControllerException(ErrType.ENTITY_NOT_EXIST,
                String.format("The data service with ID %s does not exist.", dataServiceId));
        }
        if (dataService.getType() == 1) {
            throw new ControllerException(ErrType.BAD_REQUEST, "The current data service does not support pull mode.");
        }
        if (dataService.getStatus() != 1) {
            throw new ControllerException(ErrType.BAD_REQUEST,
                "The current data service is not available, not deployed, stopped, or expired.");
        }

        Long custAppId = dataService.getApplyConfiguration().getCustAppId();
        DataCustApp custApp = custAppService.selectByPrimaryKey(custAppId);
        if (custApp == null) {
            throw new ControllerException(ErrType.ENTITY_NOT_EXIST,
                String.format("The access configuration with ID %s does not exist", custAppId));
        }
        String accessKey = custApp.getAccessKey();
        String[] accessIp = custApp.getAccessIp();
        if (!accessKey.equals(requestKey) || !Arrays.asList(accessIp).contains(host)) {
            throw new ControllerException(ErrType.BAD_REQUEST, "Wrong accessKey or accessIp");
        }

        Long dataResId = dataService.getApplyConfiguration().getDataResId();
        DataResource dataResource = dataResourceService.selectByPrimaryKey(dataResId);
        if (dataResource == null) {
            throw new ControllerException(ErrType.ENTITY_NOT_EXIST,
                String.format("The data resource with ID %s does not exist", dataResId));
        }
        Dataset dataset = datasetService.selectByPrimaryKey(dataResource.getDatasetId());
        if (dataset == null) {
            throw new ControllerException(ErrType.ENTITY_NOT_EXIST,
                String.format("The dataset with ID %s does not exist", dataResource.getDatasetId()));
        }

        try {
            // 将fieldMappings装进map中以便于查询
            List<FieldMapping> fieldMappings = dataService.getFieldMappings();
            final Map<String, FieldMapping> fieldMap = Maps.newHashMap();
            for (FieldMapping fieldMapping : fieldMappings) {
                if (StringUtils.isNotEmpty(fieldMapping.getTargetField())) {
                    fieldMap.put(fieldMapping.getSourceField(), fieldMapping);
                }
            }

            // 处理存储信息
            Map<String, Object> conf = dataset.getStorageConfigurations();
            conf.put("storage", dataset.getStorage());
            return new QueryConf(dataService.getName(), conf, fieldMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
        }
    }

    @AllArgsConstructor
    @Getter
    private static final class QueryConf {
        private String dataServiceName;
        private Map<String, Object> conf;
        private Map<String, FieldMapping> fieldMap;
    }
}
