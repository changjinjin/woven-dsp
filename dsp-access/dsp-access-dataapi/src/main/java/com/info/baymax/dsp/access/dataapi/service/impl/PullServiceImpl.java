package com.info.baymax.dsp.access.dataapi.service.impl;

import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.dsp.access.dataapi.data.DataReader;
import com.info.baymax.dsp.access.dataapi.data.MapEntity;
import com.info.baymax.dsp.access.dataapi.data.RecordQuery;
import com.info.baymax.dsp.access.dataapi.data.StorageConf;
import com.info.baymax.dsp.access.dataapi.service.PullService;
import com.info.baymax.dsp.access.dataapi.web.request.AggRequest;
import com.info.baymax.dsp.access.dataapi.web.request.DataRequest;
import com.info.baymax.dsp.access.dataapi.web.request.PullRequest;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import com.info.baymax.dsp.data.dataset.bean.FieldMapping;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: guofeng.wu
 * @Date: 2020/1/8
 */
@Service
@Slf4j
public class PullServiceImpl implements PullService {

    @Autowired
    private DataServiceEntityService dataServiceEntityService;
    @Autowired
    private DataCustAppService custAppService;
    @Autowired
    private DataResourceService dataResourceService;
    @Autowired
    private DatasetService datasetService;
    @Autowired
    private DataReader<MapEntity, MapEntity> dataReader;

    @Override
    public IPage<MapEntity> pullRecords(PullRequest request, String hosts) {
        validate(request);
        return doQuery(request, hosts);
    }

    @Override
    public IPage<MapEntity> pullAggs(AggRequest request, String hosts) {
        validate(request);
        return doQuery(request, hosts);
    }

    public boolean validate(PullRequest request) {
        return true;
    }

    public boolean validate(AggRequest request) {
        return true;
    }

    private IPage<MapEntity> doQuery(DataRequest<?> request, String hosts) {
        Long dataServiceId = request.getDataServiceId();
        String requestKey = request.getAccessKey();
        String host = hosts.split(",")[0];

        DataService dataService = dataServiceEntityService.selectByPrimaryKey(Long.valueOf(dataServiceId));
        if (dataService == null) {
            throw new ControllerException(ErrType.ENTITY_NOT_EXIST, "数据服务 " + dataServiceId + " 不存在");
        }
        if (dataService.getType() == 1) {
            throw new ControllerException(ErrType.BAD_REQUEST, "不支持push共享方式");
        }
        if (dataService.getStatus() != 1) {
            throw new ControllerException(ErrType.BAD_REQUEST, "服务不可用，未部署或已停止或已过期");
        }

        Long custAppId = dataService.getApplyConfiguration().getCustAppId();
        DataCustApp custApp = custAppService.selectByPrimaryKey(custAppId);
        if (custApp == null) {
            throw new ControllerException(ErrType.ENTITY_NOT_EXIST, "接入配置" + custAppId + " 不存在");
        }
        String accessKey = custApp.getAccessKey();
        String[] accessIp = custApp.getAccessIp();
        if (!accessKey.equals(requestKey) || !Arrays.asList(accessIp).contains(host)) {
            throw new ControllerException(ErrType.BAD_REQUEST, "Wrong accessKey or accessIp");
        }

        Long dataResId = dataService.getApplyConfiguration().getDataResId();
        DataResource dataResource = dataResourceService.selectByPrimaryKey(dataResId);
        if (dataResource == null) {
            throw new ControllerException(ErrType.ENTITY_NOT_EXIST, "数据资源" + dataResId + " 不存在");
        }
        Dataset dataset = datasetService.selectByPrimaryKey(dataResource.getDatasetId());
        if (dataset == null) {
            throw new ControllerException(ErrType.ENTITY_NOT_EXIST, "数据集" + dataResource.getDatasetId() + " 不存在");
        }

        List<FieldMapping> dataServiceMappings = dataService.getFieldMappings();
        List<FieldMapping> dataResourceMappings = dataResource.getFieldMappings();
        List<String> includes = new ArrayList<>();
        Map<String, String> fieldMap = new HashMap<>();
        for (FieldMapping dataServiceMapping : dataServiceMappings) {
            for (FieldMapping dataResourceMapping : dataResourceMappings) {
                if (dataServiceMapping.getSourceField().equals(dataResourceMapping.getTargetField())
                    && !StringUtils.isEmpty(dataServiceMapping.getSourceField())) {
                    includes.add(dataResourceMapping.getSourceField());
                    fieldMap.put(dataResourceMapping.getSourceField(), dataServiceMapping.getTargetField());
                }
            }
        }

        try {
            Map<String, String> conf = dataset.getStorageConfigurations();
            conf.put("storage", dataset.getStorage());
            Object query = request.getQuery();
            IPage<MapEntity> page = null;
            if (query instanceof AggQuery) {
                page = dataReader.readAgg(StorageConf.from(conf), ((AggQuery) query));
            } else {
                page = dataReader.readRecord(StorageConf.from(conf), ((RecordQuery) query).allProperties(includes));
            }
            return page;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
        }
    }

}
