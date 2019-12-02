package com.merce.woven.dsp.access.platform.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.merce.woven.common.message.result.Response;
import com.merce.woven.dsp.access.platform.entity.User;

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
	public Response<User> findById(@ApiParam(value = "用户ID", required = true) @RequestParam Long id) {
		User user = new User();
		user.setId(id);
		return Response.ok(user);
	}

}
