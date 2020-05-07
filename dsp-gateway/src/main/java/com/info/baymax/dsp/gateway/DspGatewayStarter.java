package com.info.baymax.dsp.gateway;

import com.info.baymax.dsp.gateway.config.YamlPropertySourceFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;

@SpringCloudApplication
@ComponentScan(basePackages = {
    "com.info.baymax"}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
    DocumentationPluginsBootstrapper.class}))
@EnableAutoConfiguration
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
