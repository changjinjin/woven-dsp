package com.info.baymax.dsp.data.platform.service.impl;

import com.info.baymax.common.enums.types.YesNoType;
import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.mybatis.mapper.DataResourceMapper;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
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
    private DataResourceMapper resourceMapper;

    @Autowired
    private DatasetService datasetService;

    @Override
    public MyIdableMapper<DataResource> getMyIdableMapper() {
        return resourceMapper;
    }

    @Override
    public DataResource save(DataResource t) {
        if (t == null) {
            throw new ServiceException(ErrType.BAD_REQUEST, "请求数据不能为空。");
        }

        // 检查数据集数据是否存在并修改数据集的关联状态
        Dataset dataset = datasetService.selectByPrimaryKey(t.getDatasetId());
        if (dataset == null) {
            throw new ServiceException(ErrType.ENTITY_NOT_EXIST, "绑定的数据集记录不存在或者已经过期。");
        }
        dataset.setIsRelated(YesNoType.YES.getValue());
        datasetService.updateByPrimaryKey(dataset);

        return DataResourceService.super.save(t);
    }

    @Override
    public void closeDataResource(List<Long> ids) {
        resourceMapper.closeDataResourceByIds(ids);
    }

    public IPage<Dataset> queryDatasets(ExampleQuery exampleQuery) {
        return datasetService.selectPage(exampleQuery);
    }
}
