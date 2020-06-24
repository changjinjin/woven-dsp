package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.info.baymax.common.page.IPage;
import com.info.baymax.common.page.IPageable;
import com.info.baymax.common.utils.DataBaseUtil;
import com.info.baymax.dsp.access.dataapi.data.*;
import com.info.baymax.dsp.access.dataapi.data.condition.RequestQuery;
import com.info.baymax.dsp.access.dataapi.data.condition.SelectSql;
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
    protected DBType dbType;

    public AbstractJdbcDataReader(DBType dbType) {
        super(Engine.JDBC);
        this.dbType = dbType;
    }

    @Override
    public boolean supports(StorageConf conf) {
        return (conf instanceof JdbcStorageConf) && super.supports(conf)
            && dbType.name().equals(((JdbcStorageConf) conf).getDBType());
    }

    @Override
    public IPage<MapEntity> read(StorageConf conf, RequestQuery query) throws DataReadException {
        IPageable pageable = query.getPageable();
        try {
            JdbcStorageConf jdbcConf = (JdbcStorageConf) conf;
            SelectSql selectSql = SelectSql.build(query.table(jdbcConf.getTable()));
            if (selectSql.isValid()) {
                Connection conn = getConn(jdbcConf);
                if (conn != null) {
                    PagingRequest<?, MapEntity> request = SqlPaginations.preparePagination(pageable.getPageNum(),
                        pageable.getPageSize());
                    QueryRunner runner = new QueryRunner();
                    List<MapEntity> list = runner.query(conn, selectSql.getExecuteSql(), new MapEntityListHandler(),
                        selectSql.getParamValues());
                    log.debug("query result:" + list.size());
                    PagingResult<MapEntity> result = request.getResult();
                    if (result != null) {
                        return IPage.<MapEntity>of(pageable, result.getTotal(), result.getItems());
                    }
                }
            }
        } catch (Exception e) {
            throw new DataReadException(e.getMessage(), e);
        }
        return IPage.<MapEntity>of(pageable, 0, null);
    }

    private Connection getConn(JdbcStorageConf conf) throws Exception {
        try {
            return DataBaseUtil.getConnection(conf.getDriver(), conf.getUrl(), conf.getUsername(), conf.getPassword(),
                null, null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

}
