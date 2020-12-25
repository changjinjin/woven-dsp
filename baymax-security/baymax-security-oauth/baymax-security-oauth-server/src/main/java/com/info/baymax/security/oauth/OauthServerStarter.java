package com.info.baymax.security.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import tk.mybatis.spring.annotation.MapperScan;

@SpringCloudApplication
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@ComponentScan(basePackages = {"com.info.baymax"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity"})
@MapperScan(basePackages = "com.info.baymax.dsp.data.**.mapper")
@PropertySource(value = {"classpath:/dsp-common.properties", "classpath:/dsp-auth-server.properties"})
public class OauthServerStarter {
    public static void main(String[] args) {
        SpringApplication.run(OauthServerStarter.class, args);
    }
}
