package com.info.baymax.dsp.job.sch;


import com.info.baymax.common.comp.config.profile.EnableExtProperties;
import com.info.baymax.dsp.job.sch.scheduler.AbstractScheduler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.elasticsearch.ElasticSearchRestHealthIndicatorAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: haijun
 * @Date: 2019/12/12 14:17
 */
@SpringCloudApplication
@EnableFeignClients
@ComponentScan(basePackages = {"com.info.baymax"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@EnableExtProperties({"classpath*:**/job-schedule*.properties", "classpath*:**/quartz.properties"})
@EnableAutoConfiguration(exclude = {ElasticSearchRestHealthIndicatorAutoConfiguration.class})
public class SchedulerServer {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerServer.class, args);
    }

}
