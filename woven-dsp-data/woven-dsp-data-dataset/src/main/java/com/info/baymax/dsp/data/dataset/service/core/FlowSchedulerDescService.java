package com.info.baymax.dsp.data.dataset.service.core;


import com.info.baymax.common.jpa.criteria.QueryObjectCriteriaService;
import com.info.baymax.dsp.data.dataset.entity.core.FlowSchedulerDesc;
import com.info.baymax.dsp.data.dataset.service.BaseMaintableService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FlowSchedulerDescService extends BaseMaintableService<FlowSchedulerDesc>, QueryObjectCriteriaService<FlowSchedulerDesc> {

	List<FlowSchedulerDesc> findAllToLaunch();

	List<Map<String, Object>> findGroupByDay(String tenantId, String owner, String flowType, Date startTime,
                                             Date endTime);
}
