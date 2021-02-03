package com.info.baymax.common.comp.base;

import com.info.baymax.common.persistence.entity.base.CommonEntity;
import com.info.baymax.common.persistence.entity.base.CommonEntityService;
import com.info.baymax.common.persistence.service.BaseIdableAndExampleQueryService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@Deprecated
public interface CommonEntityController<ID extends Serializable, T extends CommonEntity<ID>>
    extends BaseIdableAndExampleQueryController<ID, T> {

    CommonEntityService<ID, T> getCommonEntityService();

    @Override
    default BaseIdableAndExampleQueryService<ID, T> getBaseIdableAndExampleQueryService() {
        return getCommonEntityService();
    }

}
