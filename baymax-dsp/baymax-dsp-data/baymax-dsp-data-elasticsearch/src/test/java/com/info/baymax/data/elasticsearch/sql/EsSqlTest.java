package com.info.baymax.data.elasticsearch.sql;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;
import com.info.baymax.common.utils.JsonUtils;

public class EsSqlTest {
    String url = "http://localhost:9200/_sql?format=json";

    @Test
    public void showTable() {
        String str = OkHttpUtil.postResponseWithParamsInJson(url,
            JsonUtils.toJson(ImmutableMap.<String, Object>of("query", "show tables")));
        System.out.println(str);
    }

    @Test
    public void sql() {
        String str = OkHttpUtil.postResponseWithParamsInJson(url, JsonUtils.toJson(
            ImmutableMap.<String, Object>of("query", "SELECT * FROM commodity order by price desc LIMIT 10")));
        System.out.println(str);
    }

    @Test
    public void sql2() {
        String str = OkHttpUtil.postResponseWithParamsInJson(url,
            JsonUtils.toJson(ImmutableMap.<String, Object>of("query",
                "SELECT * FROM commodity where price > 200 and name like '%10%' order by price desc LIMIT 10")));
        System.out.println(str);
    }

    @Test
    public void sql3() {
        String str = OkHttpUtil.postResponseWithParamsInJson(url,
            JsonUtils.toJson(ImmutableMap.<String, Object>of("query",
                "SELECT * FROM commodity where price > 200 and name like '%10%' LIMIT 10")));
        System.out.println(str);
    }

    @Test
    public void sql4() {
        String str = OkHttpUtil.postResponseWithParamsInJson(url, JsonUtils.toJson(
            ImmutableMap.<String, Object>of("query", "SELECT count(*) as num FROM commodity where price > 200")));
        System.out.println(str);
    }

    @Test
    public void sql5() {
        String str = OkHttpUtil.postResponseWithParamsInJson(url,
            JsonUtils.toJson(ImmutableMap.<String, Object>of("query",
                "SELECT name, count(*) as count, sum(price) as sum FROM commodity group by name")));
        System.out.println(str);
    }
}
