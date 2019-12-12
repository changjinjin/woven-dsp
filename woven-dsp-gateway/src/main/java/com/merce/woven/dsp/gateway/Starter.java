package com.merce.woven.dsp.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.merce.woven.common.message.result.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "test", description = "test")
@RestController
@SpringCloudApplication
@ComponentScan(basePackages = { "com.merce.woven" })
public class Starter {
	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}

	@ApiOperation("test")
	@GetMapping("test")
	public Response<?> test() {
		return Response.ok();
	}
}
