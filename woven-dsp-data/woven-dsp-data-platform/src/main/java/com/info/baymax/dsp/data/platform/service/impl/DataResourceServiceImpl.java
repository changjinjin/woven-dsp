package com.info.baymax.dsp.data.platform.service.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.mybatis.mapper.DataResourceMapper;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/13 19:10
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class DataResourceServiceImpl extends EntityClassServiceImpl<DataResource> implements DataResourceService {
    @Autowired
    DataResourceMapper resourceMapper;

    @Autowired
    DatasetService datasetService;

    @Override
    public MyIdableMapper<DataResource> getMyIdableMapper() {
        return resourceMapper;
    }

    @Override
    public Integer createDataResource(DataResource dataResource) {
        return resourceMapper.insert(dataResource);
    }

    @Override
    public DataResource getDataResource(Long id) {
        return resourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateDataResource(DataResource dataResource) {
        resourceMapper.updateByPrimaryKey(dataResource);
    }

    @Override
    public void closeDataResource(List<Long> ids){
        resourceMapper.closeDataResourceByIds(ids);
    }

    public IPage<Dataset> queryDatasets(ExampleQuery exampleQuery) {
        return datasetService.selectPage(exampleQuery);
    }
}
