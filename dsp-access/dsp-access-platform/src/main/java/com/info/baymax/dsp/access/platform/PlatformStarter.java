package com.info.baymax.dsp.access.platform;

import com.info.baymax.common.comp.profile.EnableExtProperties;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.actuate.autoconfigure.elasticsearch.ElasticSearchRestHealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.redis.RedisHealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.redis.RedisReactiveHealthIndicatorAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;
import tk.mybatis.spring.annotation.MapperScan;

@SpringCloudApplication
@EnableSwagger2WebFlux
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAutoConfiguration(exclude = {ElasticSearchRestHealthIndicatorAutoConfiguration.class, RedisHealthIndicatorAutoConfiguration.class,
        RedisReactiveHealthIndicatorAutoConfiguration.class})
@EnableFeignClients(basePackages = {"com.info.baymax.dsp.access.platform"})
@ComponentScan(basePackages = {"com.info.baymax"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@EnableExtProperties({"classpath:dsp-common.properties", "classpath:dsp-access-platform.properties"})
public class PlatformStarter {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .properties()
                .sources(PlatformStarter.class)
                .web(WebApplicationType.REACTIVE)
                .run(args);
    }

}
