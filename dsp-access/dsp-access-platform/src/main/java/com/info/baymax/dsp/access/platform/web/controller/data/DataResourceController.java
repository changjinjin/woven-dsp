package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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
    @PostMapping("/close")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Response<?> closeDataResource(@RequestBody Long[] ids) throws Exception {
        // --TODO-- 根据dataResourceId关联更新consumer_data_application record
        // status,禁止申请权限,或者删除记录
        // --TODO-- updateOpenStatus 0
        log.info("close dataResource and delete dataApplication, ids.size={}...", ids.length);
//        dataApplicationService.deleteByIds(SaasContext.getCurrentTenantId(), ids);
        dataResourceService.closeDataResource(Arrays.asList(ids));
        return Response.ok();
    }

}
