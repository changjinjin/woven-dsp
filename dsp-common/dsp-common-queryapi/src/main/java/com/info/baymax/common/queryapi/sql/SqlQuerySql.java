package com.info.baymax.common.queryapi.sql;

import com.info.baymax.common.queryapi.query.sql.SqlQuery;

import java.util.List;

public class SqlQuerySql extends AbstractQuerySql<SqlQuery> {

    protected SqlQuerySql(SqlQuery query) {
        super(query);
    }

    public static SqlQuerySql builder(SqlQuery query) {
        return new SqlQuerySql(query);
    }

    protected void build(SqlQuery query) {
        if (valid(query)) {
            NamingSql namingSql = new NamingSql(query.getSqlTemplate(), query.getParametersMap());
            this.placeholderCountSql = "select count(1) from (" + namingSql.getPlaceholderSql() + ")";
            this.placeholderSql = namingSql.getPlaceholderSql();
            this.paramValues = namingSql.getParamValues();
        }
    }

    @Override
    protected List<String> getSelectProperties(SqlQuery query) {
        return null;
    }

}
