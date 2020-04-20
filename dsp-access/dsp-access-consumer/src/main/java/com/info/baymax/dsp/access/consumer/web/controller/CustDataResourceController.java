package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "消费端：数据资源查询相关接口", description = "数据资源查询相关接口")
@RestController
@RequestMapping("/datares")
public class CustDataResourceController {
    @Autowired
    private DataResourceService dataResourceService;

    @ApiOperation(value = "消费者查询可申请的数据资源，分页查询")
    @PostMapping("/page")
    public Response<IPage<DataResource>> queryPage(@ApiParam("查询条件") @RequestBody ExampleQuery query) {
        return Response.ok(dataResourceService.selectPage(ExampleQuery.builder(query).fieldGroup()
            .andEqualTo("tenantId", SaasContext.getCurrentTenantId()).andEqualTo("openStatus", 1).end()));
    }

    @ApiOperation(value = "查询详情", notes = "根据ID查询单条数据的详情，ID不能为空")
    @GetMapping("/infoById")
    @ResponseBody
    public Response<DataResource> infoById(@ApiParam(value = "记录ID", required = true) @RequestParam Long id) {
        if (id == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询记录ID不能为空");
        }
        return Response.ok(dataResourceService.selectByPrimaryKey(id));
    }
}
