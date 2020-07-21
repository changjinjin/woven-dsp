package com.info.baymax.dsp.access.dataapi.data.elasticsearch.jdbc;

import com.info.baymax.common.page.IPage;
import com.info.baymax.common.page.IPageable;
import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.common.service.criteria.query.RecordQuery;
import com.info.baymax.common.utils.DataBaseUtil;
import com.info.baymax.dsp.access.dataapi.data.MapEntity;
import com.info.baymax.dsp.access.dataapi.data.QueryParser;
import com.info.baymax.dsp.access.dataapi.data.StorageConf;
import com.info.baymax.dsp.access.dataapi.data.elasticsearch.ElasticSearchStorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.AbstractJdbcDataReader;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcStorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.AbstractQuerySql;
import com.info.baymax.dsp.data.consumer.beans.source.DBType;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

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
    protected IPage<MapEntity> executeQuery(StorageConf conf, IPageable pageable, AbstractQuerySql<?> selectSql)
        throws Exception {
        Connection conn = null;
        try {
            if (selectSql.isValid()) {
                conn = getConn((JdbcStorageConf) conf);
                if (conn != null) {
                    String countSql = selectSql.getPlaceholderCountSql();
                    long totalCount = runner
                        .query(conn, countSql, new ScalarHandler<Integer>(1), selectSql.getParamValues())
                        .longValue();
                    if (totalCount > 0) {
                        List<MapEntity> list = runner.query(conn, selectSql.getPlaceholderSql() + " limit "
                            + pageable.getOffset() + "," + pageable.getPageSize(), rsh, selectSql.getParamValues());
                        return IPage.<MapEntity>of(pageable, totalCount, list);
                    }
                }
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return IPage.<MapEntity>of(pageable, 0, null);
    }

    // 优先级要高（低）于ElasticSearchDataReader
    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Connection getConn(JdbcStorageConf conf) throws Exception {
        Properties properties = new Properties();
        properties.put("hostnameVerification", "false");
        properties.put("user", conf.getUsername());
        properties.put("password", conf.getPassword());
        return DataBaseUtil.getConnection(conf.getDriver(), conf.getUrl(), conf.getUsername(), conf.getPassword(),
            properties, null);
    }
}
