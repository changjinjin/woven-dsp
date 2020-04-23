package com.info.baymax.dsp.job.exec;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * @Author: haijun
 * @Date: 2019/12/19 14:29
 */
@SpringCloudApplication
@EnableAutoConfiguration
@EnableFeignClients(basePackages = {"com.info.baymax.dsp.job.exec"})
@ComponentScan(basePackages = {"com.info.baymax"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@PropertySource({ "classpath:dsp-common.properties", "classpath:dsp-job-exec.properties" })
public class DspExecutorStarter {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .properties()
                .sources(DspExecutorStarter.class)
                .web(WebApplicationType.REACTIVE)
                .run(args);
    }

}
