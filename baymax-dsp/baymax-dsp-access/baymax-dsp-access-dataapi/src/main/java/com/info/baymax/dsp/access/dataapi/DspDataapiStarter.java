package com.info.baymax.dsp.access.dataapi;

import com.merce.woven.metrics.config.EnableElasticMetricsExport;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@EnableElasticMetricsExport
@EnableCaching
@SpringCloudApplication
@EnableTransactionManagement(proxyTargetClass = true)
@EnableFeignClients(basePackages = {"com.info.baymax.dsp.access.dataapi"})
@ComponentScan(basePackages = {"com.info.baymax","com.merce.woven"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@PropertySource(value = {"classpath:dsp-common.properties", "classpath:dsp-access-dataapi.properties"})
public class DspDataapiStarter {
    public static void main(String[] args) {
        // @formatter:off
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        new SpringApplicationBuilder()
            .bannerMode(Banner.Mode.OFF)
            .properties()
            .sources(DspDataapiStarter.class)
            .web(WebApplicationType.REACTIVE)
            .run(args);
        // @formatter:on
    }
}
