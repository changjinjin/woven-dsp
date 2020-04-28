package com.info.baymax.dsp.gateway;

import com.info.baymax.dsp.gateway.config.YamlPropertySourceFactory;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringCloudApplication
@EnableSwagger2WebFlux
@ComponentScan(basePackages = { "com.info.baymax" })
@EnableAutoConfiguration
@PropertySource(value = { "classpath:/dsp-gateway.yml" }, factory = YamlPropertySourceFactory.class)
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
