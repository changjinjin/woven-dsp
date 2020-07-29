package com.info.baymax.dsp.data.consumer.mybatis.mapper;

import com.info.baymax.common.mybatis.cache.RoutingCache;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import org.apache.ibatis.annotations.*;

/**
 * @Author: haijun
 * @Date: 2019/12/16 10:52
 */
@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 1000, properties = {
		@Property(name = "cacheType", value = "${cacheType}") })
public interface DataApplicationMapper extends MyIdableMapper<DataApplication> {
}
