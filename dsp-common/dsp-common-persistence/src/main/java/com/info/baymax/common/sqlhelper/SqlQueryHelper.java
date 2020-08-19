package com.info.baymax.common.sqlhelper;

import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.page.IPageable;
import com.info.baymax.common.queryapi.result.MapEntity;
import com.info.baymax.common.queryapi.sql.AbstractQuerySql;
import com.info.baymax.common.utils.DataBaseUtil;
import com.info.baymax.common.utils.JsonUtils;
import com.jn.sqlhelper.apachedbutils.QueryRunner;
import com.jn.sqlhelper.dialect.pagination.PagingRequest;
import com.jn.sqlhelper.dialect.pagination.PagingResult;
import com.jn.sqlhelper.dialect.pagination.SqlPaginations;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
public class SqlQueryHelper {
    private static final QueryRunner runner = new QueryRunner();
    private static final MapEntityListHandler rsh = new MapEntityListHandler();

    public static Connection getConn(JdbcConf conf) throws Exception {
        return getConn(conf.getDriver(), conf.getUrl(), conf.getUsername(), conf.getPassword(), conf.getProps(),
            conf.getJarPath());
    }

    public static Connection getConn(String driver, String url, String username, String password) throws Exception {
        return getConn(driver, url, username, password);
    }

    public static Connection getConn(String driver, String url, String username, String password, Properties params,
                                     String jarPath) throws Exception {
        try {
            log.debug("get conn from conf: {driver:{}, url:{}, username:{}, password:*****, params:{}, jarPath:{}}",
                driver, url, username, params, jarPath);
            return DataBaseUtil.getConnection(driver, url, username, password, params, jarPath);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public static IPage<MapEntity> executeQuery(JdbcConf conf, AbstractQuerySql<?> selectSql, IPageable pageable)
        throws Exception {
        return executeQuery(getConn(conf), selectSql, pageable);
    }

    public static IPage<MapEntity> executeQuery(String driver, String url, String username, String password,
                                                Properties params, String jarPath, AbstractQuerySql<?> selectSql, IPageable pageable) throws Exception {
        return executeQuery(getConn(driver, url, username, password, params, jarPath), selectSql, pageable);
    }

    public static IPage<MapEntity> executeQuery(Connection conn, AbstractQuerySql<?> selectSql, IPageable pageable)
        throws Exception {
        try {
            log.debug("===>>start a sql query <<=== ");
            debug(selectSql, pageable);
            if (selectSql.isValid() && conn != null) {
                PagingRequest<?, MapEntity> request = SqlPaginations.preparePagination(pageable.getPageNum(),
                    pageable.getPageSize());
                List<MapEntity> list = runner.query(conn, selectSql.getPlaceholderSql(), rsh,
                    selectSql.getParamValues());
                log.debug("===>>query result size:" + list.size());
                PagingResult<MapEntity> result = request.getResult();
                if (result != null) {
                    return IPage.<MapEntity>of(pageable, result.getTotal(), result.getItems());
                }
            }
            log.debug("===>>end a sql query <<=== ");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return IPage.<MapEntity>of(pageable, 0, null);
    }

    public static <T extends Number> IPage<MapEntity> executeQuery(String driver, String url, String username,
                                                                   String password, Properties params, String jarPath, AbstractQuerySql<?> selectSql, IPageable pageable,
                                                                   ScalarHandler<T> scalarHandler, String limit) throws Exception {
        return executeQuery(getConn(driver, url, username, password, params, jarPath), selectSql, pageable,
            scalarHandler, limit);
    }

    public static <T extends Number> IPage<MapEntity> executeQuery(Connection conn, AbstractQuerySql<?> selectSql,
                                                                   IPageable pageable, ScalarHandler<T> scalarHandler, String limit) throws Exception {
        try {
            log.debug("===>>start a sql query <<=== ");
            debug(selectSql, pageable);
            if (selectSql.isValid() && conn != null) {
                String countSql = selectSql.getPlaceholderCountSql();
                long totalCount = runner.query(conn, countSql, scalarHandler, selectSql.getParamValues()).longValue();
                log.debug("===>>query count size:" + totalCount);
                if (totalCount > 0) {
                    List<MapEntity> list = runner.query(conn, selectSql.getPlaceholderSql() + " " + limit, rsh,
                        selectSql.getParamValues());
                    log.debug("===>>query result size:" + list.size());
                    return IPage.<MapEntity>of(pageable, totalCount, list);
                }
            }
            log.debug("===>>end a sql query <<=== ");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return IPage.<MapEntity>of(pageable, 0, null);
    }

    private static void debug(AbstractQuerySql<?> selectSql, IPageable pageable) {
        log.debug("===>>PlaceholderCountSql:" + selectSql.getPlaceholderCountSql());
        log.debug("===>>PlaceholderSql:" + selectSql.getPlaceholderSql());
        log.debug("===>>CountSql:" + selectSql.getCountSql());
        log.debug("===>>ExecuteSql:" + selectSql.getExecuteSql());
        log.debug("===>>ParamValues:" + Arrays.toString(selectSql.getParamValues()));
        log.debug("===>>Pageable:" + JsonUtils.toJson(pageable));
    }
}
