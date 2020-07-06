package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.dsp.data.consumer.constant.DataServiceType;
import com.info.baymax.dsp.data.dataset.entity.core.FlowExecution;
import com.info.baymax.dsp.data.dataset.service.core.FlowExecutionService;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service")
@Api(tags = "消费端: 数据服务接口", value = "数据服务接口")
public class CustDataServiceController implements BaseEntityController<DataService> {

    @Autowired
    DataServiceEntityService dataServiceEntityService;

    @Autowired
    private FlowExecutionService flowExecutionService;

    @Override
    public BaseEntityService<DataService> getBaseEntityService() {
        return dataServiceEntityService;
    }

    @Override
    public Response<IPage<DataService>> page(
        @ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        return Response.ok(dataServiceEntityService.selectPage(ExampleQuery.builder(query)
            .fieldGroup(FieldGroup.builder().andEqualTo("custId", SaasContext.getCurrentUserId()))));
    }

    @Override
    public Response<DataService> infoById(@ApiParam(value = "记录ID", required = true) @RequestParam Long id) {
        DataService dataService = dataServiceEntityService.selectByPrimaryKey(id);
        if (dataService.getType() == DataServiceType.SERVICE_TYPE_PULL) {
            dataService.setExecutedTimes(null);
            dataService.setFailedTimes(null);
            dataService.setLastExecutedTime(null);
            dataService.setIsRunning(null);
            dataService.setJobInfo(null);

        } else if (dataService.getType() == DataServiceType.SERVICE_TYPE_PUSH) {
            dataService.setUrl(null);
            dataService.setPath(null);
            dataService.setPullConfiguration(null);
        }
        return Response.ok(dataService);
    }

    @ApiOperation(value = "查找Execution", notes = "多条件查询Execution")
    @ResponseBody
    @PostMapping("/tasklist/{flowId}")
    public Response<IPage<FlowExecution>> query(@PathVariable String flowId, @RequestBody ExampleQuery query) {
        query = ExampleQuery.builder(query);
        query.fieldGroup().andEqualTo("flowId", flowId).andEqualTo("tenantId", SaasContext.getCurrentTenantId());
        return Response.ok(flowExecutionService.selectPage(query));
    }

}
