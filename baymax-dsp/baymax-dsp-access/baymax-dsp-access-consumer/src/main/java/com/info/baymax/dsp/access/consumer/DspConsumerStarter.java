package com.info.baymax.dsp.access.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableFeignClients(basePackages = { "com.info.baymax.dsp.access.consumer" })
@ComponentScan(basePackages = { "com.info.baymax", "com.merce.woven" })
@EntityScan(basePackages = { "com.info.baymax.dsp.data.**.entity" })
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@PropertySource(value = {"classpath:dsp-common.properties", "classpath:dsp-access-consumer.properties"}, ignoreResourceNotFound = true)
public class DspConsumerStarter {

	public static void main(String[] args) {
		log.info("JAVA CLASS PATH = " + System.getProperty("java.class.path"));
		SpringApplication.run(DspConsumerStarter.class, args);
	}
}
