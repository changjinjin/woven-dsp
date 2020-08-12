package com.info.baymax.dsp.access.dataapi.data.elasticsearch.jdbc;

import com.google.common.collect.Lists;
import com.info.baymax.common.service.criteria.agg.AggField;
import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.access.dataapi.data.jdbc.sql.AggQuerySql;

import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@ToString(callSuper = true)
public class ElasticSearchAggQuerySql extends AggQuerySql {
    private static final long serialVersionUID = 9076946654365840665L;

    protected ElasticSearchAggQuerySql(AggQuery query) {
        super(query);
    }

    public static ElasticSearchAggQuerySql builder(AggQuery query) {
        return new ElasticSearchAggQuerySql(query);
    }

    @Override
    protected List<String> getSelectProperties(AggQuery query) {
        List<String> selects = Lists.newArrayList();
        LinkedHashSet<String> groupFields = query.getGroupFields();
        if (ICollections.hasElements(groupFields)) {
            selects.add("*");
        }
        LinkedHashSet<AggField> aggFields = query.getAggFields();
        if (ICollections.hasElements(aggFields)) {
            selects.addAll(aggFields.stream().map(t -> t.toString()).collect(Collectors.toList()));
        }
        return selects;
    }

}
