package com.info.baymax.dsp.access.dataapi.data.jdbc.condition;

import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.common.utils.ICollections;
import com.inforefiner.repackaged.com.google.common.collect.Sets;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@ToString(callSuper = true)
@Slf4j
public class AggQuerySql extends AbstractQuerySql<AggQuery> {
    private static final long serialVersionUID = 9076946654365840665L;

    protected AggQuerySql(String tableAlias, AggQuery query) {
        super(tableAlias, query);
    }

    public static AggQuerySql builder(AggQuery query) {
        return new AggQuerySql("", query);
    }

    public static AggQuerySql builder(String tableAlias, AggQuery query) {
        return new AggQuerySql(tableAlias, query);
    }

    @Override
    protected void build(AggQuery query) {
        if (valid(query)) {
            StringBuffer buf = new StringBuffer();
            buf.append(selectfromTableWhere(query.getTable(), tableAlias, query.getFieldGroup()));
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
                .toString(), properties(query.getFinalSelectProperties(tableAlias)));
            log.debug("\n\nmake placeholderCountSql:" + placeholderCountSql);
            log.debug("make placeholderSql:" + placeholderSql);
            log.debug("make paramValues:" + Arrays.toString(paramValues) + "\n\n");
        }
    }

    protected String having(FieldGroup havingFieldGroup) {
        StringBuffer buf = new StringBuffer();
        ConditionSql havingConditionSql = ConditionSql.build(havingFieldGroup);
        if (havingConditionSql != null && StringUtils.isNotEmpty(havingConditionSql.getPlaceholderSql())) {
            buf.append(" having ").append(StringUtils.trimToEmpty(havingConditionSql.getPlaceholderSql()));
        }
        addParamValues(havingConditionSql.getParamValues());
        return buf.toString();
    }

    protected String groupBy(LinkedHashSet<String> groupFields) {
        if (ICollections.hasElements(groupFields)) {
            if (StringUtils.isNotEmpty(tableAlias)) {
                groupFields = Sets.newLinkedHashSet(
                    groupFields.stream().map(t -> getTableAliasAndDot() + t).collect(Collectors.toList()));
            }
            return new StringBuffer().append(" group by ").append(StringUtils.join(groupFields, ", ")).toString();
        }
        return "";
    }

}
