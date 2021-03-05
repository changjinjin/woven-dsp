package com.info.baymax.dsp.job.exec;

import com.merce.woven.metrics.config.EnableElasticMetricsExport;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: haijun
 * @Date: 2019/12/19 14:29
 */
@SpringBootApplication(proxyBeanMethods = false)
@EnableDiscoveryClient
@EnableElasticMetricsExport
@EnableFeignClients(basePackages = {"com.info.baymax.dsp.job.exec"})
@ComponentScan(basePackages = {"com.info.baymax"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@PropertySource({"classpath:dsp-common.properties", "classpath:dsp-job-exec.properties"})
@EnableAsync
public class DspExecutorStarter {

    public static void main(String[] args) {
        new SpringApplicationBuilder().bannerMode(Banner.Mode.OFF).properties().sources(DspExecutorStarter.class)
            .web(WebApplicationType.REACTIVE).run(args);
    }

}
