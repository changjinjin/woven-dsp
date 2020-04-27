package com.info.baymax.dsp.data.dataset.mybatis.mapper.core;

import com.info.baymax.common.mybatis.cache.RoutingCache;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.dataset.entity.core.FlowHistDesc;
import org.apache.ibatis.annotations.*;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 1000, properties = {
		@Property(name = "cacheType", value = "${cacheType}") })
public interface FlowHistDescMapper extends MyIdableMapper<FlowHistDesc> {

	@Select("select MAX(t.version) from merce_flow_history t where t.o_id = #{flowId} ")
	Integer findMaxVersionByFlowId(@Param("flowId") String flowId);
}
