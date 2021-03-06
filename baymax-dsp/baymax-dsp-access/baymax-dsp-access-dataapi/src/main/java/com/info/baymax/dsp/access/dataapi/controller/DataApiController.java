package com.info.baymax.dsp.access.dataapi.controller;

import com.info.baymax.common.core.result.Response;
import com.info.baymax.dsp.access.dataapi.api.AggRequest;
import com.info.baymax.dsp.access.dataapi.api.RecordRequest;
import com.info.baymax.dsp.access.dataapi.api.SqlRequest;
import com.info.baymax.dsp.access.dataapi.config.PullLog;
import com.info.baymax.dsp.access.dataapi.service.PullService;
import com.info.baymax.dsp.access.dataapi.service.RestSignService;
import com.info.baymax.dsp.access.dataapi.utils.EncryptUtils;
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

    @ApiOperation(value = "获取秘钥接口", hidden = true)
    @GetMapping("/secertkey")
    public Response<String> secertkey(
        @ApiParam(value = "应用accessKey", required = true) @RequestParam String accessKey) {
        return Response.ok(restSignService.secertkey(accessKey));
    }

    @ApiOperation(value = "获取秘钥接口")
    @GetMapping("/secertkeyRemote")
    public Response<String> secertkeyRemote(
            @ApiParam(value = "应用accessKey", required = true) @RequestParam String accessKey) {
        return Response.ok(restSignService.secertkeyRemote(accessKey));
    }

    @ApiOperation(value = "数据记录查询接口", hidden = true)
    @PostMapping("/pullRecords")
    @PullLog
    public Response<Object> pullRecords(
            @ApiParam(value = "记录请求信息", required = true) @RequestBody @Valid RecordRequest request,
            @ApiParam(value = "请求端hosts信息，需要与申请应用对相应", required = true) @RequestHeader String hosts) {
        return Response.ok(EncryptUtils.encrypt(pullService.pullRecords(request, hosts), request.isEncrypted(),
                restSignService.signKeyIfExist(request.getAccessKey())));
    }

    @ApiOperation(value = "数据记录查询接口SQL预览", hidden = true)
    @PostMapping("/pullRecordsSql")
    public Response<String> pullRecordsSql(
            @ApiParam(value = "记录请求信息", required = true) @RequestBody @Valid RecordRequest request,
            @ApiParam(value = "请求端hosts信息，需要与申请应用对相应", required = true) @RequestHeader String hosts) {
        return Response.ok(pullService.pullRecordsSql(request, hosts));
    }

    @ApiOperation(value = "数据聚合查询接口SQL", hidden = true)
    @PostMapping("/pullAggs")
    @PullLog
    public Response<Object> pullAggs(
            @ApiParam(value = "聚合请求信息", required = true) @RequestBody @Valid AggRequest request,
            @ApiParam(value = "请求端hosts信息，需要与申请应用对应", required = true) @RequestHeader String hosts) {
        return Response.ok(EncryptUtils.encrypt(pullService.pullAggs(request, hosts), request.isEncrypted(),
                restSignService.signKeyIfExist(request.getAccessKey())));
    }

    @ApiOperation(value = "数据聚合查询接口SQL预览", hidden = true)
    @PostMapping("/pullAggsSql")
    public Response<String> pullAggsSql(
            @ApiParam(value = "聚合请求信息", required = true) @RequestBody @Valid AggRequest request,
            @ApiParam(value = "请求端hosts信息，需要与申请应用对应", required = true) @RequestHeader String hosts) {
        return Response.ok(pullService.pullAggsSql(request, hosts));
    }

    @ApiOperation(value = "数据源SQL模板方式查询", hidden = true)
    @PostMapping("/pullBySql")
    public Response<Object> pullBySql(
            @ApiParam(value = "聚合请求信息", required = true) @RequestBody @Valid SqlRequest request,
            @ApiParam(value = "请求端hosts信息，需要与申请应用对应", required = true) @RequestHeader String hosts) {
        return Response.ok(EncryptUtils.encrypt(pullService.pullBySql(request, hosts), request.isEncrypted(),
                restSignService.signKeyIfExist(request.getAccessKey())));
    }

    @ApiOperation(value = "数据记录查询接口")
    @PostMapping("/pullRecordsRemote")
    @PullLog
    public Response<Object> pullRecordsRemote(
            @ApiParam(value = "记录请求信息", required = true) @RequestBody @Valid RecordRequest request,
            @ApiParam(value = "应用accessKey", required = true) @RequestHeader String accessKey,
            @ApiParam(value = "accessToken", required = true) @RequestHeader String accessToken) {
        request.setAccessKey(accessKey);
        return Response.ok(EncryptUtils.encrypt(pullService.pullRecords(request, ""), request.isEncrypted(),
                restSignService.signKeyIfExist(request.getAccessKey())));
    }

    @ApiOperation(value = "数据聚合查询接口SQL")
    @PostMapping("/pullAggsRemote")
    @PullLog
    public Response<Object> pullAggsRemote(
            @ApiParam(value = "聚合请求信息", required = true) @RequestBody @Valid AggRequest request,
            @ApiParam(value = "应用accessKey", required = true) @RequestHeader String accessKey,
            @ApiParam(value = "accessToken", required = true) @RequestHeader String accessToken) {
        request.setAccessKey(accessKey);
        return Response.ok(EncryptUtils.encrypt(pullService.pullAggs(request, ""), request.isEncrypted(),
                restSignService.signKeyIfExist(request.getAccessKey())));
    }

    @ApiOperation(value = "数据源SQL模板方式查询")
    @PostMapping("/pullBySqlRemote")
    public Response<Object> pullBySqlRemote(
            @ApiParam(value = "聚合请求信息", required = true) @RequestBody @Valid SqlRequest request,
            @ApiParam(value = "应用accessKey", required = true) @RequestHeader String accessKey,
            @ApiParam(value = "accessToken", required = true) @RequestHeader String accessToken) {
        request.setAccessKey(accessKey);
        return Response.ok(EncryptUtils.encrypt(pullService.pullBySql(request, ""), request.isEncrypted(),
                restSignService.signKeyIfExist(request.getAccessKey())));
    }
}
