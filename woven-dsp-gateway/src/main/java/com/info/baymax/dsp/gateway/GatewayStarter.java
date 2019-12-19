package com.info.baymax.dsp.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@SpringCloudApplication
@EnableZuulProxy
@EnableOAuth2Sso
@ComponentScan(basePackages = {"com.info.baymax"})
@EnableAutoConfiguration
public class GatewayStarter {
    public static void main(String[] args) {
        SpringApplication.run(GatewayStarter.class, args);
    }
}
