package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.example.FieldGroup;
import com.info.baymax.dsp.data.consumer.constant.DataServiceStatus;
import com.info.baymax.dsp.data.consumer.constant.DataServiceType;
import com.info.baymax.dsp.data.consumer.constant.ScheduleJobStatus;
import com.info.baymax.dsp.data.dataset.entity.core.FlowExecution;
import com.info.baymax.dsp.data.dataset.service.core.FlowExecutionService;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/service")
@Api(tags = "数据管理： 数据服务接口", value = "数据服务接口")
public class PlatDataServiceController implements BaseEntityController<DataService> {

    @Autowired
    private DataServiceEntityService dataServiceEntityService;

    @Autowired
    private FlowExecutionService flowExecutionService;

    @Override
    public BaseEntityService<DataService> getBaseEntityService() {
        return dataServiceEntityService;
    }

    @ApiOperation(value = "启用停用", notes = "服务启用停用接口")
    @PostMapping("/updateStatus")
    @ResponseBody
    public Response<?> updateStatus(@ApiParam(value = "启用停用对象，传ID和状态值", required = true) @RequestBody DataService t) {
        if (t == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询记录ID不能为空");
        }
        DataService dataService = dataServiceEntityService.selectByPrimaryKey(t.getId());
        dataService.setStatus(t.getStatus());
        if(t.getExpiredTime() != null && t.getExpiredTime() > 0){
            dataService.setExpiredTime(t.getExpiredTime());
        }
        dataService.setLastModifiedTime(new Date());
        dataService.setLastModifier(SaasContext.getCurrentUsername());
        if (t.getStatus() == DataServiceStatus.SERVICE_STATUS_DEPLOYED) {
            dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_READY);
        }else if(t.getStatus() == DataServiceStatus.SERVICE_STATUS_STOPPED){
            dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_TO_STOP);
        }
        dataServiceEntityService.update(dataService);
        return Response.ok();
    }

    /**
     * 数据服务一但生成就不支持修改了，如果后期允许修改涉及到很多属性的置空
     */

    @Override
    public Response<DataService> infoById(@ApiParam(value = "记录ID", required = true) @RequestParam Long id) {
        if (id == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询记录ID不能为空");
        }

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
        if (StringUtils.isEmpty(flowId)) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询条件不能为空");
        }
        if (query == null) {
            query = ExampleQuery.builder(FlowExecution.class);
        }
        if (query.getFieldGroup() == null) {
            query.setFieldGroup(new FieldGroup());
        }
        query.getFieldGroup().andEqualTo("flowId", flowId);
        query.getFieldGroup().andEqualTo("tenantId", SaasContext.getCurrentTenantId());
        return Response.ok(flowExecutionService.selectPage(query));
    }
}
