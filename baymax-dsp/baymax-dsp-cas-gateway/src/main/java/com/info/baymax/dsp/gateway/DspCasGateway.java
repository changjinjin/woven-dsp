package com.info.baymax.dsp.gateway;

import com.info.baymax.dsp.gateway.config.YamlPropertySourceFactory;
import com.info.baymax.security.cas.reactive.client.config.EnableCasClientWebflux;
import com.merce.woven.metrics.config.EnableElasticMetricsExport;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import tk.mybatis.spring.annotation.MapperScan;

@EnableElasticMetricsExport
@SpringCloudApplication
@ComponentScan(basePackages = {"com.info.baymax"})
@MapperScan(basePackages = {"com.info.baymax.dsp.data.**.mapper"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@EnableCasClientWebflux
@PropertySource(value = {"classpath:/dsp-cas-gateway.yml"}, factory = YamlPropertySourceFactory.class)
public class DspCasGateway {
    public static void main(String[] args) {
        // @formatter:off
        new SpringApplicationBuilder()
            .bannerMode(Banner.Mode.OFF)
            .properties()
            .sources(DspCasGateway.class)
            .web(WebApplicationType.REACTIVE)
            .run(args);
        // @formatter:on
    }
}
