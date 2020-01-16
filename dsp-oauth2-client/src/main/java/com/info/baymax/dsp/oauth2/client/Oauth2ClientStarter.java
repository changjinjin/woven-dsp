package com.info.baymax.dsp.oauth2.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringCloudApplication
@EnableSwagger2WebFlux
@ComponentScan(basePackages = { "com.info.baymax" })
@EnableAutoConfiguration
public class Oauth2ClientStarter {
	public static void main(String[] args) {
		SpringApplication.run(Oauth2ClientStarter.class, args);
	}
}
