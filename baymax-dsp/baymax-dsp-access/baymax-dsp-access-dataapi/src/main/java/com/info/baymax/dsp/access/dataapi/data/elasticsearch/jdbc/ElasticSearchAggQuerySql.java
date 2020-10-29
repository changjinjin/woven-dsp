package com.info.baymax.dsp.access.dataapi.data.elasticsearch.jdbc;

import com.google.common.collect.Lists;
import com.info.baymax.common.queryapi.query.aggregate.AggField;
import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.sql.AggQuerySql;
import com.info.baymax.common.utils.ICollections;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ElasticSearchAggQuerySql extends AggQuerySql {

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
