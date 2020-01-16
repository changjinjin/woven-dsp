package com.info.baymax.dsp.job.exec;

import com.info.baymax.common.comp.profile.EnableExtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.elasticsearch.ElasticSearchRestHealthIndicatorAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * @Author: haijun
 * @Date: 2019/12/19 14:29
 */
@SpringCloudApplication
@EnableFeignClients(basePackages = {"com.info.baymax.dsp.job.exec"})
@ComponentScan(basePackages = {"com.info.baymax"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@EnableExtProperties({"classpath:dsp-common.properties", "classpath:dsp-job-exec.properties"})
@EnableAutoConfiguration(exclude = {ElasticSearchRestHealthIndicatorAutoConfiguration.class})
public class ExecutorStarter {

    public static void main(String[] args) {
        SpringApplication.run(ExecutorStarter.class, args);
    }

}
