package com.info.baymax.dsp.gateway;

import com.info.baymax.common.config.YamlPropertySourceFactory;
import com.info.baymax.security.cas.reactive.client.config.EnableCasClientWebflux;
import com.merce.woven.metrics.config.EnableElasticMetricsExport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import tk.mybatis.spring.annotation.MapperScan;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableCasClientWebflux
@EnableElasticMetricsExport
@ComponentScan(basePackages = {"com.info.baymax"})
@EnableFeignClients(basePackages = "com.info.baymax.dsp.gateway.feign")
@MapperScan(basePackages = {"com.info.baymax.dsp.data.**.mapper"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@PropertySource(value = {"classpath:/dsp-cas-gateway.yml"}, factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
public class DspCasGatewayStarter {
    public static void main(String[] args) {
    	log.info("JAVA CLASS PATH = " + System.getProperty("java.class.path"));
		SpringApplication.run(DspCasGatewayStarter.class, args);
    }
}
