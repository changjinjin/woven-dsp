package com.info.baymax.dsp.data.dataset.service.core.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.info.baymax.common.core.exception.ServiceException;
import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.core.result.MapEntity;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.persistence.sqlhelper.JdbcConf;
import com.info.baymax.common.persistence.sqlhelper.SqlQueryHelper;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.queryapi.query.sql.SqlQuery;
import com.info.baymax.common.queryapi.sql.SqlQuerySql;
import com.info.baymax.common.utils.DataBaseUtil;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.dataset.entity.core.DataSource;
import com.info.baymax.dsp.data.dataset.mybatis.mapper.core.DataSourceMapper;
import com.info.baymax.dsp.data.dataset.service.core.DataSourceService;
import com.info.baymax.dsp.data.dataset.service.resource.QueryObjectByResourceOrProjectServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DataSourceServiceImpl extends QueryObjectByResourceOrProjectServiceImpl<DataSource>
    implements DataSourceService {
    private static final Cache<String, List<String>> tableListCache = CacheBuilder.newBuilder().maximumSize(10000)
        .expireAfterWrite(10, TimeUnit.MINUTES).build();
    private static final Cache<String, List<String>> tableColumnsCache = CacheBuilder.newBuilder().maximumSize(10000)
        .expireAfterWrite(10, TimeUnit.MINUTES).build();

    @Autowired
    protected DataSourceMapper dataSourceMapper;

    @Override
    public MyIdableMapper<DataSource> getMyIdableMapper() {
        return dataSourceMapper;
    }

    @Override
    public List<DataSource> findByType(String type) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder().andEqualTo("type", type)));
    }

    @Override
    public List<DataSource> findByType(String tenantId, String type) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder()//
                .andEqualTo(PROPERTY_TENANTID, tenantId)//
                .andEqualTo("type", type)//
            ));
    }

    @Override
    public List<String> getDistinctTypes(String tenantId) {
        return dataSourceMapper.getDistinctTypes(tenantId);
    }

    @Override
    public List<String> fetchTableList(String dataSourceId) {
        return getTableListInner(selectByPrimaryKey(dataSourceId));
    }

    @Override
    public List<String> fetchTableColumns(String dataSourceId, String table) {
        return getTableColumnListInner(selectByPrimaryKey(dataSourceId), table);
    }

    private List<String> getTableListInner(DataSource dataSource) {
        List<String> list = null;
        list = tableListCache.getIfPresent(dataSource.getId());
        if (ICollections.hasNoElements(list)) {
            String url = dataSource.getAttributes().get("url").toString();
            log.info("table list cache missed resource.attributes.url  {}, will fetch from database.", url);
            String driver = (String) dataSource.getAttributes().get("driver");
            String user = (String) dataSource.getAttributes().get("user");
            String password = (String) dataSource.getAttributes().get("password");
            String catalog = (String) dataSource.getAttributes().get("catalog");
            String schema = (String) dataSource.getAttributes().get("schema");
            String jarPath = null;// dataSourceAppService.getDependicyLocalPath(dataSource);
            List<Map<String, String>> properties = (List) dataSource.getAttributes().get("properties");
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
                if (ICollections.hasElements(list)) {
                    tableListCache.put(dataSource.getId(), list);
                }
            } catch (Throwable e) {
                log.error("getTableList error", e);
                throw new RuntimeException(e);
            }
        } else {
            log.info("table list cache hit resource.id {}", dataSource.getId());
        }
        return list;
    }

    private List<String> getTableColumnListInner(DataSource dataSource, String table) {
        List<String> list = null;
        try {
            list = tableColumnsCache.getIfPresent(dataSource.getId() + "_" + table);
            if (ICollections.hasNoElements(list)) {
                log.info("column cache missed resource.id {} table {}, will fetch from database.", dataSource.getId(),
                    table);
                String url = (String) dataSource.getAttributes().get("url");
                String driver = (String) dataSource.getAttributes().get("driver");
                String user = (String) dataSource.getAttributes().get("user");
                String password = (String) dataSource.getAttributes().get("password");
                String catalog = (String) dataSource.getAttributes().get("catalog");
                String schema = (String) dataSource.getAttributes().get("schema");
                String jarPath = null;// dataSourceAppService.getDependicyLocalPath(dataSource);
                List<Map<String, String>> properties = (List) dataSource.getAttributes().get("properties");
                Properties p = null;
                if (properties != null) {
                    p = new Properties();
                    for (Map<String, String> e : properties) {
                        Map.Entry<String, String> m = e.entrySet().iterator().next();
                        p.setProperty(m.getKey(), m.getValue());
                    }
                }

                try {
                    list = DataBaseUtil.getTableColumnList(driver, url, user, password, p, table, catalog, schema,
                        jarPath);
                    if (ICollections.hasElements(list)) {
                        tableColumnsCache.put(dataSource.getId() + "_" + table, list);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                log.info("column cache hit resource.id {} table {}", dataSource.getId(), table);
            }
        } catch (Exception e) {
            log.error("get column list error: ", e);
        }
        return list;
    }

    @Override
    public IPage<MapEntity> previewBySql(String dataSourceId, SqlQuery query) {
        try {
            DataSource dataSource = selectByPrimaryKey(dataSourceId);
            if (dataSource == null) {
                throw new ServiceException(ErrType.ENTITY_NOT_EXIST,
                    String.format("The data source with ID %s does not exist.", dataSourceId));
            }
            JdbcConf conf = JdbcConf.from(dataSource.getAttributes());
            conf.setJarPath(null);
            return SqlQueryHelper.executeQuery(conf, SqlQuerySql.builder(query), query.getPageable());
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public DataSource findOneByName(String tenant, String name) {
        List<DataSource> list = selectList(ExampleQuery.builder(getEntityClass())//
                .fieldGroup(FieldGroup.builder().andEqualTo("tenantId", tenant).andEqualTo("name", name))//
                .orderByDesc("lastModifiedTime"));

        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
