package com.info.baymax.dsp.access.dataapi;

import com.merce.woven.metrics.config.EnableElasticMetricsExport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableElasticMetricsExport
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = { "com.info.baymax", "com.merce.woven" })
@EntityScan(basePackages = { "com.info.baymax.dsp.data.**.entity" })
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@PropertySource(value = {"classpath:dsp-common.properties", "classpath:dsp-access-dataapi.properties"}, ignoreResourceNotFound = true)
public class DspDataapiStarter {
	public static void main(String[] args) {
		log.info("JAVA CLASS PATH = " + System.getProperty("java.class.path"));
		// System.setProperty("es.set.netty.runtime.available.processors", "false");
		SpringApplication.run(DspDataapiStarter.class, args);
	}
}
