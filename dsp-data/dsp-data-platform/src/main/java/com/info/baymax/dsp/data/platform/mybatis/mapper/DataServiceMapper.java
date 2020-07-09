package com.info.baymax.dsp.data.platform.mybatis.mapper;

import com.info.baymax.common.mybatis.cache.RoutingCache;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.platform.entity.DataService;
import org.apache.ibatis.annotations.*;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 1000, properties = {
    @Property(name = "cacheType", value = "${cacheType}")})
public interface DataServiceMapper extends MyIdableMapper<DataService> {
}
