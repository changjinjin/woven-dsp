package com.info.baymax.dsp.access.dataapi;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.info.baymax.common.comp.config.profile.EnableExtProperties;

import tk.mybatis.spring.annotation.MapperScan;

@SpringCloudApplication
@EnableFeignClients(basePackages = {"com.info.baymax.dsp.access.dataapi"})
@ComponentScan(basePackages = {"com.info.baymax"})
@MapperScan(basePackages = {"com.jusfoun.services.ops.server.mapper"})
@EnableExtProperties({"classpath:dsp-common.properties", "classpath:dsp-access-dataapi.properties"})
public class DataapiStarter {
    public static void main(String[] args) {
        SpringApplication.run(DataapiStarter.class, args);
    }
}
