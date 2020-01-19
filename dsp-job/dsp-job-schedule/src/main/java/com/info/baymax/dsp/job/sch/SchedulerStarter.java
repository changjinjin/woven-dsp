package com.info.baymax.dsp.job.sch;

import com.info.baymax.common.comp.profile.EnableExtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.elasticsearch.ElasticSearchRestHealthIndicatorAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: haijun
 * @Date: 2019/12/12 14:17
 */
@SpringCloudApplication
@EnableFeignClients(basePackages = {"com.info.baymax.dsp.job.sch.client"})
@ComponentScan(basePackages = {"com.info.baymax"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@EnableExtProperties({"classpath:dsp-common.properties", "classpath:dsp-job-schedule.properties",
    "classpath:quartz.properties"})
@EnableAutoConfiguration(exclude = {ElasticSearchRestHealthIndicatorAutoConfiguration.class,
    DataSourceAutoConfiguration.class})
public class SchedulerStarter {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerStarter.class, args);
    }

}