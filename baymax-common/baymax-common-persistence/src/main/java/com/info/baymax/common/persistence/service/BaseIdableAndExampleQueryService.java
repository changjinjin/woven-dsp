package com.info.baymax.common.persistence.service;

import com.info.baymax.common.persistence.entity.gene.Idable;
import com.info.baymax.common.persistence.mybatis.mapper.base.BaseExampleMapper;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQueryService;

import java.io.Serializable;

public interface BaseIdableAndExampleQueryService<ID extends Serializable, T extends Idable<ID>>
    extends ExampleQueryService<T>, BaseIdableService<ID, T> {

    @Override
    default BaseExampleMapper<T> getBaseExampleMapper() {
        return getMyIdableMapper();
    }

}
