package com.info.baymax.dsp.access.dataapi.data.jdbc.condition;

import com.info.baymax.common.service.criteria.agg.AggQuery;
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
            buf.append(selectAndWhere(query.getFinalSelectProperties(tableAlias), query.getTable(), tableAlias,
                query.getFieldGroup()));

            // group by 条件
            LinkedHashSet<String> groupFields = query.getGroupFields();
            if (ICollections.hasElements(groupFields)) {
                if (StringUtils.isNotEmpty(tableAlias)) {
                    groupFields = Sets.newLinkedHashSet(
                        groupFields.stream().map(t -> getTableAliasAndDot() + t).collect(Collectors.toList()));
                }
                buf.append(" group by ").append(StringUtils.join(groupFields, ", "));
            }

            // having 条件
            ConditionSql havingConditionSql = ConditionSql.build(query.getHavingFieldGroup());
            if (havingConditionSql != null && StringUtils.isNotEmpty(havingConditionSql.getPlaceholderSql())) {
                buf.append(" having ").append(StringUtils.trimToEmpty(havingConditionSql.getPlaceholderSql()));
            }

            // order by 条件
            String orderBy = orderBy(query.getOrdSort());
            if (StringUtils.isNotEmpty(orderBy)) {
                buf.append(" order by ").append(orderBy);
            }

            // 参数处理

            addParamValues(havingConditionSql.getParamValues());
            this.placeholderSql = buf.toString();
            log.debug("make executeSql:" + placeholderSql + ", paramValues:" + Arrays.toString(paramValues));
        }
    }

}
