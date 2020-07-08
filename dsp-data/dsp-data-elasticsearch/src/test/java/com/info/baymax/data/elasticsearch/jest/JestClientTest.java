package com.info.baymax.data.elasticsearch.jest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.data.elasticsearch.AbstractBootTest;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.indices.IndicesExists;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
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
        String query = "{\r\n" + "  \"size\": 0,\r\n" + "  \"query\": {\r\n" + "    \"match_all\": {}\r\n" + "  },\r\n"
            + "  \"aggs\": {\r\n" + "    \"brand\": {\r\n" + "      \"terms\": {\r\n"
            + "        \"field\": \"brand.keyword\",\r\n" + "        \"order\": [\r\n" + "          {\r\n"
            + "            \"_term\": \"asc\"\r\n" + "          }\r\n" + "        ]\r\n" + "      },\r\n"
            + "      \"aggs\": {\r\n" + "        \"category\": {\r\n" + "          \"terms\": {\r\n"
            + "            \"field\": \"category.keyword\"\r\n" + "          },\r\n" + "          \"aggs\": {\r\n"
            + "            \"avg_price\": {\r\n" + "              \"avg\": {\r\n"
            + "                \"field\": \"price\"\r\n" + "              }\r\n" + "            },\r\n"
            + "            \"sum_price\": {\r\n" + "              \"sum\": {\r\n"
            + "                \"field\": \"price\"\r\n" + "              }\r\n" + "            }\r\n"
            + "          }\r\n" + "        }\r\n" + "      }\r\n" + "    }\r\n" + "  }\r\n" + "}\r\n" + "";

        SearchResult result = jestClient
            .execute(new Search.Builder(query).addIndex("commodity").addType("commodity").build());
        if (result.isSucceeded()) {
            System.out.println(result);
            MetricAggregation aggregations = result.getAggregations();
        } else {
            System.out.println(result.getErrorMessage());
        }
    }

    @Test
    public void parseAggregation() {
        // String jsonString = result.getJsonString();
        String jsonString = "{\r\n" + "    \"took\": 1,\r\n" + "    \"timed_out\": false,\r\n"
            + "    \"_shards\": {\r\n" + "        \"total\": 5,\r\n" + "        \"successful\": 5,\r\n"
            + "        \"skipped\": 0,\r\n" + "        \"failed\": 0\r\n" + "    },\r\n" + "    \"hits\": {\r\n"
            + "        \"total\": {\r\n" + "            \"value\": 8,\r\n" + "            \"relation\": \"eq\"\r\n"
            + "        },\r\n" + "        \"max_score\": null,\r\n" + "        \"hits\": []\r\n" + "    },\r\n"
            + "    \"aggregations\": {\r\n" + "        \"brand\": {\r\n"
            + "            \"doc_count_error_upper_bound\": 0,\r\n" + "            \"sum_other_doc_count\": 0,\r\n"
            + "            \"buckets\": [\r\n" + "                {\r\n"
            + "                    \"key\": \"华为\",\r\n" + "                    \"doc_count\": 5,\r\n"
            + "                    \"category\": {\r\n"
            + "                        \"doc_count_error_upper_bound\": 0,\r\n"
            + "                        \"sum_other_doc_count\": 0,\r\n"
            + "                        \"buckets\": [\r\n" + "                            {\r\n"
            + "                                \"key\": \"102\",\r\n"
            + "                                \"doc_count\": 5,\r\n"
            + "                                \"avg_price\": {\r\n"
            + "                                    \"value\": 3200\r\n" + "                                },\r\n"
            + "                                \"sum_price\": {\r\n"
            + "                                    \"value\": 16000\r\n" + "                                }\r\n"
            + "                            }\r\n" + "                        ]\r\n" + "                    }\r\n"
            + "                },\r\n" + "                {\r\n" + "                    \"key\": \"百草味\",\r\n"
            + "                    \"doc_count\": 1,\r\n" + "                    \"category\": {\r\n"
            + "                        \"doc_count_error_upper_bound\": 0,\r\n"
            + "                        \"sum_other_doc_count\": 0,\r\n"
            + "                        \"buckets\": [\r\n" + "                            {\r\n"
            + "                                \"key\": \"101\",\r\n"
            + "                                \"doc_count\": 1,\r\n"
            + "                                \"avg_price\": {\r\n"
            + "                                    \"value\": 120\r\n" + "                                },\r\n"
            + "                                \"sum_price\": {\r\n"
            + "                                    \"value\": 120\r\n" + "                                }\r\n"
            + "                            }\r\n" + "                        ]\r\n" + "                    }\r\n"
            + "                },\r\n" + "                {\r\n" + "                    \"key\": \"良品铺子\",\r\n"
            + "                    \"doc_count\": 2,\r\n" + "                    \"category\": {\r\n"
            + "                        \"doc_count_error_upper_bound\": 0,\r\n"
            + "                        \"sum_other_doc_count\": 0,\r\n"
            + "                        \"buckets\": [\r\n" + "                            {\r\n"
            + "                                \"key\": \"101\",\r\n"
            + "                                \"doc_count\": 2,\r\n"
            + "                                \"avg_price\": {\r\n"
            + "                                    \"value\": 780\r\n" + "                                },\r\n"
            + "                                \"sum_price\": {\r\n"
            + "                                    \"value\": 1560\r\n" + "                                }\r\n"
            + "                            }\r\n" + "                        ]\r\n" + "                    }\r\n"
            + "                }\r\n" + "            ]\r\n" + "        }\r\n" + "    }\r\n" + "}";
        JSONObject json = JSON.parseObject(jsonString, JSONObject.class);
        JSONObject aggregations = json.getJSONObject("aggregations");
        List<Map<String, Object>> newArrayList = Lists.newArrayList();
        parse(aggregations,
            Arrays.asList(AggField.builder().aggType(AggType.COUNT).name("price").build(),
                AggField.builder().aggType(AggType.AVG).name("price").build(),
                AggField.builder().aggType(AggType.SUM).name("price").build()),
            Arrays.asList("brand", "category"), newArrayList, Maps.newHashMap());
        System.out.println(newArrayList);
    }

    public void parse(JSONObject json, List<AggField> aggFields, List<String> groupFields,
                      List<Map<String, Object>> result, Map<String, Object> map) {
        JSONObject m = null;
        String g = null;

        for (String group : groupFields) {
            m = json.getJSONObject(group);
            if (m != null) {
                g = group;
                break;
            }
        }

        if (m != null) {
            JSONArray buckets = m.getJSONArray("buckets");
            if (ICollections.hasElements(buckets)) {
                for (Object object : buckets) {
                    JSONObject item = (JSONObject) object;
                    map.put(g, item.getString("key"));
                    parse(item, aggFields, groupFields, result, map);
                }
            }
        } else {
            Map<String, Object> newMap = new HashMap<String, Object>();
            newMap.putAll(map);
            for (AggField aggField : aggFields) {
                newMap.put(aggField.getAlias(), getvalue(json, aggField.getAggType(), aggField.getAlias()));
            }
            result.add(newMap);
        }
    }

    // 取值
    public Object getvalue(JSONObject json, AggType aggType, String alias) {
        switch (aggType) {
            case COUNT:
                return json.get("doc_count");
            default:
                JSONObject jsonObject = json.getJSONObject(alias);
                if (jsonObject == null) {
                    return 0;
                }
                return jsonObject.get("value");
        }
    }
}
