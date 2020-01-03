package com.info.baymax.dsp.access.dataapi.web;

import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.access.dataapi.service.ElasticSearchService;
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
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
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

    @ApiOperation(value = "数据拉取接口")
    @PostMapping("/pull")
    public Response pullData(@RequestBody Map<String, String> body, @RequestHeader Map<String, String> header) throws Exception {
        String dataServiceId = body.get("dataServiceId");
        String requestKey = body.get("accessKey");
        String host = header.get("host");
        int offset = Integer.valueOf(body.getOrDefault("offset", "0"));
        int size = Integer.valueOf(body.getOrDefault("size", "10000"));

        if (dataServiceId == null) {
            return Response.error(ErrType.BAD_REQUEST, "Missing dataServiceId param");
        }
        DataService dataService = dataServiceEntityService.selectByPrimaryKey(Long.valueOf(dataServiceId));
        if (dataService.getType() == 1) {
            return Response.error(ErrType.BAD_REQUEST, "Don't support push type");
        }
        Long custAppId = dataService.getApplyConfiguration().getCustAppId();
        DataCustApp custApp = custAppService.selectByPrimaryKey(custAppId);

        String accessKey = custApp.getAccessKey();
        String[] accessIp = custApp.getAccessIp();
        if (accessKey.equals(requestKey) && Arrays.asList(accessIp).contains(host)) {
            Long dataResId = dataService.getApplyConfiguration().getDataResId();
            DataResource dataResource = dataResourceService.selectByPrimaryKey(dataResId);
            Dataset dataset = datasetService.selectByPrimaryKey(dataResource.getDatasetId());
            //todo 获取dataset需要返回的字段列表
            List<FieldMapping> dataServiceFieldMappings = dataService.getFieldMappings();
            List<String> includes = new ArrayList<>();
            Map<String, String> fieldMap = new HashMap<>();
            for (FieldMapping fieldMapping : dataServiceFieldMappings) {
                includes.add(fieldMapping.getSourceField());
                fieldMap.put(fieldMapping.getSourceField(), fieldMapping.getTargetField());
            }
            SearchResponse searchResponse = elasticSearchService.query(dataset.getStorageConfigurations(),
                    offset, size, includes.toArray(new String[0]));
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            Map<String, Object> res = new HashMap<>();
            for (SearchHit hit : searchHits) {
                Map<String, Object> map = hit.getSourceAsMap();
                for (String key : map.keySet()) {
                    res.put(fieldMap.get(key), map.get(key));
                }
            }
            return Response.ok(res);

        } else {
            return Response.error(ErrType.BAD_REQUEST, "Wrong accessKey or accessIp");
        }
    }


}
