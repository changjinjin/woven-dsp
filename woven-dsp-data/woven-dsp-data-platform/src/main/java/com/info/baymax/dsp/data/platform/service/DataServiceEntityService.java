package com.info.baymax.dsp.data.platform.service;

import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.jpa.criteria.QueryObjectCriteriaService;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataServiceEntity;
import com.merce.woven.common.jpa.criteria.query.QueryObject;
import com.merce.woven.common.jpa.page.Page;
import com.merce.woven.data.entity.core.Dataset;

import java.util.List;

/**
 * @Author: guofeng.wu
 * @Date: 2019/12/18
 */
public interface DataServiceEntityService extends BaseEntityService<DataServiceEntity> {
    Integer createDataService(DataResource dataResource);

    void updateDataService(DataResource dataResource);

    void deleteDataService(List<Long> ids);
}
