package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.example.FieldGroup;
import com.info.baymax.dsp.data.consumer.constant.DataServiceType;
import com.info.baymax.dsp.data.dataset.entity.core.FlowExecution;
import com.info.baymax.dsp.data.dataset.service.core.FlowExecutionService;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
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
        if (query == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询条件不能为空");
        }
        if(query.getFieldGroup() == null){
            query.setFieldGroup(new FieldGroup());
        }
        query.getFieldGroup().andEqualTo("custId", SaasContext.getCurrentUserId());
        return Response.ok(dataServiceEntityService.selectPage(query));
    }


    @Override
    public Response<DataService> infoById(@ApiParam(value = "记录ID", required = true) @RequestParam Long id) {
        if (id == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询记录ID不能为空");
        }

        DataService dataService = dataServiceEntityService.selectByPrimaryKey(id);
        if(dataService.getType() == DataServiceType.SERVICE_TYPE_PULL){
            dataService.setTotalExecuted(null);
            dataService.setExecutedTimes(null);
            dataService.setFailedTimes(null);
            dataService.setLastExecutedTime(null);
            dataService.setIsRunning(null);
            dataService.setJobInfo(null);

        }else if(dataService.getType() == DataServiceType.SERVICE_TYPE_PUSH){
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
        if (StringUtils.isEmpty(flowId)) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询条件不能为空");
        }
        if(query == null) {
            query = ExampleQuery.builder(FlowExecution.class);
        }
        if(query.getFieldGroup() == null){
            query.setFieldGroup(new FieldGroup());
        }
        query.getFieldGroup().andEqualTo("flowId", flowId);
        query.getFieldGroup().andEqualTo("tenantId", SaasContext.getCurrentTenantId());
        return Response.ok(flowExecutionService.selectPage(query));
    }

}
