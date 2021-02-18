package com.info.baymax.dsp.gateway;

import com.info.baymax.dsp.gateway.config.YamlPropertySourceFactory;
import com.merce.woven.metrics.config.EnableElasticMetricsExport;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableDiscoveryClient
@EnableElasticMetricsExport
@ComponentScan(basePackages = {"com.info.baymax"})
@EnableFeignClients(basePackages = "com.info.baymax.dsp.gateway.feign")
@PropertySource(value = {"classpath:/dsp-gateway.yml"}, factory = YamlPropertySourceFactory.class)
public class DspGatewayStarter {
    public static void main(String[] args) {
        // @formatter:off
        new SpringApplicationBuilder()
            .bannerMode(Banner.Mode.OFF)
            .properties()
            .sources(DspGatewayStarter.class)
            .web(WebApplicationType.REACTIVE)
            .run(args);
        // @formatter:on
    }
}
