package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.example.FieldGroup;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "消费端：数据资源查询相关接口", description = "数据资源查询相关接口")
@RestController
@RequestMapping("/datares")
public class CustDataResourceController {
    @Autowired
    private DataResourceService dataResourceService;

    @ApiOperation(value = "消费者查询可申请的数据资源，分页查询")
    @PostMapping("/page")
    public Response<IPage<DataResource>> queryPage(@ApiParam("查询条件") @RequestBody ExampleQuery query) {
        if (query.getFieldGroup() == null) {
            query.setFieldGroup(new FieldGroup());
        }
        query.getFieldGroup().andEqualTo("tenantId", SaasContext.getCurrentTenantId()).andEqualTo("openStatus", 1);
        return Response.ok(dataResourceService.selectPage(query));
    }
}
