package com.info.baymax.dsp.access.dataapi.web;

import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import com.info.baymax.dsp.data.platform.service.ElasticSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/dataapi")
@Api(tags = "数据服务接口", value = "数据拉取接口")
public class DataApiController implements Serializable {

    private static final long serialVersionUID = -5006451148239176107L;

    @Autowired
    DataServiceEntityService dataServiceEntityService;

    @Autowired
    DataApplicationService dataApplicationService;

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
    public Response pullData(@RequestBody Map<String, String> body) throws Exception {
        String dataServiceId = body.get("dataServiceId");
        String requestKey = body.get("accessKey");

        if (dataServiceId == null) {
            return Response.error(ErrType.BAD_REQUEST, "Missing dataServiceId param");
        }
        DataService dataService = dataServiceEntityService.selectByPrimaryKey(Long.valueOf(dataServiceId));
        if (dataService.getType() == 1) {
            return Response.error(ErrType.BAD_REQUEST, "Don't support push type");
        }
        Long dataApplicationId = dataService.getApplicationId();
        DataApplication dataApplication = dataApplicationService.selectByPrimaryKey(dataApplicationId);

        Long custAppId = dataApplication.getCustAppId();
        DataCustApp custApp = custAppService.selectByPrimaryKey(custAppId);

        String accessKey = custApp.getAccessKey();
        String[] accessIp = custApp.getAccessIp();
        if (accessKey.equals(requestKey)) {
            Long dataResId = dataApplication.getDataResId();
            DataResource dataResource = dataResourceService.selectByPrimaryKey(dataResId);
            Dataset dataset = datasetService.selectByPrimaryKey(dataResource.getDatasetId());
            return Response.ok(elasticSearchService.query(dataset.getStorageConfigurations()));

        } else {
            return Response.error(ErrType.BAD_REQUEST, "Wrong accessKey or accessIp");
        }
    }


}
