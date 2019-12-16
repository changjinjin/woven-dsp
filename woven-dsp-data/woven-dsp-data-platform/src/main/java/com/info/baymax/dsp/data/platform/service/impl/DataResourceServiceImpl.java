package com.info.baymax.dsp.data.platform.service.impl;

import com.info.baymax.common.mybatis.mapper.example.Example;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.mybatis.mapper.DataResourceMapper;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/13 19:10
 */
@Service
public class DataResourceServiceImpl implements DataResourceService {
    @Autowired
    DataResourceMapper resourceMapper;

    @Override
    public Integer createDataResource(DataResource dataResource) {
        return resourceMapper.insert(dataResource);
    }

    @Override
    public void updateDataResource(DataResource dataResource) {
        resourceMapper.updateByExample(dataResource, Example.builder(DataResource.class).build());
    }

    @Override
    public void deleteDataResource(List<Integer> ids) {
        resourceMapper.deleteByPrimaryKeys(ids);
    }
}
