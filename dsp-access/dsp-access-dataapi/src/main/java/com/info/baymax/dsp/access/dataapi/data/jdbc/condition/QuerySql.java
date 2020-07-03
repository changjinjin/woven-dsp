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

    protected QuerySql(JdbcQuery query) {
        super(query);
    }

    public static QuerySql builder(JdbcQuery query) {
        return new QuerySql(query);
    }

    protected void build(JdbcQuery query) {
        if (valid(query)) {
            StringBuffer buf = new StringBuffer();

            // select column1,column2,column3... from table
            buf.append("select ").append(StringUtils.join(query.finalSelectProperties(), ", ")).append(" from ")
                .append(query.getTable());

            // where 条件
            ConditionSql whereConditionSql = ConditionSql.build(query.fieldGroup());
            if (whereConditionSql != null && StringUtils.isNotEmpty(whereConditionSql.getExecuteSql())) {
                buf.append(" where ").append(whereConditionSql.getExecuteSql()).append(" ");
            }

            // order by 条件
            String orderBy = orderBy(query.getOrdSort());
            if (StringUtils.isNotEmpty(orderBy)) {
                buf.append(" order by ").append(orderBy);
            }

            this.executeSql = buf.toString();
            this.paramValues = whereConditionSql.getParamValues();
            log.debug("make executeSql:" + executeSql + ", paramValues:" + Arrays.toString(paramValues));
        }
    }

}
