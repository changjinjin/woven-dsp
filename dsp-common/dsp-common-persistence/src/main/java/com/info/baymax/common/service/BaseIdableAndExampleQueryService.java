package com.info.baymax.common.service;

import com.info.baymax.common.entity.id.Idable;
import com.info.baymax.common.mybatis.mapper.base.BaseExampleMapper;
import com.info.baymax.common.service.criteria.ExampleQueryService;

import java.io.Serializable;

public interface BaseIdableAndExampleQueryService<ID extends Serializable, T extends Idable<ID>>
    extends ExampleQueryService<T>, BaseIdableService<ID, T> {

    @Override
    default BaseExampleMapper<T> getBaseExampleMapper() {
        return getMyIdableMapper();
    }

}
