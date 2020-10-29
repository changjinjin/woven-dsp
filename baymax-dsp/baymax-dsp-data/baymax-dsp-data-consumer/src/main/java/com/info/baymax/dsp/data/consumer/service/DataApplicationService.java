package com.info.baymax.dsp.data.consumer.service;

import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;

/**
 * @Author: haijun
 * @Date: 2019/12/13 19:09
 */
public interface DataApplicationService extends BaseEntityService<DataApplication> {

    void updateDataApplicationStatus(Long id, Integer status);
}
