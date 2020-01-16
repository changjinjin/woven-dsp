package com.info.baymax.dsp.gateway;

import com.info.baymax.dsp.gateway.config.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringCloudApplication
@EnableSwagger2WebFlux
@ComponentScan(basePackages = {"com.info.baymax"})
@EnableAutoConfiguration
@PropertySource(value = {"classpath:/dsp-gateway.yml"}, factory = YamlPropertySourceFactory.class)
public class Oauth2ClientStarter {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2ClientStarter.class, args);
    }
}
