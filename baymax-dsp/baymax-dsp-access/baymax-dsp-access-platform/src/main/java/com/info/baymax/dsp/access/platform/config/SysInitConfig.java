package com.info.baymax.dsp.access.platform.config;

import com.info.baymax.dsp.data.sys.initialize.InitConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.init")
public class SysInitConfig extends InitConfig {
}
