package com.info.baymax.security.cas.reactive.client.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({CasBeanConfig.class, CasSecurityConfig.class})
@EnableConfigurationProperties(CasServiceProperties.class)
@ComponentScan("com.info.baymax.security.cas.reactive.client")
public class CasClientWebfluxAutoConfiguration {
}
