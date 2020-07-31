package com.info.baymax.dsp.access.dataapi.data.elasticsearch.jdbc;

import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.AggQuerySql;
import com.inforefiner.repackaged.com.google.common.collect.Sets;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@ToString(callSuper = true)
public class ElasticSearchAggQuerySql extends AggQuerySql {
    private static final long serialVersionUID = 9076946654365840665L;

    protected ElasticSearchAggQuerySql(String tableAlias, AggQuery query) {
        super(tableAlias, query);
    }

    public static ElasticSearchAggQuerySql builder(AggQuery query) {
        return new ElasticSearchAggQuerySql("", query);
    }

    public static ElasticSearchAggQuerySql builder(String tableAlias, AggQuery query) {
        return new ElasticSearchAggQuerySql(tableAlias, query);
    }

    // 适配
    protected String groupBy(LinkedHashSet<String> groupFields) {
        if (ICollections.hasElements(groupFields)) {
            if (StringUtils.isNotEmpty(tableAlias)) {
                groupFields = Sets.newLinkedHashSet(
                    groupFields.stream().map(t -> getTableAliasAndDot() + t).collect(Collectors.toList()));
            }
            int size = groupFields.size();
            return new StringBuffer().append(" group by ").append(size < 2 ? "" : "(")
                .append(StringUtils.join(groupFields, ", ")).append(size < 2 ? "" : ")").toString();
        }
        return "";
    }

}
