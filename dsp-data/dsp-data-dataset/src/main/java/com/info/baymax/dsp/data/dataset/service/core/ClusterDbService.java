package com.info.baymax.dsp.data.dataset.service.core;


import com.info.baymax.common.entity.base.BaseMaintableService;
import com.info.baymax.common.jpa.criteria.QueryObjectCriteriaService;
import com.info.baymax.dsp.data.dataset.entity.core.ClusterEntity;

import java.util.List;

/**
 * create by pengchuan.chen on 2019/11/27
 */
public interface ClusterDbService extends BaseMaintableService<ClusterEntity>, QueryObjectCriteriaService<ClusterEntity> {

  ClusterEntity findOneByName(String tenantId, String name, boolean includeConfFile);

  List<ClusterEntity> findAllEnabledCluster(String tenantId);
}
