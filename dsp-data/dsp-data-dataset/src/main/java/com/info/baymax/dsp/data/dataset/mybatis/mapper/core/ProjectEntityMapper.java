package com.info.baymax.dsp.data.dataset.mybatis.mapper.core;

import com.info.baymax.common.mybatis.cache.RoutingCache;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.dataset.entity.core.ProjectEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Property;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 1000, properties = {
		@Property(name = "cacheType", value = "${cacheType}") })
public interface ProjectEntityMapper extends MyIdableMapper<ProjectEntity> {

}
