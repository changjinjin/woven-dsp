package com.info.baymax.dsp.access.platform.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.data.consumer.entity.Consumer;
import com.info.baymax.dsp.data.consumer.service.ConsumerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "消费者管理", description = "消费者管理")
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @ApiOperation(value = "新加消费者信息")
    @GetMapping("save")
    @ResponseBody
    public Response<?> save(@ApiParam(value = "消费者信息") @RequestBody Consumer t) {
        consumerService.saveOrUpdate(t);
        return Response.ok();
    }

    @ApiOperation(value = "更新消费者信息")
    @GetMapping("update")
    @ResponseBody
    public Response<?> update(@ApiParam(value = "消费者信息") @RequestBody Consumer t) {
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
    public Response<Consumer> infoById(@ApiParam(value = "消费者ID", required = true) @RequestParam Long id) {
        return Response.ok(consumerService.selectByPrimaryKey(id));
    }
}
