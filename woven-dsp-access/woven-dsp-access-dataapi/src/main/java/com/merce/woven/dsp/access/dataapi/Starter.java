package com.merce.woven.dsp.access.dataapi;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import tk.mybatis.spring.annotation.MapperScan;

import java.io.IOException;

@SpringCloudApplication
//@EnableUserInfoTransmitter
@EnableFeignClients(basePackages = { "com.merce.woven.dsp.access.platform" })
@ComponentScan(basePackages = { "com.merce.woven.dsp.access.platform" })
@MapperScan(basePackages = { "com.jusfoun.services.ops.server.mapper" })
public class Starter {

	@Bean
	public static PropertySourcesPlaceholderConfigurer getPropertyPlaceholderConfigurer() throws IOException {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		ppc.setLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:**/access-dataapi*.properties"));
		return ppc;
	}

	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}
}
