package com.info.baymax.dsp.access.dataapi.data.elasticsearch.jdbc;

import com.info.baymax.common.persistence.sqlhelper.SqlQueryHelper;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.page.IPageable;
import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.record.RecordQuery;
import com.info.baymax.common.queryapi.result.MapEntity;
import com.info.baymax.common.queryapi.sql.AbstractQuerySql;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.access.dataapi.data.QueryParser;
import com.info.baymax.dsp.access.dataapi.data.StorageConf;
import com.info.baymax.dsp.access.dataapi.data.elasticsearch.ElasticSearchStorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.AbstractJdbcDataReader;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcStorageConf;
import com.info.baymax.dsp.data.consumer.beans.source.DBType;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
public class ElasticSearchJdbcDataReader extends AbstractJdbcDataReader {
    public ElasticSearchJdbcDataReader() {
        super(DBType.Elasticsearch);
    }

    @Override
    public boolean supports(StorageConf conf) {
        return (conf instanceof ElasticSearchStorageConf) && DBType.Elasticsearch.name()
            .equals((new ElasticSearchJdbcStorageConf((ElasticSearchStorageConf) conf)).getDBType());
    }

    @Override
    public IPage<MapEntity> readRecord(StorageConf conf, RecordQuery query) throws Exception {
        ElasticSearchJdbcStorageConf elasticSearchJdbcStorageConf = new ElasticSearchJdbcStorageConf(
            (ElasticSearchStorageConf) conf);
        return executeQuery(elasticSearchJdbcStorageConf, query.getPageable(), QueryParser
            .getInstance(ElasticSearchJdbcQueryParser.class).parseRecordQuery(elasticSearchJdbcStorageConf, query));
    }

    @Override
    public IPage<MapEntity> readAgg(StorageConf conf, AggQuery query) throws Exception {
        ElasticSearchJdbcStorageConf elasticSearchJdbcStorageConf = new ElasticSearchJdbcStorageConf(
            (ElasticSearchStorageConf) conf);
        return executeQuery(elasticSearchJdbcStorageConf, query.getPageable(), QueryParser
            .getInstance(ElasticSearchJdbcQueryParser.class).parseAggQuery(elasticSearchJdbcStorageConf, query));
    }

    @Override
    protected IPage<MapEntity> executeQuery(JdbcStorageConf conf, IPageable pageable, AbstractQuerySql<?> selectSql)
        throws Exception {
        // 附加参数
        Properties properties = new Properties();
        properties.put("hostnameVerification", "false");
        properties.put("user", conf.getUsername());
        properties.put("password", conf.getPassword());

        // do query
        IPage<MapEntity> page = SqlQueryHelper.executeQuery(conf.getDriver(), conf.getUrl(), conf.getUsername(),
            conf.getPassword(), properties, null, selectSql, pageable, new ScalarHandler<Integer>(1),
            "limit " + pageable.getOffset() + "," + pageable.getPageSize());

        // 处理keyword后缀
        if (page != null && ICollections.hasElements(page.getList())) {
            page.setList(trimeKeyword(page.getList()));
        }
        return page;
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private List<MapEntity> trimeKeyword(List<MapEntity> entitys) {
        return entitys.stream().map(t -> trimeKeyword(t)).collect(Collectors.toList());
    }

    
    //XXX SQL查询通过keyword过滤的字段返回的字段名称会带上 ".keyowrd"后缀，这里将后缀去掉
    private MapEntity trimeKeyword(MapEntity entity) {
        final MapEntity newEntity = MapEntity.build();
        entity.forEach((k, v) -> {
            if (k.endsWith(".keyword")) {
                newEntity.put(StringUtils.removeEnd(k, ".keyword"), v);
            } else {
                newEntity.put(k, v);
            }
        });
        return newEntity;
    }
}
