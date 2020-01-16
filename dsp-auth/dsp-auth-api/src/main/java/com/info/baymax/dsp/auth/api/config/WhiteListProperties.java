package com.info.baymax.dsp.auth.api.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = WhiteListProperties.PREFIX)
public class WhiteListProperties {
    public static final String PREFIX = "security.token.ignored";

    /**
     * 忽略的静态资源请求
     */
    private String[] staticResources;

    /**
     * 所有方法都忽略的请求
     */
    private String[] anyMethods;

    public String[] getAllWhiteList() {
        return ArrayUtils.addAll(staticResources, anyMethods);
    }
}
