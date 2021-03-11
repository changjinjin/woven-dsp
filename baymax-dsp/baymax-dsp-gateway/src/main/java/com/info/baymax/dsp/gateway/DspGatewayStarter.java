package com.info.baymax.dsp.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import com.info.baymax.dsp.gateway.config.YamlPropertySourceFactory;
import com.merce.woven.metrics.config.EnableElasticMetricsExport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableElasticMetricsExport
@ComponentScan(basePackages = {"com.info.baymax"})
@EnableFeignClients(basePackages = "com.info.baymax.dsp.gateway.feign")
@PropertySource(value = {"classpath:/dsp-gateway.yml"}, factory = YamlPropertySourceFactory.class)
public class DspGatewayStarter {
    public static void main(String[] args) {
        // @formatter:off
    	log.info("JAVA CLASS PATH = " + System.getProperty("java.class.path"));
		SpringApplication.run(DspGatewayStarter.class, args);
    }
}
