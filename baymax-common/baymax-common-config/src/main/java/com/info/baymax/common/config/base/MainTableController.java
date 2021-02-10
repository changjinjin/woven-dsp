package com.info.baymax.common.config.base;

import com.info.baymax.common.persistence.entity.base.BaseMaintableService;
import com.info.baymax.common.persistence.entity.base.CommonEntityService;
import com.info.baymax.common.persistence.entity.base.Maintable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface MainTableController<T extends Maintable> extends CommonEntityController<String, T> {

    BaseMaintableService<T> getBaseMaintableService();

    @Override
    default CommonEntityService<String, T> getCommonEntityService() {
        return getBaseMaintableService();
    }
}
