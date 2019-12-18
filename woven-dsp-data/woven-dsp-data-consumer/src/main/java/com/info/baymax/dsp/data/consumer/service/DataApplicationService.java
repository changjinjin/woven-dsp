package com.info.baymax.dsp.data.consumer.service;

import java.util.List;

import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.jpa.criteria.QueryObjectCriteriaService;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;

/**
 * @Author: haijun
 * @Date: 2019/12/13 19:09
 */
public interface DataApplicationService extends BaseEntityService<DataApplication>, QueryObjectCriteriaService<DataApplication> {
    Long createDataApplication(DataApplication dataApplication);
    void updateDataApplication(DataApplication dataApplication);
    void deleteByDataResIds(Long tenantId, List<Long> ids);
}
