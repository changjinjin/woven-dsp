package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    private DataServiceEntityService dataServiceEntityService;

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
        return Response.ok();
    }

    @ApiOperation(value = "关闭某数据资源的申请权限")
    @PostMapping("/close/{tag}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Response<?> closeDataResource(@RequestBody Long[] ids, @PathVariable Integer tag) throws Exception {
        // --TODO-- 根据dataResourceId关联更新consumer_data_application record
        // status,禁止申请权限,或者删除记录
        // --TODO-- updateOpenStatus 0
        log.info("close dataResource and delete dataApplication, ids.size={}...", ids.length);
//        dataApplicationService.deleteByIds(SaasContext.getCurrentTenantId(), ids);
        dataResourceService.closeDataResource(Arrays.asList(ids));
        //tag 为1 表示自动停止数据资源关联数据申请和数据服务
        if (tag == 1) {
            ExampleQuery dataApplicationQuery = ExampleQuery.builder(DataApplication.class)
                    .fieldGroup()
                    .andIn("dataResId", ids)
                    .end();
            List<DataApplication> list = dataApplicationService.selectList(dataApplicationQuery);
            for (DataApplication dataApplication : list) {
                //更新申请状态: 待审批申请状态置为拒绝
                if (dataApplication.getStatus() == 0) {
                    dataApplicationService.updateDataApplicationStatus(dataApplication.getId(), -1);
                }
                //更新服务状态: 数据资源相关联的数据服务状态置为停止
                dataServiceEntityService.updateStatusByApplicationId(dataApplication.getId(), 2);
            }
        }
        return Response.ok();
    }

}
