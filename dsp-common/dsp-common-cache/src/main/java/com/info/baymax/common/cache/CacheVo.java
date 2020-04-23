package com.info.baymax.common.cache;

import java.io.Serializable;

import lombok.Data;

@Data
public class CacheVo implements Serializable {
    private static final long serialVersionUID = -3007790670312954774L;

    private String cacheName;
    private String alias;
    private String paramKey;
    private String paramValue;

    public CacheVo() {
    }

    public CacheVo(String cacheName, String alias) {
        this.cacheName = cacheName;
        this.alias = alias;
    }

    public CacheVo(String cacheName, String paramKey, String paramValue) {
        this.cacheName = cacheName;
        this.paramKey = paramKey;
        this.paramValue = paramValue;
    }

    public CacheVo(String cacheName, String alias, String paramKey, String paramValue) {
        this.cacheName = cacheName;
        this.alias = alias;
        this.paramKey = paramKey;
        this.paramValue = paramValue;
    }
}
