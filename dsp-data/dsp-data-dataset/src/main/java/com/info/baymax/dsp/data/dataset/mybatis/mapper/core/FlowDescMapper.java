package com.info.baymax.dsp.data.dataset.mybatis.mapper.core;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.dataset.entity.core.FlowDesc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FlowDescMapper extends MyIdableMapper<FlowDesc> {

	@Select("SELECT * FROM merce_flow d  LEFT JOIN merce_data_resource_ref t ON t.instance_id =d.id WHERE t.id IS NULL and d.source != 'system'")
	List<FlowDesc> findFlowByNotResoure();
}
