package com.info.baymax.dsp.data.platform.service;

import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.persistence.entity.base.BaseEntityService;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.platform.entity.DataResource;

import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/13 19:09
 */
public interface DataResourceService extends BaseEntityService<DataResource> {

    void closeDataResource(List<Long> ids);

    IPage<Dataset> queryDatasets(ExampleQuery exampleQuery);

    List<DataResource> selectDataResourceListByIds(List<Long> ids);

    DataResource selectEntityByName(String name);
}
