package com.info.baymax.dsp.data.dataset.service.core.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.dataset.entity.core.ProcessConfig;
import com.info.baymax.dsp.data.dataset.mybatis.mapper.core.ProcessConfigMapper;
import com.info.baymax.dsp.data.dataset.service.core.ProcessConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessConfigServiceImpl extends EntityClassServiceImpl<ProcessConfig> implements ProcessConfigService {
    @Autowired
    protected ProcessConfigMapper processConfigMapper;

    @Override
    public MyIdableMapper<ProcessConfig> getMyIdableMapper() {
        return processConfigMapper;
    }

    @Override
    public List<ProcessConfig> findByProcessConfigType(String processConfigType) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder().andEqualTo("processConfigType", processConfigType))//
        );
    }

    @Override
    public List<ProcessConfig> findByProcessConfigType(String tenantId, String processConfigType) {

        return selectList(ExampleQuery.builder(getEntityClass()).fieldGroup(FieldGroup.builder()
            .andEqualTo(PROPERTY_TENANTID, tenantId).andEqualTo("processConfigType", processConfigType)));
    }

    @Override
    public ProcessConfig findOneByName(String tenantId, String name) {
        return selectOne(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(
                FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId).andEqualTo(PROPERTY_NAME, name))//
        );
    }
}
