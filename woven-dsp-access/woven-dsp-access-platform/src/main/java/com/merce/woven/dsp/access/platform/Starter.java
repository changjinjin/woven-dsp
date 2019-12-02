package com.merce.woven.dsp.access.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringCloudApplication
//@EnableUserInfoTransmitter
@EnableFeignClients(basePackages = { "com.merce.woven.dsp.access.platform" })
@ComponentScan(basePackages = { "com.merce.woven.dsp.access.platform" })
//@MapperScan(basePackages = { "com.jusfoun.services.ops.server.mapper" })
//@PropertySource(value = { "classpath:/access-platform*.properties" })
public class Starter {
	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}
}
