package com.merce.woven.dsp.auth;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import tk.mybatis.spring.annotation.MapperScan;

@EnableCaching
@SpringCloudApplication
@EnableAutoConfiguration(exclude = { ErrorMvcAutoConfiguration.class })
@ComponentScan(basePackages = { "com.merce.woven" })
@EntityScan(basePackages = { "com.merce.woven.dsp.data.**.entity" })
@MapperScan(basePackages = "com.merce.woven.dsp.data.**.mapper")
public class Starter {

	@Bean
	public static PropertySourcesPlaceholderConfigurer getPropertyPlaceholderConfigurer() throws IOException {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		ppc.setLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:**/auth-server*.properties"));
		return ppc;
	}

	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}
}
