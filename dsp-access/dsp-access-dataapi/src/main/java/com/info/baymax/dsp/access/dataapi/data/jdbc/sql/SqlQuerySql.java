package com.info.baymax.dsp.access.dataapi.data.jdbc.sql;

import com.info.baymax.common.queryapi.query.parameters.SqlQuery;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@ToString(callSuper = true)
@Slf4j
public class SqlQuerySql extends AbstractQuerySql<SqlQuery> {
    private static final long serialVersionUID = 9076946654365840665L;

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
            log.debug("\n\nmake placeholderCountSql:" + placeholderCountSql);
            log.debug("make placeholderSql:" + placeholderSql);
            log.debug("make paramValues:" + Arrays.toString(paramValues) + "\n\n");
        }
    }

    @Override
    protected List<String> getSelectProperties(SqlQuery query) {
        return null;
    }

}
