package com.info.baymax.dsp.access.dataapi;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.info.baymax.common.comp.config.profile.EnableExtProperties;

import tk.mybatis.spring.annotation.MapperScan;

@SpringCloudApplication
@EnableFeignClients(basePackages = {"com.info.baymax.dsp.access.platform"})
@ComponentScan(basePackages = {"com.info.baymax.dsp.access.platform"})
@MapperScan(basePackages = {"com.jusfoun.services.ops.server.mapper"})
@EnableExtProperties("classpath*:**/access-dataapi*.properties")
public class Starter {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}
