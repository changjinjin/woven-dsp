package com.info.baymax.dsp.data.consumer.service;

import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.jpa.criteria.QueryObjectCriteriaService;
import com.info.baymax.dsp.data.consumer.entity.Consumer;

public interface ConsumerService extends BaseEntityService<Consumer>, QueryObjectCriteriaService<Consumer> {
}
