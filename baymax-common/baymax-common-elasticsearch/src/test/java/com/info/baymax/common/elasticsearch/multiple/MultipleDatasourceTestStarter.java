package com.info.baymax.common.elasticsearch.multiple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.info.baymax.common.elasticsearch.multiple"})
@EntityScan(basePackages = {"com.info.baymax.common.elasticsearch.multiple.entity.mysql"})
@PropertySource(value = "classpath:/application-multiple.properties")
public class MultipleDatasourceTestStarter {

    public static void main(String[] args) {
        log.info("JAVA CLASS PATH = " + System.getProperty("java.class.path"));
        SpringApplication.run(MultipleDatasourceTestStarter.class, args);
    }
}
