package com.info.baymax.dsp.access.dataapi.data.jdbc.condition;

import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcQuery;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@ToString(callSuper = true)
@Slf4j
public class QuerySql extends AbstractQuerySql<JdbcQuery> {
    private static final long serialVersionUID = 9076946654365840665L;

    protected QuerySql(String tableAlias, JdbcQuery query) {
        super(tableAlias, query);
    }

    public static QuerySql builder(JdbcQuery query) {
        return new QuerySql("", query);
    }

    public static QuerySql builder(String tableAlias, JdbcQuery query) {
        return new QuerySql(tableAlias, query);
    }

    protected void build(JdbcQuery query) {
        if (valid(query)) {
            StringBuffer buf = new StringBuffer();
            buf.append(selectAndWhere(query.getFinalSelectProperties(tableAlias), query.getTable(), tableAlias,
                query.getFieldGroup()));

            // order by 条件
            String orderBy = orderBy(query.getOrdSort());
            if (StringUtils.isNotEmpty(orderBy)) {
                buf.append(" order by ").append(orderBy);
            }
            this.placeholderSql = buf.toString();
            log.debug("make executeSql:" + placeholderSql + ", paramValues:" + Arrays.toString(paramValues));
        }
    }

}
