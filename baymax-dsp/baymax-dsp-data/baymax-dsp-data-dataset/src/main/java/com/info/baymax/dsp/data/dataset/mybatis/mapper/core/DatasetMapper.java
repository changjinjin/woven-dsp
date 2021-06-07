package com.info.baymax.dsp.data.dataset.mybatis.mapper.core;

import com.info.baymax.common.persistence.mybatis.cache.RoutingCache;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import org.apache.ibatis.annotations.*;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 1000, properties = {
		@Property(name = "cacheType", value = "${cacheType}") })
public interface DatasetMapper extends MyIdableMapper<Dataset> {

	@Options(useCache = false)
	@Select("select * from merce_dataset t where t.name = #{name,jdbcType=VARCHAR}")
	Dataset selectEntityByName(@Param("name") String name);
}
