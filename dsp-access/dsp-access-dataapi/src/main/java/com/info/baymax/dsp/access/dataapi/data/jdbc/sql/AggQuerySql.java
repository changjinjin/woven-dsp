package com.info.baymax.dsp.access.dataapi.data.jdbc.sql;

import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.utils.ICollections;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

@ToString(callSuper = true)
@Slf4j
public class AggQuerySql extends AbstractQuerySql<AggQuery> {
    private static final long serialVersionUID = 9076946654365840665L;

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
            log.debug("\n\nmake placeholderCountSql:" + placeholderCountSql);
            log.debug("make placeholderSql:" + placeholderSql);
            log.debug("make paramValues:" + Arrays.toString(paramValues) + "\n\n");
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
        if (ICollections.hasElements(groupFields)) {
            return new StringBuffer().append(" GROUP BY ").append(StringUtils.join(groupFields, ", ")).toString();
        }
        return "";
    }

    @Override
    protected List<String> getSelectProperties(AggQuery query) {
        return query.getFinalSelectProperties();
    }

}
