package com.merce.woven.cas.client.reactive.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({CasBeanConfig.class, CasSecurityConfig.class})
@EnableConfigurationProperties(CasServiceProperties.class)
@ComponentScan("com.merce.woven.cas.client.reactive")
public class CasClientWebfluxAutoConfiguration {
}
