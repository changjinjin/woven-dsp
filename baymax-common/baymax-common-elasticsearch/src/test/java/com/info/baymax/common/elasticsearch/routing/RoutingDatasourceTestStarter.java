package com.info.baymax.common.elasticsearch.routing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import tk.mybatis.spring.annotation.MapperScan;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.info.baymax"})
@EntityScan(basePackages = {"com.info.baymax.common.elasticsearch.routing.entity"})
@MapperScan(basePackages = {"com.info.baymax.common.elasticsearch.routing.mybatis.mapper"})
@PropertySource(value = "classpath:/application-routing.properties")
public class RoutingDatasourceTestStarter {

	public static void main(String[] args) {
		log.info("JAVA CLASS PATH = " + System.getProperty("java.class.path"));
		SpringApplication.run(RoutingDatasourceTestStarter.class, args);
	}
}
