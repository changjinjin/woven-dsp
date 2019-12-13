package com.info.baymax.dsp.access.consumer;

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
@EnableFeignClients(basePackages = { "com.info.baymax.dsp.access.platform" })
@ComponentScan(basePackages = { "com.info.baymax.dsp.access.platform" })
@MapperScan(basePackages = { "com.jusfoun.services.ops.server.mapper" })
public class Starter {

	@Bean
	public static PropertySourcesPlaceholderConfigurer getPropertyPlaceholderConfigurer() throws IOException {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		ppc.setLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:**/access-consumer*.properties"));
		return ppc;
	}

	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}
}
