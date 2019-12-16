package com.info.baymax.dsp.auth.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.info.baymax.dsp.data.sys.initialize.InitConfig;

@Order(0)
@Configuration
@ConfigurationProperties(prefix = SecurityInitConfig.PREFIX)
public class SecurityInitConfig extends InitConfig {
    public static final String PREFIX = "security.init";
}
