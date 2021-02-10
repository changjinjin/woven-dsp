package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.config.base.BaseEntityController;
import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.entity.base.BaseEntityService;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.dsp.data.consumer.constant.DataServiceStatus;
import com.info.baymax.dsp.data.consumer.constant.DataServiceType;
import com.info.baymax.dsp.data.consumer.constant.ScheduleJobStatus;
import com.info.baymax.dsp.data.dataset.entity.core.FlowExecution;
import com.info.baymax.dsp.data.dataset.service.core.FlowExecutionService;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/service")
@Api(tags = "数据管理： 数据服务接口", value = "数据服务接口")
public class PlatDataServiceController implements BaseEntityController<DataService> {

    @Autowired
    private DataServiceService dataServiceService;
    @Autowired
    private FlowExecutionService flowExecutionService;

    @Override
    public BaseEntityService<DataService> getBaseEntityService() {
        return dataServiceService;
    }

    @ApiOperation(value = "启用停用", notes = "服务启用停用接口")
    @PostMapping("/updateStatus")
    public Response<?> updateStatus(@ApiParam(value = "启用停用对象，传ID和状态值", required = true) @RequestBody DataService t) {
        DataService dataService = dataServiceService.selectByPrimaryKey(t.getId());
        dataService.setStatus(t.getStatus());
        if (t.getExpiredTime() != null && t.getExpiredTime() > 0) {
            dataService.setExpiredTime(t.getExpiredTime());
        }
        dataService.setLastModifiedTime(new Date());
        dataService.setLastModifier(SaasContext.getCurrentUsername());
        if (t.getStatus() == DataServiceStatus.SERVICE_STATUS_DEPLOYED) {
            dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_READY);
        } else if (t.getStatus() == DataServiceStatus.SERVICE_STATUS_STOPPED) {
            dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_TO_STOP);
        }
        dataServiceService.update(dataService);
        return Response.ok().build();
    }

    /**
     * 数据服务一但生成就不支持修改了，如果后期允许修改涉及到很多属性的置空
     */
    @Override
    public Response<DataService> infoById(@ApiParam(value = "记录ID", required = true) @RequestParam Long id) {
        DataService dataService = dataServiceService.selectByPrimaryKey(id);
        if (dataService.getType() == DataServiceType.SERVICE_TYPE_PULL) {
            dataService.setExecutedTimes(null);
            dataService.setFailedTimes(null);
            dataService.setLastExecutedTime(null);
            dataService.setIsRunning(null);
            dataService.setJobInfo(null);
        } else if (dataService.getType() == DataServiceType.SERVICE_TYPE_PUSH) {
            dataService.setUrl(null);
            dataService.setPath(null);
        }
        return Response.ok(dataService);
    }

    @ApiOperation(value = "查找Execution", notes = "多条件查询Execution")
    @PostMapping("/tasklist/{flowId}")
    public Response<IPage<FlowExecution>> query(@PathVariable String flowId, @RequestBody ExampleQuery query) {
        // @formatter:off
        query = ExampleQuery
            .builder(query)
            .fieldGroup(FieldGroup.builder()
                .andEqualTo("flowId", flowId)
                .andEqualTo("tenantId", SaasContext.getCurrentTenantId())
            );
        // @formatter:on
        return Response.ok(flowExecutionService.selectPage(query));
    }
}
