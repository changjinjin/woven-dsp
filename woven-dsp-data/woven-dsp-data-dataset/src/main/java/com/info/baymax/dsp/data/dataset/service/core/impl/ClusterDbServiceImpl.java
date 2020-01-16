package com.info.baymax.dsp.data.dataset.service.core.impl;

import com.info.baymax.common.jpa.criteria.query.QueryObject;
import com.info.baymax.common.jpa.page.Page;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.dataset.entity.core.ClusterEntity;
import com.info.baymax.dsp.data.dataset.mybatis.mapper.core.ClusterMapper;
import com.info.baymax.dsp.data.dataset.service.core.ClusterDbService;
import com.info.baymax.dsp.data.dataset.service.resource.QueryObjectByResourceOrProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    ExampleQuery exampleQuery = exampleQuery(queryObject);
    Set<String> excludeproperties = new HashSet<String>() {{
      add(CONFIG_FILE);
    }};
    exampleQuery.setExcludeProperties(excludeproperties);
    return findObjectPage(exampleQuery);
  }

  @Override
  public ClusterEntity findOneByName(String tenantId, String name, boolean includeConfFile) {
    ExampleQuery exampleQuery = ExampleQuery.builder(getEntityClass())
            .fieldGroup()
            .andEqualTo(PROPERTY_TENANTID, tenantId)
            .andEqualTo(PROPERTY_NAME, name)
            .end();
    if (!includeConfFile) {
      Set<String> excludeproperties = new HashSet<String>() {{
        add(CONFIG_FILE);
      }};
      exampleQuery.setExcludeProperties(excludeproperties);
    }
    return selectOne(exampleQuery);
  }

  @Override
  public List<ClusterEntity> findAllEnabledCluster(String tenantId) {
    ExampleQuery exampleQuery = ExampleQuery.builder(getEntityClass())
            .fieldGroup()
            .andEqualTo(PROPERTY_TENANTID, tenantId)
            .andEqualTo("enabled", 1)
            .end();
    exampleQuery.setExcludeProperties(new HashSet<String>() {{
      add(CONFIG_FILE);
    }});
    return selectList(exampleQuery);
  }
}
