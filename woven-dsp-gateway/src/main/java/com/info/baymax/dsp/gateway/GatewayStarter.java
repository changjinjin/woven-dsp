package com.info.baymax.dsp.gateway;

import com.info.baymax.dsp.gateway.config.resource.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@SpringCloudApplication
@EnableZuulProxy
@EnableOAuth2Sso
@ComponentScan(basePackages = {"com.info.baymax"})
@EnableAutoConfiguration
@PropertySource(value = {"classpath:/dsp-gateway.yml"}, factory = YamlPropertySourceFactory.class)
public class GatewayStarter {
    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayStarter.class, args);
    }
}
