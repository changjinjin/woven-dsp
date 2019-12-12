package com.merce.woven.dsp.auth.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.merce.woven.dsp.data.sys.initialize.InitConfig;

@Configuration
@ConfigurationProperties(prefix = SecurityInitConfig.PREFIX)
public class SecurityInitConfig extends InitConfig {
	public static final String PREFIX = "security.init";
}
