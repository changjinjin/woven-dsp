package com.info.baymax.dsp.job.exec;

import com.info.baymax.common.comp.config.profile.EnableExtProperties;
import com.info.baymax.dsp.job.exec.reader.CommonReader;
import com.info.baymax.dsp.job.exec.writer.CommonWriter;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import java.lang.reflect.Modifier;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: haijun
 * @Date: 2019/12/19 14:29
 */
@SpringCloudApplication
@EnableFeignClients(basePackages = {"com.info.baymax.dsp.job"})
@ComponentScan(basePackages = {"com.info.baymax"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@EnableExtProperties("classpath*:**/job-exec*.properties")
@Slf4j
public class ExecutorServer {

    public static void main(String[] args) {
        SpringApplication.run(ExecutorServer.class, args);
    }

}
