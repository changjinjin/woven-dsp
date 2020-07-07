package com.info.baymax.data.elasticsearch.jest;

import com.alibaba.fastjson.JSON;
import com.info.baymax.data.elasticsearch.AbstractBootTest;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.indices.IndicesExists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JestClientTest extends AbstractBootTest {
    @Autowired
    private JestClient jestClient;

    @Test
    public void test1() throws IOException {
        JestResult jestResult = jestClient.execute(new IndicesExists.Builder("metrics-2020-06").build());
        Object indexFound = jestResult.getValue("found");
        System.out.println(jestResult + "_" + indexFound);
        System.out.println(JSON.toJSONString(jestResult));
    }

    @Test
    public void test2() throws IOException {
        String matchAll = "{ \"query\": { \"match_all\": {} } }";
        SearchResult result = jestClient
            .execute(new Search.Builder(matchAll).addIndex("commodity").addType("commodity").build());
        if (result.isSucceeded()) {
            List<Map> list = result.getSourceAsObjectList(Map.class, true);
            System.out.println(list);
        } else {
            List<Map> list = result.getSourceAsObjectList(Map.class, true);
            System.out.println(list);
        }
    }

    @Test
    public void test3() throws IOException {
        String query = "{\r\n" +
            "  \"size\": 0,\r\n" +
            "  \"query\": {\r\n" +
            "    \"match_all\": {}\r\n" +
            "  },\r\n" +
            "  \"aggs\": {\r\n" +
            "    \"brand\": {\r\n" +
            "      \"terms\": {\r\n" +
            "        \"field\": \"brand.keyword\",\r\n" +
            "        \"order\": [\r\n" +
            "          {\r\n" +
            "            \"_term\": \"asc\"\r\n" +
            "          }\r\n" +
            "        ]\r\n" +
            "      },\r\n" +
            "      \"aggs\": {\r\n" +
            "        \"category\": {\r\n" +
            "          \"terms\": {\r\n" +
            "            \"field\": \"category.keyword\"\r\n" +
            "          },\r\n" +
            "          \"aggs\": {\r\n" +
            "            \"avg_price\": {\r\n" +
            "              \"avg\": {\r\n" +
            "                \"field\": \"price\"\r\n" +
            "              }\r\n" +
            "            },\r\n" +
            "            \"sum_price\": {\r\n" +
            "              \"sum\": {\r\n" +
            "                \"field\": \"price\"\r\n" +
            "              }\r\n" +
            "            }\r\n" +
            "          }\r\n" +
            "        }\r\n" +
            "      }\r\n" +
            "    }\r\n" +
            "  }\r\n" +
            "}\r\n" +
            "";

        SearchResult result = jestClient
            .execute(new Search.Builder(query).addIndex("commodity").addType("commodity").build());
        if (result.isSucceeded()) {
            System.out.println(result);
            MetricAggregation aggregations = result.getAggregations();
            List<Map> list = result.getSourceAsObjectList(Map.class, true);
            System.out.println(list);
        } else {
            System.out.println(result.getErrorMessage());
        }
    }
}
