package com.info.baymax.dsp.access.dataapi.web;

import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.page.IPage;
import com.info.baymax.dsp.access.dataapi.request.PullRequest;
import com.info.baymax.dsp.access.dataapi.request.PullResponse;
import com.info.baymax.dsp.access.dataapi.service.PullService;
import com.info.baymax.dsp.access.dataapi.service.RestSignService;
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
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;

@RestController
@RequestMapping("/dataapi")
@Api(tags = "数据服务接口", value = "数据拉取接口")
@Slf4j
public class DataApiController implements Serializable {

    private static final long serialVersionUID = -5006451148239176107L;

    @Autowired
    private DataServiceEntityService dataServiceEntityService;

    @Autowired
    private DataCustAppService custAppService;

    @Autowired
    private DataResourceService dataResourceService;

    @Autowired
    private DatasetService datasetService;

    @Autowired
    private PullService pullService;

    @Autowired
    private RestSignService restSignService;

    @ApiOperation(value = "获取秘钥接口")
    @GetMapping("/secertkey")
    public Response<String> secertkey(
        @ApiParam(value = "应用accessKey", required = true) @RequestParam String accessKey) {
        return Response.ok(restSignService.secertkey(accessKey));
    }

    @ApiOperation(value = "数据拉取接口")
    @PostMapping("/pull")
    public PullResponse pullData(@ApiParam(value = "数据拉取是请求信息", required = true) @RequestBody PullRequest request,
                                 @ApiParam(value = "请求端hosts信息，需要与申请应用对相应", required = true) @RequestHeader String hosts) {
        String dataServiceId = request.getDataServiceId();
        String requestKey = request.getAccessKey();
        String host = hosts.split(",")[0];
        int offset = request.getOffset();
        int size = request.getSize();

        if (dataServiceId == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "请求缺少dataServiceId参数");
        }
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
        if (accessKey.equals(requestKey) && Arrays.asList(accessIp).contains(host)) {
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
            List<Map<String, Object>> list = pullService.query(dataset.getStorage(), fieldMap,
                dataset.getStorageConfigurations(), offset, size, includes.toArray(new String[0]));
            return PullResponse.ok(IPage.<Map<String, Object>>of(1, list.size(), list.size(), list)).request(request)
                .encrypt(restSignService.signKeyIfExist(accessKey));
        } else {
            throw new ControllerException(ErrType.BAD_REQUEST, "Wrong accessKey or accessIp");
        }
    }
}
