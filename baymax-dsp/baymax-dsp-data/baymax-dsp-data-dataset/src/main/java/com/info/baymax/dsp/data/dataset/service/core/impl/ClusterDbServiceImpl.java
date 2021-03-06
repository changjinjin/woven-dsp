package com.info.baymax.dsp.data.dataset.service.core.impl;

import com.info.baymax.common.persistence.jpa.criteria.query.QueryObject;
import com.info.baymax.common.persistence.jpa.page.Page;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.persistence.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.dsp.data.dataset.entity.core.ClusterEntity;
import com.info.baymax.dsp.data.dataset.mybatis.mapper.core.ClusterMapper;
import com.info.baymax.dsp.data.dataset.service.core.ClusterDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by pengchuan.chen on 2019/11/27
 */
@Service
public class ClusterDbServiceImpl extends EntityClassServiceImpl<ClusterEntity> implements ClusterDbService {

    protected static final String CONFIG_FILE = "configFile";

    @Autowired
    private ClusterMapper clusterMapper;

    @Override
    public MyIdableMapper<ClusterEntity> getMyIdableMapper() {
        return clusterMapper;
    }

    @Override
    public Page<ClusterEntity> findObjectPage(QueryObject queryObject) {
        return findObjectPage(exampleQuery(queryObject).excludeProperties(CONFIG_FILE));
    }

    @Override
    public ClusterEntity findOneByName(String tenantId, String name, boolean includeConfFile) {
        ExampleQuery exampleQuery = ExampleQuery.builder(getEntityClass()).fieldGroup(
            FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId).andEqualTo(PROPERTY_NAME, name));
        if (!includeConfFile) {
            exampleQuery.excludeProperties(CONFIG_FILE);
        }
        return selectOne(exampleQuery);
    }

    @Override
    public List<ClusterEntity> findAllEnabledCluster(String tenantId) {
        ExampleQuery exampleQuery = ExampleQuery.builder(getEntityClass()).excludeProperties(CONFIG_FILE)
            .fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId).andEqualTo("enabled", 1));
        return selectList(exampleQuery);
    }
}
