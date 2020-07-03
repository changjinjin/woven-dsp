package com.info.baymax.dsp.access.dataapi.data.jdbc.condition;

import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.common.utils.ICollections;
import com.inforefiner.repackaged.org.apache.curator.shaded.com.google.common.collect.Lists;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

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
            // select column1,column2,column3... from table
            buf.append("select ").append(StringUtils.join(query.finalSelectProperties(), ", ")).append(" from ")
                .append(query.getTable());

            // where 条件
            ConditionSql whereConditionSql = ConditionSql.build(query.fieldGroup());
            if (whereConditionSql != null && StringUtils.isNotEmpty(whereConditionSql.getExecuteSql())) {
                buf.append(" where ").append(StringUtils.trimToEmpty(whereConditionSql.getExecuteSql())).append(" ");
            }

            // group by 条件
            LinkedHashSet<String> groupFields = query.getGroupFields();
            if (ICollections.hasElements(groupFields)) {
                buf.append(" group by ").append(StringUtils.join(groupFields, ", "));
            }

            // having 条件
            ConditionSql havingConditionSql = ConditionSql.build(query.getHavingFieldGroup());
            if (havingConditionSql != null && StringUtils.isNotEmpty(havingConditionSql.getExecuteSql())) {
                buf.append(" having ").append(StringUtils.trimToEmpty(havingConditionSql.getExecuteSql())).append(" ");
            }

            // order by 条件
            String orderBy = orderBy(query.getHavingSorts());
            if (StringUtils.isNotEmpty(orderBy)) {
                buf.append(" order by ").append(orderBy);
            }

            // 参数处理
            ArrayList<Object> newArrayList = Lists.newArrayList();
            Object[] paramValues1 = whereConditionSql.getParamValues();
            if (paramValues1 != null && paramValues1.length > 0) {
                newArrayList.addAll(Lists.newArrayList(paramValues1));
            }
            Object[] paramValues2 = havingConditionSql.getParamValues();
            if (paramValues2 != null && paramValues2.length > 0) {
                newArrayList.addAll(Lists.newArrayList(paramValues2));
            }

            this.executeSql = buf.toString();
            this.paramValues = newArrayList.toArray(new Object[newArrayList.size()]);
            log.debug("make executeSql:" + executeSql + ", paramValues:" + Arrays.toString(paramValues));
        }
    }

}
