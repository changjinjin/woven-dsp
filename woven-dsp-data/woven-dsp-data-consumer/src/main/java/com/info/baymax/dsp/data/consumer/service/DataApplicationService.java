package com.info.baymax.dsp.data.consumer.service;

import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.jpa.criteria.QueryObjectCriteriaService;
import com.info.baymax.dsp.data.consumer.entity.Customer;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;

import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/13 19:09
 */
public interface DataApplicationService extends BaseEntityService<DataApplication>, QueryObjectCriteriaService<DataApplication> {
    Long createDataApplication(DataApplication dataApplication);
    void updateDataApplication(DataApplication dataApplication);
    void deleteDataApplication(List<Long> ids);
    void deleteByDataResIds(List<Long> ids);
}
