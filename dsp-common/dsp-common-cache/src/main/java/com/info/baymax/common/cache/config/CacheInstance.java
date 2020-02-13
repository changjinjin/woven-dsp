package com.info.baymax.common.cache.config;

public class CacheInstance {
    private String cacheName;
    private long expiration = 0L;

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

}
