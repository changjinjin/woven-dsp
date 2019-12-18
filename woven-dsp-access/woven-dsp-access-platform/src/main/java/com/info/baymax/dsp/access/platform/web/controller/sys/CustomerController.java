package com.info.baymax.dsp.access.platform.web.controller.sys;

import com.info.baymax.common.comp.serialize.annotation.JsonBody;
import com.info.baymax.common.comp.serialize.annotation.JsonBodys;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.data.sys.entity.security.Customer;
import com.info.baymax.dsp.data.sys.service.security.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "消费者管理", description = "消费者管理")
@RestController
@RequestMapping("/cust")
public class CustomerController {

    @Autowired
    private CustomerService consumerService;

    @ApiOperation(value = "新加消费者信息")
    @PostMapping("save")
    @ResponseBody
    public Response<?> save(@ApiParam(value = "消费者信息") @RequestBody Customer t) {
        consumerService.saveOrUpdate(t);
        return Response.ok();
    }

    @ApiOperation(value = "更新消费者信息")
    @PostMapping("update")
    @ResponseBody
    public Response<?> update(@ApiParam(value = "消费者信息") @RequestBody Customer t) {
        consumerService.saveOrUpdate(t);
        return Response.ok();
    }

    @ApiOperation(value = "删除消费者信息")
    @GetMapping("delete")
    @ResponseBody
    public Response<?> delete(@ApiParam(value = "消费者ID列表", required = true) @RequestParam List<Long> ids) {
        consumerService.deleteByPrimaryKeys(ids);
        return Response.ok();
    }

    @ApiOperation(value = "分页查询消费者信息")
    @PostMapping("page")
    @ResponseBody
    @JsonBodys({@JsonBody(type = Customer.class, excludes = "password")})
    public Response<IPage<Customer>> page(@ApiParam(value = "查询条件") @RequestBody ExampleQuery query) {
        return Response.ok(consumerService.selectPage(ExampleQuery.builder(query)));
    }

    @ApiOperation(value = "查询消费者信息")
    @GetMapping("infoById")
    @ResponseBody
    @JsonBodys({@JsonBody(type = Customer.class, excludes = "password")})
    public Response<Customer> infoById(@ApiParam(value = "消费者ID", required = true) @RequestParam Long id) {
        return Response.ok(consumerService.selectByPrimaryKey(id));
    }
}
