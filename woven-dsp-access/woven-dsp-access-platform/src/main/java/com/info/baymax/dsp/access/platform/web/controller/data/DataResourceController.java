package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/13 16:41 http://xxx.yyy/api/dsp/platform/
 */
@Slf4j
@Api(tags = "数据资源相关接口", description = "数据资源相关接口")
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
    public Response<?> openDataResource(DataResource drs) throws Exception {
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
    public Response<?> closeDataResource(List<Long> ids) throws Exception {
        // --TODO-- 根据dataResourceId关联更新consumer_data_application record status,禁止申请权限,或者删除记录
        // --TODO-- updateOpenStatus 0
        log.info("close dataResource and delete dataApplication, ids.size={}...", ids.size());
        dataApplicationService.deleteByIds(SaasContext.getCurrentTenantId(), ids.toArray());
        dataResourceService.closeDataResource(ids);
        return Response.ok();
    }

}
