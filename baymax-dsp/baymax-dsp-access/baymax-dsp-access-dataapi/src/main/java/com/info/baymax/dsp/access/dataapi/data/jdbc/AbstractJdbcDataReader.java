package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.info.baymax.common.persistence.sqlhelper.SqlQueryHelper;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.page.IPageable;
import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.record.RecordQuery;
import com.info.baymax.common.queryapi.query.sql.SqlQuery;
import com.info.baymax.common.queryapi.result.MapEntity;
import com.info.baymax.common.queryapi.sql.AbstractQuerySql;
import com.info.baymax.common.queryapi.sql.SqlQuerySql;
import com.info.baymax.dsp.access.dataapi.data.Engine;
import com.info.baymax.dsp.access.dataapi.data.MapEntityDataReader;
import com.info.baymax.dsp.access.dataapi.data.QueryParser;
import com.info.baymax.dsp.access.dataapi.data.StorageConf;
import com.info.baymax.dsp.data.consumer.beans.source.DBType;

public abstract class AbstractJdbcDataReader extends MapEntityDataReader implements JdbcSqlDataReader {
    protected DBType dbType;

    public AbstractJdbcDataReader(DBType dbType) {
        super(Engine.JDBC);
        this.dbType = dbType;
    }

    @Override
    public boolean supports(StorageConf conf) {
        return (conf instanceof JdbcStorageConf) && super.supports(conf)
            && dbType.getValue().equals(((JdbcStorageConf) conf).getDBType());
    }

    @Override
    public IPage<MapEntity> readRecord(StorageConf conf, RecordQuery query) throws Exception {
        JdbcStorageConf jdbcStorageConf = (JdbcStorageConf) conf;
        return executeQuery(jdbcStorageConf, query.getPageable(),
            QueryParser.getInstance(JdbcQueryParser.class).parseRecordQuery(jdbcStorageConf, query));
    }

    @Override
    public IPage<MapEntity> readAgg(StorageConf conf, AggQuery query) throws Exception {
        JdbcStorageConf jdbcStorageConf = (JdbcStorageConf) conf;
        return executeQuery(jdbcStorageConf, query.getPageable(),
            QueryParser.getInstance(JdbcQueryParser.class).parseAggQuery(jdbcStorageConf, query));
    }

    @Override
    public IPage<MapEntity> readBySql(StorageConf conf, SqlQuery query) throws Exception {
        return executeQuery((JdbcStorageConf) conf, query.getPageable(), SqlQuerySql.builder(query));
    }

    protected IPage<MapEntity> executeQuery(JdbcStorageConf conf, IPageable pageable, AbstractQuerySql<?> selectSql)
        throws Exception {
        return SqlQueryHelper.executeQuery(conf.getDriver(), conf.getUrl(), conf.getUsername(), conf.getPassword(),
            null, null, selectSql, pageable);
    }
}
