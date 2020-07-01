package com.jn.sqlhelper.dialect.elasticsearch;

import com.jn.langx.annotation.Name;
import com.jn.sqlhelper.dialect.annotation.Driver;
import com.jn.sqlhelper.dialect.internal.AbstractDialect;
import com.jn.sqlhelper.dialect.internal.limit.LimitOnlyLimitHandler;
import com.jn.sqlhelper.dialect.likeescaper.BackslashStyleEscaper;

import java.sql.CallableStatement;
import java.sql.SQLException;

@Name("elasticsearch")
@Driver("com.amazon.opendistroforelasticsearch.jdbc.ElasticsearchDriver")
public class ElasticsearchDialect extends AbstractDialect<ElasticsearchDialect> {

    public ElasticsearchDialect() {
        super();
        setUrlParser(new ElasticsearchUrlParser());
        setLimitHandler(new LimitOnlyLimitHandler());
        setLikeEscaper(BackslashStyleEscaper.INSTANCE);
    }

    @Override
    public boolean isUseLimitInVariableMode() {
        return super.isUseLimitInVariableMode() ? super.isUseLimitInVariableMode() : false;
    }

    @Override
    public boolean isSupportsLimit() {
        return true;
    }

    @Override
    public boolean isSupportsLimitOffset() {
        return true;
    }

    @Override
    public int registerResultSetOutParameter(CallableStatement statement, int col) throws SQLException {
        return col;
    }

    @Override
    public char getBeforeQuote() {
        return '"';
    }

    @Override
    public char getAfterQuote() {
        return '"';
    }

    @Override
    public boolean isSupportsBatchUpdates() {
        return true;
    }

    @Override
    public boolean isSupportsBatchSql() {
        return true;
    }
}
