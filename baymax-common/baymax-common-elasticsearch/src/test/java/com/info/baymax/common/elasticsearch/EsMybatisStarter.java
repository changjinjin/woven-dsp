package com.info.baymax.common.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.info.baymax", "com.info.baymax.common.elasticsearch"})
@EntityScan(basePackages = {"com.info.baymax.common.elasticsearch.**.entity"})
@MapperScan(basePackages = "com.info.baymax.common.elasticsearch.**.mapper")
public class EsMybatisStarter {

	public static void main(String[] args) {
		log.info("JAVA CLASS PATH = " + System.getProperty("java.class.path"));
		SpringApplication.run(EsMybatisStarter.class, args);
	}
}
