package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.entity.base.BaseEntityService;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.web.base.BaseEntityController;
import com.info.baymax.dsp.data.consumer.constant.DataServiceStatus;
import com.info.baymax.dsp.data.consumer.constant.ScheduleJobStatus;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.dataset.bean.FieldMapping;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/13 16:41 http://xxx.yyy/api/dsp/platform/
 */
@Slf4j
@Api(tags = "数据管理：数据资源相关接口", description = "数据资源相关接口")
@RestController
@RequestMapping("/datares")
public class DataResourceController implements BaseEntityController<DataResource> {
    @Autowired
    private DataResourceService dataResourceService;
    @Autowired
    private DataApplicationService dataApplicationService;
    @Autowired
    private DataServiceService dataServiceEntityService;

    @Override
    public BaseEntityService<DataResource> getBaseEntityService() {
        return dataResourceService;
    }

    @ApiOperation(value = "开放数据资源给消费者")
    @PostMapping("/open")
    public Response<?> openDataResource(@RequestBody DataResource drs) throws Exception {
        // --TODO-- updateOpenStatus 1 and updateDataPolicy
        log.info("publish dataResource, id={} ...", drs.getId());
        if (drs.getOpenStatus() == 1) {
            dataResourceService.saveOrUpdate(drs);
        } else {
            throw new RuntimeException("Open DataResource but openStatus is 0");
        }
        return Response.ok().build();
    }

    @ApiOperation(value = "关闭某数据资源的申请权限")
    @PostMapping("/close/{tag}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Response<?> closeDataResource(@RequestBody Long[] ids, @PathVariable Integer tag) throws Exception {
        // --TODO-- 根据dataResourceId关联更新consumer_data_application record
        // status,禁止申请权限,或者删除记录
        // --TODO-- updateOpenStatus 0
        log.info("close dataResource and delete dataApplication, ids.size={}...", ids.length);
        // dataApplicationService.deleteByIds(SaasContext.getCurrentTenantId(), ids);
        dataResourceService.closeDataResource(Arrays.asList(ids));
        // tag 为1 表示自动停止数据资源关联数据申请和数据服务
        if (tag == 1) {
            ExampleQuery dataApplicationQuery = ExampleQuery.builder(DataApplication.class)
                .fieldGroup(FieldGroup.builder().andIn("dataResId", ids));
            List<DataApplication> list = dataApplicationService.selectList(dataApplicationQuery);
            for (DataApplication dataApplication : list) {
                // 更新申请状态: 待审批申请状态置为拒绝
                if (dataApplication.getStatus() == 0) {
                    dataApplicationService.updateDataApplicationStatus(dataApplication.getId(), -1);
                }
                // 更新服务状态: 数据资源相关联的数据服务状态置为停止
                dataServiceEntityService.updateStatusByApplicationId(dataApplication.getId(),
                    DataServiceStatus.SERVICE_STATUS_STOPPED, ScheduleJobStatus.JOB_STATUS_TO_STOP);
            }
        }
        return Response.ok().build();
    }

    @ApiOperation(value = "查询详情", notes = "根据ID查询单条数据的详情，ID不能为空")
    @GetMapping("/infoById")
    @ResponseBody
    public Response<DataResource> infoById(@ApiParam(value = "记录ID", required = true) @RequestParam Long id) {
        DataResource dataResource = dataResourceService.selectByPrimaryKey(id);
        List<FieldMapping> list = new ArrayList<>();
        List<FieldMapping> fieldMappings = dataResource.getFieldMappings();
        if(null != fieldMappings && fieldMappings.size() > 0){
            SaasContext currentSaasContext = SaasContext.getCurrentSaasContext();
            String userType = currentSaasContext.getUserType();
            if(!userType.isEmpty() && userType.equals("Customer")){
                for(FieldMapping field : fieldMappings){
                    if(!field.getTargetField().isEmpty()){
                        list.add(field);
                    }
                }
                dataResource.setFieldMappings(list);
            }
        }
        return Response.ok(dataResource);
    }
}
