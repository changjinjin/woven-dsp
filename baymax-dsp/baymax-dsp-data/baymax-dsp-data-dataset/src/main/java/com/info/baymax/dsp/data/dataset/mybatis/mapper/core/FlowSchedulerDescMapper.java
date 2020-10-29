package com.info.baymax.dsp.data.dataset.mybatis.mapper.core;

import com.info.baymax.common.mybatis.cache.RoutingCache;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.dataset.entity.core.FlowSchedulerDesc;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 1000, properties = {
		@Property(name = "cacheType", value = "${cacheType}") })
public interface FlowSchedulerDescMapper extends MyIdableMapper<FlowSchedulerDesc> {

	@Select("select t.day as name, count(t.id) as count,'day' as filed from merce_flow_schedule t where t.tenant_id = #{tenantId} and t.owner like #{owner} and t.flow_type like #{flowType} and t.create_time between #{startTime} and #{endTime} group by t.day")
	List<Map<String, Object>> findGroupByDay(@Param("tenantId") String tenantId, @Param("owner") String owner, @Param("flowType") String flowType, @Param("startTime") Date startTime,
                                             @Param("endTime") Date endTime);
}
