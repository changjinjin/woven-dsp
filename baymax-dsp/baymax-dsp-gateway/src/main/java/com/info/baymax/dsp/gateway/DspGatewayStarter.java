package com.info.baymax.dsp.gateway;

import com.info.baymax.dsp.gateway.config.YamlPropertySourceFactory;
import com.merce.woven.metrics.config.EnableElasticMetricsExport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableElasticMetricsExport
@ComponentScan(basePackages = {"com.info.baymax"})
@EnableFeignClients(basePackages = "com.info.baymax.dsp.gateway.feign")
@PropertySource(value = {"classpath:/dsp-gateway.yml"}, factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
public class DspGatewayStarter {
    public static void main(String[] args) {
        log.info("JAVA CLASS PATH = " + System.getProperty("java.class.path"));
		SpringApplication.run(DspGatewayStarter.class, args);
    }
}
