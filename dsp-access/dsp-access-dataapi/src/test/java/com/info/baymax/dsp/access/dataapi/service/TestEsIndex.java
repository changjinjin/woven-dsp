package com.info.baymax.dsp.access.dataapi.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestEsIndex {
    // private static String[] indexNames = { "metrics_145-2019-11" };
    // private static String[] indexNames = { "metrics_145-2019-11" };
    // private static String[] indexNames = {"metrics_84-2020-04"};
    // private static String[] indexNames = { "test_es0421" };
    private static String[] indexNames = {"metrics-2020-04"};

    public static void main(String[] args) throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").put("client.transport.sniff", false)
            .build();

        @SuppressWarnings("resource")
        Client client = new PreBuiltTransportClient(settings)
            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

        search(client);

        // deleteIndex(client);
    }

    private static void deleteIndex(Client client) {
        client.admin().indices().prepareDelete(indexNames).execute().actionGet();
    }

    private static void search(Client client) {
        // BoolQueryBuilder queryBuilder = boolQuery();
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        // queryBuilder.must(QueryBuilders.rangeQuery("@timestamp").gt("").lt(endDate).includeLower(true).includeUpper(false));
        // queryBuilder.must(QueryBuilders.termQuery("name", "http_trace"));
        // queryBuilder.must(QueryBuilders.termQuery("type", "http_trace"));
        // queryBuilder.must(termQuery("instance.keyword", "woven-gateway"));

        // queryBuilder.must(termQuery("name", "http_server_requests"));
        // queryBuilder.must(termQuery("name", "datasource_status_checks"));
        // queryBuilder.must(termQuery("loginId", "admin"));
        // queryBuilder.must(termQuery("tenantId", "5b5b00e9-cc95-46ff-9cb3-bdaa2966aad3"));
        // queryBuilder.must(termQuery("tenantName", "tnew"));

        SearchResponse response = client//
            .prepareSearch(indexNames)//
            // .setTypes("doc")//
            .setQuery(queryBuilder)//
            // .addSort("@timestamp", SortOrder.DESC)//
            .setFrom(0)//
            .setSize(10)//
            .execute()//
            .actionGet();

        SearchHits hits = response.getHits();
        long totalHits = hits.getTotalHits();
        System.out.println("totalHits:" + totalHits);

        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            // do something with the SearchHit

            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();

            // 取_source字段值
            String sourceAsString = hit.getSourceAsString(); // 取成json串
            // Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 取成map对象
            // 从map中取字段值
            System.out.println("index:" + index + "  type:" + type + "  id:" + id);
            System.out.println(sourceAsString);
        }
    }

}
