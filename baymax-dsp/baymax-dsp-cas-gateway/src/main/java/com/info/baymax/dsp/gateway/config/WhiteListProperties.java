package com.info.baymax.dsp.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.token.ignored")
public class WhiteListProperties {

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
