package com.info.baymax.common.web.base;

import com.info.baymax.common.persistence.entity.base.BaseEntity;
import com.info.baymax.common.persistence.entity.base.BaseEntityService;
import com.info.baymax.common.persistence.entity.base.CommonEntityService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface BaseEntityController<T extends BaseEntity> extends CommonEntityController<Long, T> {

    BaseEntityService<T> getBaseEntityService();

    @Override
    default CommonEntityService<Long, T> getCommonEntityService() {
        return getBaseEntityService();
    }
}
