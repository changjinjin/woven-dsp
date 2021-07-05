package com.info.baymax.common.web.base;

import com.info.baymax.common.persistence.entity.base.BaseCommonEntityService;
import com.info.baymax.common.persistence.entity.base.CommonEntity;
import com.info.baymax.common.persistence.service.BaseIdableAndExampleQueryService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@Deprecated
public interface CommonEntityController<ID extends Serializable, T extends CommonEntity<ID>>
    extends BaseIdableAndExampleQueryController<ID, T> {

    BaseCommonEntityService<ID, T> getBaseCommonEntityService();

    @Override
    default BaseIdableAndExampleQueryService<ID, T> getBaseIdableAndExampleQueryService() {
        return getBaseCommonEntityService();
    }

}
