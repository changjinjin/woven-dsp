package com.info.baymax.common.comp.base.crud;

import com.info.baymax.common.persistence.entity.gene.Idable;
import com.info.baymax.common.persistence.service.BaseIdableAndExampleQueryService;

import java.io.Serializable;

public interface BaseController<ID extends Serializable, T extends Idable<ID>> {
    BaseIdableAndExampleQueryService<ID, T> getBaseIdableAndExampleQueryService();
}
