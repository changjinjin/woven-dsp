package com.info.baymax.dsp.data.dataset.service.core;

import com.info.baymax.common.persistence.entity.base.BaseMaintableService;
import com.info.baymax.common.persistence.jpa.criteria.QueryObjectCriteriaService;
import com.info.baymax.dsp.data.dataset.entity.core.ProcessConfig;

import java.util.List;

public interface ProcessConfigService extends BaseMaintableService<ProcessConfig> ,QueryObjectCriteriaService<ProcessConfig>{

	List<ProcessConfig> findByProcessConfigType(String configType);

	List<ProcessConfig> findByProcessConfigType(String tenantId, String configType);

	ProcessConfig findOneByName(String tenantId, String name);
}
