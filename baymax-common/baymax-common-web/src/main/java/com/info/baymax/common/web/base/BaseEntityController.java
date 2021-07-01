package com.info.baymax.common.web.base;

import com.info.baymax.common.persistence.entity.base.BaseCommonEntityService;
import com.info.baymax.common.persistence.entity.base.BaseEntity;
import com.info.baymax.common.persistence.entity.base.BaseEntityService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface BaseEntityController<T extends BaseEntity> extends CommonEntityController<Long, T> {

    BaseEntityService<T> getBaseEntityService();

    @Override
    default BaseCommonEntityService<Long, T> getBaseCommonEntityService() {
        return getBaseEntityService();
    }
}
