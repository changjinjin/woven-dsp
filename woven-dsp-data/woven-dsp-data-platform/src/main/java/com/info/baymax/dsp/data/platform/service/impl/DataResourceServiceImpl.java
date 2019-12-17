package com.info.baymax.dsp.data.platform.service.impl;

import com.info.baymax.common.jpa.criteria.query.QueryObject;
import com.info.baymax.common.jpa.page.Page;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.mybatis.mapper.example.Example;
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
    public void updateDataResource(DataResource dataResource) {
        resourceMapper.updateByExample(dataResource, Example.builder(DataResource.class).build());
    }

    @Override
    public void deleteDataResource(List<Long> ids) {
        resourceMapper.deleteByPrimaryKeys(ids);
    }

    @Override
    public void closeDataResource(List<Long> ids){
        resourceMapper.closeDataResourceByIds(ids);
    }

    @Override
    public Page<Dataset> queryDatasets(QueryObject queryObject) {
        return datasetService.findObjectPage(queryObject);
    }
}
