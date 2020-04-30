package com.info.baymax.dsp.access.consumer;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;
import tk.mybatis.spring.annotation.MapperScan;

@SpringCloudApplication
@EnableSwagger2WebFlux
@EnableAutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableFeignClients(basePackages = {"com.info.baymax.dsp.access.consumer"})
@ComponentScan(basePackages = {
    "com.info.baymax"}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
    DocumentationPluginsBootstrapper.class}))
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@PropertySource(value = {"classpath:dsp-common.properties", "classpath:dsp-access-consumer.properties"})
public class DspConsumerStarter {

    public static void main(String[] args) {
        // @formatter:off
        new SpringApplicationBuilder()
            .bannerMode(Banner.Mode.OFF)
            .properties()
            .sources(DspConsumerStarter.class)
            .web(WebApplicationType.REACTIVE)
            .run(args);
        // @formatter:on
    }
}
