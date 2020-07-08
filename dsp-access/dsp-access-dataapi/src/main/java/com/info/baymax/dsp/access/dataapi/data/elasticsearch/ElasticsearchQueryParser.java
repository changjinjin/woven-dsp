package com.info.baymax.dsp.access.dataapi.data.elasticsearch;

import com.google.common.collect.Lists;
import com.info.baymax.common.mybatis.mapper.example.Example.CriteriaItem;
import com.info.baymax.common.page.IPageable;
import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.common.service.criteria.query.RecordQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.access.dataapi.data.QueryParser;
import io.searchbox.core.Search;
import io.searchbox.core.search.sort.Sort;
import io.searchbox.core.search.sort.Sort.Sorting;
import io.searchbox.params.Parameters;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ElasticsearchQueryParser
    implements QueryParser<ElasticSearchStorageConf, ElasticsearchQuery, Search, AggQuery, Search> {

    @Override
    public Search parse(ElasticSearchStorageConf storageConf, RecordQuery query) throws Exception {
        IPageable pageable = query.getPageable();
        return new Search.Builder(searchBuilder(query.getFinalSelectProperties(""), query.getFieldGroup()))
            .addIndices(Arrays.asList(storageConf.getIndex()))//
            .addTypes(Arrays.asList(storageConf.getIndexType()))//
            .addSort(sorts(query.getOrdSort()))//
            .setParameter(Parameters.FROM, pageable.getOffset())
            .setParameter(Parameters.SIZE, pageable.getPageSize()).build();
    }

    @Override
    public ElasticsearchQuery convert(ElasticSearchStorageConf storageConf, RecordQuery query) throws Exception {
        return ElasticsearchQuery.from(query).clusterName(storageConf.getClusterName()).indices(storageConf.getIndex())
            .indexType(storageConf.getIndexType());
    }

    @Override
    public Search parseAgg(ElasticSearchStorageConf storageConf, AggQuery query) throws Exception {
        return null;
    }

    @Override
    public AggQuery convertAgg(ElasticSearchStorageConf storageConf, AggQuery query) throws Exception {
        return query.table(storageConf.getIndex());
    }

    private String searchBuilder(List<String> includes, FieldGroup fieldGroup) {
        String[] includeFields = null;
        if (ICollections.hasElements(includes)) {
            includeFields = includes.stream().toArray(String[]::new);
        }

        QueryBuilder queryBuilder = null;
        if (fieldGroup == null || ICollections.hasNoElements(fieldGroup.ordItems())) {
            queryBuilder = QueryBuilders.matchAllQuery();
        } else {
            queryBuilder = boolQueryBuilder(fieldGroup);
        }
        return SearchSourceBuilder.searchSource().query(queryBuilder).fetchSource(includeFields, null).toString();
    }

    private BoolQueryBuilder boolQueryBuilder(FieldGroup fieldGroup) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // TODO parse fieldGroup to a BoolQueryBuilder
        List<CriteriaItem> ordItems = fieldGroup.ordItems();
        // if (start > 0) {
        // queryBuilder.must(rangeQuery("@timestamp").gte(start));
        // }
        // if (end > 0) {
        // queryBuilder.must(rangeQuery("@timestamp").lt(end));
        // }
        return boolQuery;
    }

    private List<Sort> sorts(Collection<com.info.baymax.common.service.criteria.field.Sort> sorts) {
        if (ICollections.hasElements(sorts)) {
            return sorts.stream().map(t -> new Sort(t.getName(), Sorting.valueOf(t.getOrder().name().toUpperCase())))
                .collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }
}
