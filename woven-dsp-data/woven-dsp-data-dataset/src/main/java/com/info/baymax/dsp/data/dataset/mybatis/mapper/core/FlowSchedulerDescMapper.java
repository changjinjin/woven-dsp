package com.info.baymax.dsp.data.dataset.mybatis.mapper.core;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.dataset.entity.core.FlowSchedulerDesc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface FlowSchedulerDescMapper extends MyIdableMapper<FlowSchedulerDesc> {

	@Select("select t.day as name, count(t.id) as count,'day' as filed from merce_flow_schedule t where t.tenant_id = #{tenantId} and t.owner like #{owner} and t.flow_type like #{flowType} and t.create_time between #{startTime} and #{endTime} group by t.day")
	List<Map<String, Object>> findGroupByDay(@Param("tenantId") String tenantId, @Param("owner") String owner, @Param("flowType") String flowType, @Param("startTime") Date startTime,
                                             @Param("endTime") Date endTime);
}
