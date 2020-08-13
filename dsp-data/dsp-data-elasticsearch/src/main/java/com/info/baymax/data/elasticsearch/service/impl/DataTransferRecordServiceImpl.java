package com.info.baymax.data.elasticsearch.service.impl;

import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.data.elasticsearch.config.EsMetricsIndexProperties;
import com.info.baymax.data.elasticsearch.config.jest.ISearchResult;
import com.info.baymax.data.elasticsearch.entity.DataTransferRecord;
import com.info.baymax.data.elasticsearch.service.DataTransferRecordService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.core.search.aggregation.TermsAggregation.Entry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
@Slf4j
public class DataTransferRecordServiceImpl implements DataTransferRecordService {

    @Autowired
    private JestClient jestClient;
    @Autowired
    private EsMetricsIndexProperties properties;

    private String TYPE_NAME = DataTransferRecord.TYPE_NAME;
    private String INDEX_TYPE = "doc";

    private List<String> getIndies() {
        return properties.getIndies(jestClient);
    }

    @Override
    public List<Map<String, Object>> userVisitTopN(long start, long end, int n, boolean reverse)
        throws ServiceException {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(termQuery("name", TYPE_NAME));
        if (start > 0) {
            queryBuilder.must(rangeQuery("@timestamp").gte(start));
        }
        if (end > 0) {
            queryBuilder.must(rangeQuery("@timestamp").lt(end));
        }

        TermsAggregationBuilder teamAgg = AggregationBuilders.terms("custIdAgg").field("custId.keyword").subAggregation(
            AggregationBuilders.terms("custNameAgg").field("custName.keyword").subAggregation(AggregationBuilders
                .cardinality("distinct_sid").field("sid.keyword").precisionThreshold(Integer.MAX_VALUE)));
        SearchSourceBuilder builder = new SearchSourceBuilder().query(queryBuilder).aggregation(teamAgg).sort("count",
            SortOrder.DESC);
        SearchResult result = null;
        try {
            String query = builder.toString();
            log.debug("jestClient query:" + query);
            result = new ISearchResult(
                jestClient.execute(new Search.Builder(query).addIndices(getIndies()).addType(INDEX_TYPE).build()));
            List<Map<String, Object>> list = new ArrayList<>();
            if (result.isSucceeded()) {
                List<Entry> idAgg = result.getAggregations().getTermsAggregation("custIdAgg").getBuckets();
                Long count = 0L;
                String key = null;
                String key1 = null;
                Map<String, Object> map = null;
                for (Entry entry : idAgg) {
                    key = entry.getKeyAsString();
                    count = entry.getCount();
                    List<Entry> nameAgg = entry.getTermsAggregation("custNameAgg").getBuckets();
                    for (Entry entry1 : nameAgg) {
                        key1 = entry1.getKeyAsString();
                    }
                    map = new HashMap<String, Object>();
                    map.put("custId", key);
                    map.put("custName", key1);
                    map.put("count", count);
                    list.add(map);
                }
            } else {
                log.error("es query failed,errorMessage:" + result.getErrorMessage());
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public List<Map<String, Object>> datasetVisitTopN(long start, long end, int n, boolean reverse) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(termQuery("name", TYPE_NAME));
        if (start > 0) {
            queryBuilder.must(rangeQuery("@timestamp").gte(start));
        }
        if (end > 0) {
            queryBuilder.must(rangeQuery("@timestamp").lt(end));
        }

        TermsAggregationBuilder teamAgg = AggregationBuilders.terms("datasetIdAgg").field("datasetId.keyword")
            .subAggregation(AggregationBuilders.terms("datasetNameAgg").field("datasetName.keyword")
                .subAggregation(AggregationBuilders.cardinality("distinct_sid").field("sid.keyword")
                    .precisionThreshold(Integer.MAX_VALUE)));
        SearchSourceBuilder builder = new SearchSourceBuilder().query(queryBuilder).aggregation(teamAgg).sort("count",
            SortOrder.DESC);
        SearchResult result = null;
        try {
            String query = builder.toString();
            log.debug("jestClient query:" + query);
            result = new ISearchResult(
                jestClient.execute(new Search.Builder(query).addIndices(getIndies()).addType(INDEX_TYPE).build()));
            List<Map<String, Object>> list = new ArrayList<>();
            if (result.isSucceeded()) {
                List<Entry> idAgg = result.getAggregations().getTermsAggregation("datasetIdAgg").getBuckets();
                Long count = 0L;
                String key = null;
                String key1 = null;
                Map<String, Object> map = null;
                for (Entry entry : idAgg) {
                    key = entry.getKeyAsString();
                    count = entry.getCount();
                    List<Entry> nameAgg = entry.getTermsAggregation("datasetNameAgg").getBuckets();
                    for (Entry entry1 : nameAgg) {
                        key1 = entry1.getKeyAsString();
                    }
                    map = new HashMap<String, Object>();
                    map.put("datasetId", key);
                    map.put("datasetName", key1);
                    map.put("count", count);
                    list.add(map);
                }
            } else {
                log.error("es query failed,errorMessage:" + result.getErrorMessage());
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public List<Map<String, Object>> userVisitDatasetTopN(String custId, long start, long end, int n, boolean reverse) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(termQuery("name", TYPE_NAME))
            .must(termQuery("custId.keyword", custId));
        if (start > 0) {
            queryBuilder.must(rangeQuery("@timestamp").gte(start));
        }
        if (end > 0) {
            queryBuilder.must(rangeQuery("@timestamp").lt(end));
        }

        TermsAggregationBuilder teamAgg = AggregationBuilders.terms("datasetIdAgg").field("datasetId.keyword")
            .subAggregation(AggregationBuilders.terms("datasetNameAgg").field("datasetName.keyword")
                .subAggregation(AggregationBuilders.cardinality("distinct_sid").field("sid.keyword")
                    .precisionThreshold(Integer.MAX_VALUE)));
        SearchSourceBuilder builder = new SearchSourceBuilder().query(queryBuilder).aggregation(teamAgg).sort("count",
            SortOrder.DESC);
        SearchResult result = null;
        try {
            String query = builder.toString();
            log.debug("jestClient query:" + query);
            result = new ISearchResult(
                jestClient.execute(new Search.Builder(query).addIndices(getIndies()).addType(INDEX_TYPE).build()));
            List<Map<String, Object>> list = new ArrayList<>();
            if (result.isSucceeded()) {
                List<Entry> idAgg = result.getAggregations().getTermsAggregation("datasetIdAgg").getBuckets();
                Long count = 0L;
                String key = null;
                String key1 = null;
                Map<String, Object> map = null;
                for (Entry entry : idAgg) {
                    key = entry.getKeyAsString();
                    count = entry.getCount();
                    List<Entry> nameAgg = entry.getTermsAggregation("datasetNameAgg").getBuckets();
                    for (Entry entry1 : nameAgg) {
                        key1 = entry1.getKeyAsString();
                    }
                    map = new HashMap<String, Object>();
                    map.put("datasetId", key);
                    map.put("datasetName", key1);
                    map.put("count", count);
                    list.add(map);
                }
            } else {
                log.error("es query failed,errorMessage:" + result.getErrorMessage());
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public List<Map<String, Object>> datasetVisitUserTopN(String datasetId, long start, long end, int n,
                                                          boolean reverse) throws ServiceException {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(termQuery("name", TYPE_NAME))
            .must(termQuery("datasetId.keyword", datasetId));
        if (start > 0) {
            queryBuilder.must(rangeQuery("@timestamp").gte(start));
        }
        if (end > 0) {
            queryBuilder.must(rangeQuery("@timestamp").lt(end));
        }

        TermsAggregationBuilder teamAgg = AggregationBuilders.terms("custIdAgg").field("custId.keyword").subAggregation(
            AggregationBuilders.terms("custNameAgg").field("custName.keyword").subAggregation(AggregationBuilders
                .cardinality("distinct_sid").field("sid.keyword").precisionThreshold(Integer.MAX_VALUE)));
        SearchSourceBuilder builder = new SearchSourceBuilder().query(queryBuilder).aggregation(teamAgg).sort("count",
            SortOrder.DESC);
        SearchResult result = null;
        try {
            String query = builder.toString();
            log.debug("jestClient query:" + query);
            result = new ISearchResult(
                jestClient.execute(new Search.Builder(query).addIndices(getIndies()).addType(INDEX_TYPE).build()));
            List<Map<String, Object>> list = new ArrayList<>();
            if (result.isSucceeded()) {
                List<Entry> idAgg = result.getAggregations().getTermsAggregation("custIdAgg").getBuckets();
                Long count = 0L;
                String key = null;
                String key1 = null;
                Map<String, Object> map = null;
                for (Entry entry : idAgg) {
                    key = entry.getKeyAsString();
                    count = entry.getCount();
                    List<Entry> nameAgg = entry.getTermsAggregation("custNameAgg").getBuckets();
                    for (Entry entry1 : nameAgg) {
                        key1 = entry1.getKeyAsString();
                    }
                    map = new HashMap<String, Object>();
                    map.put("custId", key);
                    map.put("custName", key1);
                    map.put("count", count);
                    list.add(map);
                }
            } else {
                log.error("es query failed,errorMessage:" + result.getErrorMessage());
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public IPage<DataTransferRecord> queryList(String keyword, String transferType, String growthType, String custId,
                                               String datasetId, long start, long end, int pageNum, int pageSize) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(termQuery("name", TYPE_NAME));
        if (StringUtils.isNotEmpty(keyword)) {
            queryBuilder.must(QueryBuilders.boolQuery().should(matchQuery("datasetName", keyword))
                .should(matchQuery("custName", keyword)).should(matchQuery("custAppName", keyword))
                .should(matchQuery("dataServiceName", keyword)));
        }
        if (StringUtils.isNotEmpty(transferType)) {
            queryBuilder.must(termQuery("transferType.keyword", transferType));
        }
        if (StringUtils.isNotEmpty(growthType)) {
            queryBuilder.must(termQuery("growthType.keyword", growthType));
        }
        if (StringUtils.isNotEmpty(custId)) {
            queryBuilder.must(termQuery("custId.keyword", custId));
        }
        if (StringUtils.isNotEmpty(datasetId)) {
            queryBuilder.must(termQuery("datasetId.keyword", datasetId));
        }
        if (start > 0) {
            queryBuilder.must(rangeQuery("@timestamp").gte(start));
        }
        if (end > 0) {
            queryBuilder.must(rangeQuery("@timestamp").lt(end));
        }
        SearchSourceBuilder builder = new SearchSourceBuilder().query(queryBuilder).sort("@timestamp", SortOrder.DESC)
            .from((pageNum - 1) * pageSize).size(pageSize);
        try {
            String query = builder.toString();
            log.debug("jestClient query:" + query);
            SearchResult result = new ISearchResult(
                jestClient.execute(new Search.Builder(query).addIndices(getIndies()).addType(INDEX_TYPE).build()));
            if (result.isSucceeded()) {
                List<Hit<DataTransferRecord, Void>> hits = result.getHits(DataTransferRecord.class);
                return IPage.<DataTransferRecord>of(pageNum, pageSize, result.getTotal(),
                    hits.stream().map(t -> t.source).collect(Collectors.toList()));
            } else {
                log.error("es query failed,errorMessage:" + result.getErrorMessage());
            }
            return IPage.<DataTransferRecord>of(pageNum, pageSize, 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
        }
    }
}
