package com.info.baymax.security.oauth.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.info.baymax.dsp.data.sys.initialize.InitConfig;

@Order(0)
@Configuration
@ConfigurationProperties(prefix = SecurityInitProperties.PREFIX)
public class SecurityInitProperties extends InitConfig {
    public static final String PREFIX = "security.init";
}
