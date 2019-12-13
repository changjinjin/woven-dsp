package com.info.baymax.dsp.auth.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.info.baymax.dsp.data.sys.initialize.InitConfig;

@Configuration
@ConfigurationProperties(prefix = SecurityInitConfig.PREFIX)
public class SecurityInitConfig extends InitConfig {
	public static final String PREFIX = "security.init";
}
