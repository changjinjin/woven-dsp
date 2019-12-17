package com.info.baymax.dsp.access.platform.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.data.consumer.entity.Customer;
import com.info.baymax.dsp.data.consumer.service.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
    public Response<?> delete(@ApiParam(value = "消费者ID", required = true) @RequestParam Long id) {
        consumerService.deleteByPrimaryKey(id);
        return Response.ok();
    }

    @ApiOperation(value = "查询消费者信息")
    @GetMapping("infoById")
    @ResponseBody
    public Response<Customer> infoById(@ApiParam(value = "消费者ID", required = true) @RequestParam Long id) {
        return Response.ok(consumerService.selectByPrimaryKey(id));
    }
}
