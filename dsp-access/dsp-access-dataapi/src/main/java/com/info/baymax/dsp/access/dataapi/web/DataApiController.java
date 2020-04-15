package com.info.baymax.dsp.access.dataapi.web;

import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.access.dataapi.request.PullRequest;
import com.info.baymax.dsp.access.dataapi.service.ElasticSearchService;
import com.info.baymax.dsp.access.dataapi.service.PullService;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import com.info.baymax.dsp.data.dataset.bean.FieldMapping;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/dataapi")
@Api(tags = "数据服务接口", value = "数据拉取接口")
public class DataApiController implements Serializable {

    private static final long serialVersionUID = -5006451148239176107L;

    @Autowired
    DataServiceEntityService dataServiceEntityService;

    @Autowired
    DataCustAppService custAppService;

    @Autowired
    DataResourceService dataResourceService;

    @Autowired
    DatasetService datasetService;

    @Autowired
    ElasticSearchService elasticSearchService;

    @Autowired
    PullService pullService;

    @ApiOperation(value = "数据拉取接口")
    @PostMapping("/pull")
    public Response<?> pullData(@RequestBody PullRequest body, @RequestHeader String[] hosts) throws Exception {
        String dataServiceId = body.getDataServiceId();
        String requestKey = body.getAccessKey();
        String host = hosts[0];
        int offset = body.getOffset();
        int size = body.getSize();

        if (dataServiceId == null) {
            return Response.error(ErrType.BAD_REQUEST, "请求缺少dataServiceId参数");
        }
        DataService dataService = dataServiceEntityService.selectByPrimaryKey(Long.valueOf(dataServiceId));
        if (dataService == null) {
            return Response.error(ErrType.ENTITY_NOT_EXIST, "数据服务 " + dataServiceId + " 不存在");
        }
        if (dataService.getType() == 1) {
            return Response.error(ErrType.BAD_REQUEST, "不支持push共享方式");
        }
        if (dataService.getStatus() != 1) {
            return Response.error(ErrType.BAD_REQUEST, "服务不可用，未部署或已停止或已过期");
        }
        Long custAppId = dataService.getApplyConfiguration().getCustAppId();
        DataCustApp custApp = custAppService.selectByPrimaryKey(custAppId);
        if (custApp == null) {
            return Response.error(ErrType.ENTITY_NOT_EXIST, "接入配置" + custAppId + " 不存在");
        }

        String accessKey = custApp.getAccessKey();
        String[] accessIp = custApp.getAccessIp();
        if (accessKey.equals(requestKey) && Arrays.asList(accessIp).contains(host)) {
            Long dataResId = dataService.getApplyConfiguration().getDataResId();
            DataResource dataResource = dataResourceService.selectByPrimaryKey(dataResId);
            if (dataResource == null) {
                return Response.error(ErrType.ENTITY_NOT_EXIST, "数据资源" + dataResId + " 不存在");
            }
            Dataset dataset = datasetService.selectByPrimaryKey(dataResource.getDatasetId());
            if (dataset == null) {
                return Response.error(ErrType.ENTITY_NOT_EXIST, "数据集" + dataResource.getDatasetId() + " 不存在");
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
            List<Map<String, Object>> res = pullService.query(dataset.getStorage(), fieldMap, dataset.getStorageConfigurations(),
                    offset, size, includes.toArray(new String[0]));
            return Response.ok(res);

        } else {
            return Response.error(ErrType.BAD_REQUEST, "Wrong accessKey or accessIp");
        }
    }


}
