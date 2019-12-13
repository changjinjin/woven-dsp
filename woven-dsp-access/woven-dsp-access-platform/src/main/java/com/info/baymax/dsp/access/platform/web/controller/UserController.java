package com.info.baymax.dsp.access.platform.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.info.baymax.common.message.result.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "样例接口", description = "样例接口")
@RestController
@RequestMapping("/user")
public class UserController {

	@ApiOperation(value = "根据ID查询用户信息")
	@GetMapping("findById")
	@ResponseBody
	public Response<?> test(@ApiParam(value = "用户ID", required = true) @RequestParam Long id) {
		return Response.ok();
	}

}
