package com.merce.woven.dsp.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.jusfoun.services.common.feign.EnableUserInfoTransmitter;

import tk.mybatis.spring.annotation.MapperScan;

@SpringCloudApplication
@EnableUserInfoTransmitter
@EnableCaching
@EnableFeignClients(basePackages = { "com.jusfoun" })
@ComponentScan(basePackages = { "com.jusfoun", "com.codingapi" })
@MapperScan(basePackages = "com.jusfoun.services.auth.server.mapper")
public class Starter {

	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}
}
