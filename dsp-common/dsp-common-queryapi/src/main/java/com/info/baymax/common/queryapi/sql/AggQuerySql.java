package com.info.baymax.common.queryapi.sql;

import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashSet;
import java.util.List;

public class AggQuerySql extends AbstractQuerySql<AggQuery> {

    protected AggQuerySql(AggQuery query) {
        super(query);
    }

    public static AggQuerySql builder(AggQuery query) {
        return new AggQuerySql(query);
    }

    @Override
    protected void build(AggQuery query) {
        if (valid(query)) {
            StringBuffer buf = new StringBuffer();
            buf.append(selectfromTableWhere(query.getTable(), query.getFieldGroup()));
            // group by 条件
            buf.append(groupBy(query.getGroupFields()));
            // having 条件
            buf.append(having(query.getHavingFieldGroup()));

            // 参数处理
            String sql = buf.toString();
            this.placeholderCountSql = String.format(sql, count());
            this.placeholderSql = String.format(new StringBuffer()//
                    .append(sql)//
                    .append(orderBy(query.getOrdSort()))// order by 条件
                    .toString(), properties(query));
        }
    }

    protected String having(FieldGroup havingFieldGroup) {
        StringBuffer buf = new StringBuffer();
        ConditionSql havingConditionSql = ConditionSql.build(havingFieldGroup);
        if (havingConditionSql != null && StringUtils.isNotEmpty(havingConditionSql.getPlaceholderSql())) {
            buf.append(" HAVING ").append(StringUtils.trimToEmpty(havingConditionSql.getPlaceholderSql()));
        }
        addParamValues(havingConditionSql.getParamValues());
        return buf.toString();
    }

    protected String groupBy(LinkedHashSet<String> groupFields) {
        if (groupFields != null && !groupFields.isEmpty()) {
            return new StringBuffer().append(" GROUP BY ").append(StringUtils.join(groupFields, ", ")).toString();
        }
        return "";
    }

    @Override
    protected List<String> getSelectProperties(AggQuery query) {
        return query.getFinalSelectProperties();
    }

}
