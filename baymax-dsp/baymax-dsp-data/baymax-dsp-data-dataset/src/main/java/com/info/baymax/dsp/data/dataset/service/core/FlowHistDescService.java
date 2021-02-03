package com.info.baymax.dsp.data.dataset.service.core;


import com.info.baymax.common.persistence.entity.base.BaseMaintableService;
import com.info.baymax.dsp.data.dataset.entity.core.FlowHistDesc;

import java.util.List;

public interface FlowHistDescService extends BaseMaintableService<FlowHistDesc> {

	List<FlowHistDesc> findByOId(String oid);

	FlowHistDesc findByOIdAndVersion(String oid, int version);

	int deleteByOId(String tenantId, String owner, String oid);

	int deleteByOIds(String tenantId, String owner, String[] oid);

	Integer findMaxVersionByFlowId(String flowId);

	FlowHistDesc findAllByOwnerIdAndNameAndVersion(String ownerId, Integer version, String name);

	FlowHistDesc findAllByNameAndVersion(Integer version, String name);

}
