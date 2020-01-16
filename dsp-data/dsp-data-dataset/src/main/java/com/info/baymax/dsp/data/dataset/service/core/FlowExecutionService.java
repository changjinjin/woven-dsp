package com.info.baymax.dsp.data.dataset.service.core;

import com.info.baymax.common.entity.base.BaseMaintableService;
import com.info.baymax.common.jpa.criteria.QueryObjectCriteriaService;
import com.info.baymax.dsp.data.dataset.entity.core.FlowExecution;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FlowExecutionService extends BaseMaintableService<FlowExecution>, QueryObjectCriteriaService<FlowExecution> {

	FlowExecution findOneByJobId(String jobId);

	List<FlowExecution> findByFlowSchedulerId(String flowSchedulerId);

	List<FlowExecution> findByFlowId(String flowId);

	List<Map<String, Object>> findGroupByHour(String tenantId, String owner, String flowType, Date startTime,
                                              Date endTime);

	List<FlowExecution> findAllRunningExecutions();

	List<FlowExecution> findAllByStatusType(String statusType);

	int countByStatusType(String statusType);
}
