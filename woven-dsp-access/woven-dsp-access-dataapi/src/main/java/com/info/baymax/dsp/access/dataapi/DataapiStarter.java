package com.info.baymax.dsp.access.dataapi;

import com.info.baymax.common.comp.config.profile.EnableExtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.elasticsearch.ElasticSearchRestHealthIndicatorAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringCloudApplication
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAutoConfiguration(exclude = {ElasticSearchRestHealthIndicatorAutoConfiguration.class})
@EnableFeignClients(basePackages = {"com.info.baymax.dsp.access.dataapi"})
@ComponentScan(basePackages = {"com.info.baymax"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@EnableExtProperties({"classpath:dsp-common.properties", "classpath:dsp-access-dataapi.properties"})
public class DataapiStarter {
    public static void main(String[] args) {
        SpringApplication.run(DataapiStarter.class, args);
    }
}
