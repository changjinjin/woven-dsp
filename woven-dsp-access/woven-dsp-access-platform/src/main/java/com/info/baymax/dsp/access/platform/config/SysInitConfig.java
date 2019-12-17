package com.info.baymax.dsp.access.platform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.info.baymax.dsp.data.sys.initialize.InitConfig;

@Configuration
@ConfigurationProperties(prefix = "security.init")
public class SysInitConfig extends InitConfig {
}
