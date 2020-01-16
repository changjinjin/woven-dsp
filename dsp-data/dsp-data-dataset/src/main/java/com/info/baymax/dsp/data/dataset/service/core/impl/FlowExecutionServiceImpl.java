package com.info.baymax.dsp.data.dataset.service.core.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.dataset.entity.core.FlowExecution;
import com.info.baymax.dsp.data.dataset.mybatis.mapper.core.FlowExecutionMapper;
import com.info.baymax.dsp.data.dataset.service.core.FlowExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FlowExecutionServiceImpl extends EntityClassServiceImpl<FlowExecution> implements FlowExecutionService {

	@Autowired
	private FlowExecutionMapper flowExecutionMapper;

	@Override
	public MyIdableMapper<FlowExecution> getMyIdableMapper() {
		return flowExecutionMapper;
	}

	@Override
	public FlowExecution findOneByJobId(String jobId) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo("jobId", jobId)//
				.end()//
		);
	}

	@Override
	public List<FlowExecution> findByFlowSchedulerId(String flowSchedulerId) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo("flowSchedulerId", flowSchedulerId)//
				.end()//
				.orderByDesc("lastModifiedTime")//
		);
	}

	@Override
	public List<FlowExecution> findByFlowId(String flowId) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo("flowId", flowId)//
				.end()//
				.orderByDesc("lastModifiedTime")//
		);
	}

	@Override
	public List<Map<String, Object>> findGroupByHour(String tenantId, String owner, String flowType, Date startTime,
			Date endTime) {
		return flowExecutionMapper.findGroupByHour(tenantId, owner, flowType, startTime, endTime);
	}

	@Override
	public List<FlowExecution> findAllRunningExecutions() {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andIn("statusType", new String[] { "RUNNING", "READY" })//
				.end()//
				.orderByDesc("lastModifiedTime")//
		);
	}

	@Override
	public List<FlowExecution> findAllByStatusType(String statusType) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo("statusType", statusType)//
				.end()//
				.orderByDesc("lastModifiedTime")//
		);
	}

	@Override
	public int countByStatusType(String statusType) {
		return selectCount(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo("statusType", statusType)//
				.end()//
		);
	}
}
