package com.info.baymax.dsp.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
import tk.mybatis.spring.annotation.MapperScan;

//@EnableCaching
@SpringCloudApplication
@EnableSwagger2WebMvc
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@ComponentScan(basePackages = {"com.info.baymax"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@PropertySource(value = {"classpath:/dsp-common.properties", "classpath:/dsp-auth-server.properties"})
public class AuthStarter {
    public static void main(String[] args) {
        SpringApplication.run(AuthStarter.class, args);
    }
}
