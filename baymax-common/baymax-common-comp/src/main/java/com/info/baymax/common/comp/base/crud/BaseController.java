package com.info.baymax.common.comp.base.crud;

import com.info.baymax.common.entity.gene.Idable;
import com.info.baymax.common.service.BaseIdableAndExampleQueryService;

import java.io.Serializable;

public interface BaseController<ID extends Serializable, T extends Idable<ID>> {
    BaseIdableAndExampleQueryService<ID, T> getBaseIdableAndExampleQueryService();
}
