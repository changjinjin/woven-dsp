package com.info.baymax.dsp.data.platform.service.impl;

import com.info.baymax.common.core.enums.types.YesNoType;
import com.info.baymax.common.core.exception.ServiceException;
import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.persistence.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
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
        switch (t.getSourceType()) {
            case DATASOURCE:
                // TODO nothing
                break;
            default:
                // 检查数据集数据是否存在并修改数据集的关联状态
                Dataset dataset = datasetService.selectByPrimaryKey(t.getDatasetId());
                if (dataset == null) {
                    throw new ServiceException(ErrType.ENTITY_NOT_EXIST, "绑定的数据集记录不存在或者已经过期。");
                }
                dataset.setIsRelated(YesNoType.YES.getValue());
                datasetService.updateByPrimaryKeySelective(dataset);
                break;
        }
        return DataResourceService.super.save(t);
    }

    @Override
    public void closeDataResource(List<Long> ids) {
        DataResource record = new DataResource();
        record.setOpenStatus(0);
        updateByExampleSelective(record, ExampleQuery.builder(getEntityClass())
            .fieldGroup(FieldGroup.builder().andIn("id", ids.toArray(new Long[ids.size()]))));
    }

    @Override
    public IPage<Dataset> queryDatasets(ExampleQuery exampleQuery) {
        return datasetService.selectPage(exampleQuery);
    }

    @Override
    public List<DataResource> selectDataResourceListByIds(List<Long> ids) {
        return resourceMapper.selectDataResourceListByIds(ids);
    }

    @Override
    public DataResource findOneByName(String tenant, String name) {
        List<DataResource> list = selectList(ExampleQuery.builder(getEntityClass())//
                .fieldGroup(FieldGroup.builder().andEqualTo("tenantId", tenant).andEqualTo("name", name))//
                .orderByDesc("lastModifiedTime"));

        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
