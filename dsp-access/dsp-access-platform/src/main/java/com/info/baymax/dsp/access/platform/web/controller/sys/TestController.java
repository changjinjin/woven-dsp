package com.info.baymax.dsp.access.platform.web.controller.sys;

import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.common.swagger.annotation.ApiModelMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
@Api(tags = "系统管理：测试文档注解", value = "测试文档注解接口定义", hidden = true)
public class TestController {

    @ApiOperation(value = "测试@ApiModelMap注解使用", hidden = true)
    @PostMapping("map")
    public Response<Map<String, Object>> test1(@ApiModelMap({
        @ApiModelProperty(name = "id", value = "主键", dataType = "long", required = true, notes = "主键值，long类型"),
        @ApiModelProperty(name = "name", value = "名称", dataType = "string", required = true, notes = "名称值，string类型"),
        @ApiModelProperty(name = "age", value = "年龄", dataType = "int", required = false, notes = "年龄值，int类型")}) Map<String, Object> map) {
        return Response.ok(map);
    }

    @ApiOperation(value = "测试@ApiModelMap注解使用", hidden = true)
    @PostMapping("map1")
    public Response<Map<String, Object>> test2(@ApiModelMap({
        @ApiModelProperty(name = "id", value = "主键", dataType = "long", required = true, notes = "主键值，long类型"),
        @ApiModelProperty(name = "name", value = "名称", dataType = "string", required = true, notes = "名称值，string类型"),
        @ApiModelProperty(name = "age", value = "年龄", dataType = "int", required = false, notes = "年龄值，int类型")}) Map<String, Object> map) {
        return Response.ok(map);
    }

}
