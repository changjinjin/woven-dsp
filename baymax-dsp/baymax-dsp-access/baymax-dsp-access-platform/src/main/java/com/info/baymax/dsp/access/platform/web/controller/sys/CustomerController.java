package com.info.baymax.dsp.access.platform.web.controller.sys;

import com.info.baymax.common.comp.base.MainTableController;
import com.info.baymax.common.comp.serialize.annotation.JsonBody;
import com.info.baymax.common.comp.serialize.annotation.JsonBodys;
import com.info.baymax.common.entity.base.BaseMaintableService;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.result.Response;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.access.platform.config.SysInitConfig;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import com.info.baymax.dsp.data.sys.entity.security.Customer;
import com.info.baymax.dsp.data.sys.service.security.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.constraints.NotEmpty;

@Api(tags = "系统管理：消费者管理", description = "消费者管理")
@RestController
@RequestMapping("/cust")
public class CustomerController implements MainTableController<Customer> {

    @Autowired
    private CustomerService consumerService;
    @Autowired
    private DataCustAppService dataCustAppService;
    @Autowired
    private SysInitConfig sysInitConfig;

    @Override
    public BaseMaintableService<Customer> getBaseMaintableService() {
        return consumerService;
    }

    @JsonBodys({@JsonBody(type = Customer.class, excludes = "password")})
    @Override
    public Response<IPage<Customer>> page(ExampleQuery query) {
        return MainTableController.super.page(query);
    }

    @JsonBodys({@JsonBody(type = Customer.class, excludes = "password")})
    @Override
    public Response<Customer> infoById(String id) {
        return MainTableController.super.infoById(id);
    }

    @ApiOperation(value = "重置用户密码")
    @PostMapping("/resetPwd")
    public Response<?> resetPwd(@ApiParam(value = "重置密码的用户ID数组", required = true) @RequestParam @NotEmpty String[] ids) {
        consumerService.resetPwd(ids, sysInitConfig.getPassword());
        return Response.ok().build();
    }

    @ApiOperation(value = "修改用户启用停用状态", notes = "根据ID修改用户的启用或停用状态，需要传用户ID和修改的目标状态值（0-停用，1-启用），批量操作")
    @PostMapping("/resetStatus")
    public Response<?> resetStatus(@ApiParam(value = "修改状态的对象列表", required = true) @RequestBody @NotEmpty List<Customer> list) {
        consumerService.resetStatus(list);
        return Response.ok().build();
    }

    @ApiOperation(value = "消费者应用配置查询", notes = "消费者应用配置查询")
    @PostMapping("/appPage")
    public Response<IPage<DataCustApp>> appPage(@ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        return Response.ok(dataCustAppService.selectPage(query));
    }

    @ApiOperation(value = "查询接入配置详情", notes = "根据ID查询单条数据的详情，ID不能为空")
    @GetMapping("/app/{id}")
    @ResponseBody
    public Response<DataCustApp> queryAppById(@ApiParam(value = "记录ID", required = true) @PathVariable Long id) {
        return Response.ok(dataCustAppService.selectByPrimaryKey(id));
    }
}
