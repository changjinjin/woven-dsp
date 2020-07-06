package com.info.baymax.dsp.data.dataset.service.core.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.dataset.entity.core.FlowSchedulerDesc;
import com.info.baymax.dsp.data.dataset.mybatis.mapper.core.FlowSchedulerDescMapper;
import com.info.baymax.dsp.data.dataset.service.core.FlowSchedulerDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FlowSchedulerDescServiceImpl extends EntityClassServiceImpl<FlowSchedulerDesc>
		implements FlowSchedulerDescService {

	@Autowired
	private FlowSchedulerDescMapper flowSchedulerDescMapper;

	@Override
	public MyIdableMapper<FlowSchedulerDesc> getMyIdableMapper() {
		return flowSchedulerDescMapper;
	}

	@Override
	public List<FlowSchedulerDesc> findAllToLaunch() {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().group(FieldGroup.builder()//
						.andEqualTo("schedulerId", "cron").orEqualTo("totalExecuted", 0)).andEqualTo("enabled", 1))//
		);
	}

	@Override
	public List<Map<String, Object>> findGroupByDay(String tenantId, String owner, String flowType, Date startTime,
			Date endTime) {
		return flowSchedulerDescMapper.findGroupByDay(tenantId, owner, flowType, startTime, endTime);
	}

}
