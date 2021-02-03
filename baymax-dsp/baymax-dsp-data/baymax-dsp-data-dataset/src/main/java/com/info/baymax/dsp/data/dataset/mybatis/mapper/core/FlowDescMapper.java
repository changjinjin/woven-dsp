package com.info.baymax.dsp.data.dataset.mybatis.mapper.core;

import com.info.baymax.common.persistence.mybatis.cache.RoutingCache;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.dataset.entity.core.FlowDesc;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Property;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 1000, properties = {
		@Property(name = "cacheType", value = "${cacheType}") })
public interface FlowDescMapper extends MyIdableMapper<FlowDesc> {

	@Select("SELECT * FROM merce_flow d  LEFT JOIN merce_data_resource_ref t ON t.instance_id =d.id WHERE t.id IS NULL and d.source != 'system'")
	List<FlowDesc> findFlowByNotResoure();
}
