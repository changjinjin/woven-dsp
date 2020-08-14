package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.info.baymax.access.dataapi.api.MapEntity;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.page.IPageable;
import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.record.RecordQuery;
import com.info.baymax.common.utils.DataBaseUtil;
import com.info.baymax.common.utils.JsonUtils;
import com.info.baymax.dsp.access.dataapi.data.Engine;
import com.info.baymax.dsp.access.dataapi.data.MapEntityDataReader;
import com.info.baymax.dsp.access.dataapi.data.QueryParser;
import com.info.baymax.dsp.access.dataapi.data.StorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.sql.AbstractQuerySql;
import com.info.baymax.dsp.data.consumer.beans.source.DBType;
import com.jn.sqlhelper.apachedbutils.QueryRunner;
import com.jn.sqlhelper.dialect.pagination.PagingRequest;
import com.jn.sqlhelper.dialect.pagination.PagingResult;
import com.jn.sqlhelper.dialect.pagination.SqlPaginations;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.List;

@Slf4j
public abstract class AbstractJdbcDataReader extends MapEntityDataReader {
    protected final QueryRunner runner = new QueryRunner();
    protected final MapEntityListHandler rsh = new MapEntityListHandler();

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
        return executeQuery(conf, query.getPageable(),
            QueryParser.getInstance(JdbcQueryParser.class).parseRecordQuery((JdbcStorageConf) conf, query));
    }

    @Override
    public IPage<MapEntity> readAgg(StorageConf conf, AggQuery query) throws Exception {
        return executeQuery(conf, query.getPageable(),
            QueryParser.getInstance(JdbcQueryParser.class).parseAggQuery((JdbcStorageConf) conf, query));
    }

    protected IPage<MapEntity> executeQuery(StorageConf conf, IPageable pageable, AbstractQuerySql<?> selectSql)
        throws Exception {
        Connection conn = null;
        try {
            if (selectSql.isValid()) {
                conn = getConn((JdbcStorageConf) conf);
                if (conn != null) {
                    PagingRequest<?, MapEntity> request = SqlPaginations.preparePagination(pageable.getPageNum(),
                        pageable.getPageSize());
                    List<MapEntity> list = runner.query(conn, selectSql.getPlaceholderSql(), rsh,
                        selectSql.getParamValues());
                    log.debug("query result:" + list.size());
                    PagingResult<MapEntity> result = request.getResult();
                    if (result != null) {
                        return IPage.<MapEntity>of(pageable, result.getTotal(), result.getItems());
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

    public Connection getConn(JdbcStorageConf conf) throws Exception {
        try {
            log.debug("get conn from conf: " + JsonUtils.toJson(conf));
            return DataBaseUtil.getConnection(conf.getDriver(), conf.getUrl(), conf.getUsername(), conf.getPassword(),
                null, null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

}
