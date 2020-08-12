package com.info.baymax.dsp.access.dataapi.web.controller;

import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.access.dataapi.config.PullLog;
import com.info.baymax.dsp.access.dataapi.service.PullService;
import com.info.baymax.dsp.access.dataapi.service.RestSignService;
import com.info.baymax.dsp.access.dataapi.web.request.AggRequest;
import com.info.baymax.dsp.access.dataapi.web.request.PullResponse;
import com.info.baymax.dsp.access.dataapi.web.request.RecordRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;

@RestController
@RequestMapping("/data")
@Api(tags = "数据服务接口", value = "数据拉取接口")
public class DataApiController implements Serializable {

    private static final long serialVersionUID = -5006451148239176107L;

    @Autowired
    private PullService pullService;
    @Autowired
    private RestSignService restSignService;

    @ApiOperation(value = "获取秘钥接口")
    @GetMapping("/secertkey")
    public Response<String> secertkey(
        @ApiParam(value = "应用accessKey", required = true) @RequestParam String accessKey) {
        return Response.ok(restSignService.secertkey(accessKey));
    }

    @ApiOperation(value = "数据记录查询接口")
    @PostMapping("/pullRecords")
    @PullLog
    public PullResponse pullRecords(
        @ApiParam(value = "记录请求信息", required = true) @RequestBody @Valid RecordRequest request,
        @ApiParam(value = "请求端hosts信息，需要与申请应用对相应", required = true) @RequestHeader String hosts) {
        return PullResponse.ok(pullService.pullRecords(request, hosts)).request(request)
            .encrypt(restSignService.signKeyIfExist(request.getAccessKey()));
    }

    @ApiOperation(value = "数据记录查询接口SQL预览")
    @PostMapping("/pullRecordsSql")
    public Response<String> pullRecordsSql(
        @ApiParam(value = "记录请求信息", required = true) @RequestBody @Valid RecordRequest request,
        @ApiParam(value = "请求端hosts信息，需要与申请应用对相应", required = true) @RequestHeader String hosts) {
        return Response.ok(pullService.pullRecordsSql(request, hosts));
    }

    @ApiOperation(value = "数据聚合查询接口SQL")
    @PostMapping("/pullAggs")
    @PullLog
    public PullResponse pullAggs(@ApiParam(value = "聚合请求信息", required = true) @RequestBody @Valid AggRequest request,
                                 @ApiParam(value = "请求端hosts信息，需要与申请应用对应", required = true) @RequestHeader String hosts) {
        return PullResponse.ok(pullService.pullAggs(request, hosts)).request(request)
            .encrypt(restSignService.signKeyIfExist(request.getAccessKey()));
    }

    @ApiOperation(value = "数据聚合查询接口SQL预览")
    @PostMapping("/pullAggsSql")
    public Response<String> pullAggsSql(
        @ApiParam(value = "聚合请求信息", required = true) @RequestBody @Valid AggRequest request,
        @ApiParam(value = "请求端hosts信息，需要与申请应用对应", required = true) @RequestHeader String hosts) {
        return Response.ok(pullService.pullAggsSql(request, hosts));
    }
}
