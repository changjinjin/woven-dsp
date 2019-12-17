package com.info.baymax.dsp.data.platform.service;

import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.jpa.criteria.QueryObjectCriteriaService;
import com.info.baymax.common.jpa.criteria.query.QueryObject;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.common.jpa.page.Page;

import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/13 19:09
 */
public interface DataResourceService extends BaseEntityService<DataResource>, QueryObjectCriteriaService<DataResource> {
    Integer createDataResource(DataResource dataResource);
    void updateDataResource(DataResource dataResource);
    void deleteDataResource(List<Long> ids);
    void closeDataResource(List<Long> ids);
    Page<Dataset> queryDatasets(QueryObject queryObject);
}
