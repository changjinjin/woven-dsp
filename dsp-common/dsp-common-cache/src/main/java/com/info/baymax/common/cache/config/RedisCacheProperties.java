package com.info.baymax.common.cache.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = RedisCacheProperties.PREFIX)
public class RedisCacheProperties {
    public static final String PREFIX = "spring.cache";

    /**
     * 缓存列表
     */
    private List<CacheInstance> instances;

    public List<CacheInstance> getInstances() {
        return instances;
    }

    public void setInstances(List<CacheInstance> instances) {
        this.instances = instances;
    }

}
