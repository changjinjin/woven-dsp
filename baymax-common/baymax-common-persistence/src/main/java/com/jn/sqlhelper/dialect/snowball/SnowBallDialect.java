package com.jn.sqlhelper.dialect.snowball;

import com.jn.langx.annotation.Name;
import com.jn.sqlhelper.dialect.annotation.Driver;
import com.jn.sqlhelper.dialect.internal.AbstractDialect;
import com.jn.sqlhelper.dialect.internal.limit.LimitCommaLimitHandler;
import com.jn.sqlhelper.dialect.internal.urlparser.MySqlUrlParser;
import com.jn.sqlhelper.dialect.likeescaper.BackslashStyleEscaper;

import java.sql.CallableStatement;
import java.sql.SQLException;

@Name("snowball")
@Driver("com.inforefiner.snowball.SnowballDriver")
public class SnowBallDialect extends AbstractDialect<SnowBallDialect> {

    public SnowBallDialect() {
        super();
        setUrlParser(new MySqlUrlParser());
        setLimitHandler(new LimitCommaLimitHandler());
        setLikeEscaper(BackslashStyleEscaper.INSTANCE);
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
        return '`';
    }

    @Override
    public char getAfterQuote() {
        return '`';
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
