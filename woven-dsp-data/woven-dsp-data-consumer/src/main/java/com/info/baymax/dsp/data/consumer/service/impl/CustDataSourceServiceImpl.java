package com.info.baymax.dsp.data.consumer.service.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.utils.DataBaseUtil;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.mybatis.mapper.CustDataSourceMapper;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
public class CustDataSourceServiceImpl extends EntityClassServiceImpl<CustDataSource> implements CustDataSourceService {

    @Autowired
    private CustDataSourceMapper custDataSourceMapper;

    @Override
    public MyIdableMapper<CustDataSource> getMyIdableMapper() {
        return custDataSourceMapper;
    }

    @Override
    public List<String> selectTableList(String id) {
        CustDataSource dataSource = selectByPrimaryKey(id);
        return getTableListInner(dataSource);
    }

    @SuppressWarnings("unchecked")
    private List<String> getTableListInner(CustDataSource dataSource) {
        String url = (String) dataSource.getAttributes().get("url");
        List<String> list = null;
        log.info("table list cache missed resource.attributes.url  {}, will fetch from database.", url);
        String driver = (String) dataSource.getAttributes().get("driver");
        String user = (String) dataSource.getAttributes().get("user");
        String password = (String) dataSource.getAttributes().get("password");
        String catalog = (String) dataSource.getAttributes().get("catalog");
        String schema = (String) dataSource.getAttributes().get("schema");
        String jarPath = (String) dataSource.getAttributes().get("jarPath");
        List<Map<String, String>> properties = (List<Map<String, String>>) dataSource.getAttributes().get("properties");
        Properties p = null;
        if (properties != null) {
            p = new Properties();
            for (Map<String, String> e : properties) {
                Map.Entry<String, String> m = e.entrySet().iterator().next();
                if (StringUtils.isNotEmpty(m.getKey()) && StringUtils.isNotEmpty(m.getValue())) {
                    p.setProperty(m.getKey(), m.getValue());
                }
            }
        }
        try {
            list = DataBaseUtil.getTableList(driver, url, user, password, catalog, schema, p, jarPath);
        } catch (Throwable e) {
            log.error("getTableList error", e);
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean jdbcConnect(CustDataSource dataSource) {
        String url = (String) dataSource.getAttributes().get("url");
        String driver = (String) dataSource.getAttributes().get("driver");
        String user = (String) dataSource.getAttributes().get("user");
        String password = (String) dataSource.getAttributes().get("password");
        String jarPath = (String) dataSource.getAttributes().get("jarPath");
        List<Map<String, String>> properties = (List<Map<String, String>>) dataSource.getAttributes().get("properties");
        Properties p = null;
        if (properties != null) {
            p = new Properties();
            for (Map<String, String> e : properties) {
                Map.Entry<String, String> m = e.entrySet().iterator().next();
                p.setProperty(m.getKey(), m.getValue());
            }
        }

        try {
            DataBaseUtil.connect(driver, url, user, password, p, jarPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
