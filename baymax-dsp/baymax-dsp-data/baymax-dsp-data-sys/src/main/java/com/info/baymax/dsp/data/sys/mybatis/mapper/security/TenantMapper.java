package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import com.info.baymax.common.persistence.mybatis.cache.RoutingCache;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Property;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 50,properties = {@Property(name = "cacheType",value = "${cacheType}")})
public interface TenantMapper extends MyIdableMapper<Tenant> {
}
