package com.info.baymax.dsp.access.dataapi.data.elasticsearch;

import com.info.baymax.common.page.IPageable;
import com.info.baymax.dsp.access.dataapi.data.Query;
import com.info.baymax.dsp.access.dataapi.data.QueryParser;
import com.info.baymax.dsp.access.dataapi.data.StorageConf;
import com.info.baymax.dsp.access.dataapi.data.elasticsearch.jdbc.ElasticSearchJdbcStorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcQueryParser;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcStorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.SelectSql;
import com.jn.sqlhelper.dialect.elasticsearch.ElasticsearchDialect;
import com.jn.sqlhelper.dialect.internal.limit.AbstractLimitHandler;
import com.jn.sqlhelper.dialect.internal.limit.LimitHandler;
import com.jn.sqlhelper.dialect.internal.limit.LimitHelper;
import com.jn.sqlhelper.dialect.pagination.RowSelection;
import io.searchbox.core.Search;
import lombok.extern.slf4j.Slf4j;
import org.es.sql.bean.ElasticSqlParseResult;
import org.es.sql.parser.ElasticSql2DslParser;

import java.util.Locale;

@Slf4j
public class ElasticsearchQuerySqlParser extends ElasticsearchQueryParser {

    @Override
    public Search parse(ElasticSearchStorageConf storageConf, Query query) throws Exception {
        IPageable pageable = query.getPageable();
        SelectSql selectSql = extracted(pageable, parseSql(new ElasticSearchJdbcStorageConf(storageConf), query));
        ElasticSql2DslParser sql2DslParser = new ElasticSql2DslParser();
        ElasticSqlParseResult parseResult = sql2DslParser.parse(selectSql.getExecuteSql(), selectSql.getParamValues());
        String dsl = parseResult.toDsl();
        log.debug("query dsl：" + dsl);
        return new Search.Builder(dsl).addIndex(storageConf.getIndex()).addType(storageConf.getIndexType())
            .allowNoIndices(true).ignoreUnavailable(true).build();
    }

    private SelectSql extracted(IPageable pageable, SelectSql selectSql) {
        LimitHandler limitHandler = new SimpleLimitHandler();
        ElasticsearchDialect dialect = new ElasticsearchDialect();
        dialect.setUseLimitInVariableMode(true);
        limitHandler.setDialect(dialect);

        long offset = pageable.getOffset();
        Integer pageSize = pageable.getPageSize();

        RowSelection rowSelection = new RowSelection();
        rowSelection.setOffset(offset);
        rowSelection.setLimit(pageSize);

        selectSql.addParamValues((int) offset, pageSize);
        String processSql = limitHandler.processSql(selectSql.getExecuteSql(), rowSelection);
        log.debug("process sql:" + processSql);
        selectSql.setExecuteSql(processSql);
        return selectSql;
    }

    public SelectSql parseSql(StorageConf conf, Query query) throws Exception {
        return QueryParser.getInstance(JdbcQueryParser.class).parse((JdbcStorageConf) conf, query);
    }

}

class SimpleLimitHandler extends AbstractLimitHandler {
    private boolean isSupportForUpdate;

    @Override
    public String processSql(String sql, RowSelection rowSelection) {
        return getLimitString(sql, LimitHelper.getFirstRow(rowSelection), getMaxOrLimit(rowSelection));
    }

    @Override
    protected String getLimitString(String sql, long offset, int limit) {
        sql = sql.trim();
        String forUpdateClause = "";
        boolean isForUpdate = false;
        if (isSupportForUpdate()) {
            String sqlLowercase = sql.toLowerCase(Locale.ROOT);
            int forUpdateIndex = sqlLowercase.lastIndexOf("for update");
            if (forUpdateIndex > -1) {
                forUpdateClause = sql.substring(forUpdateIndex);
                sql = sql.substring(0, forUpdateIndex - 1);
                isForUpdate = true;
            }
        }

        StringBuilder sql2 = new StringBuilder(sql.length() + 100);
        sql2.append(sql);

        if (getDialect().isUseLimitInVariableMode()) {
            sql2.append(" limit ?, ?");
        } else {
            sql2.append(" limit " + offset + ", " + limit);
        }
        if (isForUpdate) {
            sql2.append(" " + forUpdateClause);
        }
        return sql2.toString();
    }

    public boolean isSupportForUpdate() {
        return isSupportForUpdate;
    }

    public SimpleLimitHandler setSupportForUpdate(boolean supportForUpdate) {
        isSupportForUpdate = supportForUpdate;
        return this;
    }
}