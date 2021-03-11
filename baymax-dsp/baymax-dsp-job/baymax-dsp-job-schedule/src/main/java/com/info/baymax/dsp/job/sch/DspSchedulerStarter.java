package com.info.baymax.dsp.job.sch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: haijun
 * @Date: 2019/12/12 14:17
 */
@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.info.baymax.dsp.job.sch.client"})
@ComponentScan(basePackages = {"com.info.baymax"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@PropertySource({ "classpath:dsp-common.properties", "classpath:dsp-job-schedule.properties",
		"classpath:quartz.properties" })
public class DspSchedulerStarter {

    public static void main(String[] args) {
        log.info("JAVA CLASS PATH = " + System.getProperty("java.class.path"));
		SpringApplication.run(DspSchedulerStarter.class, args);
    }

}
