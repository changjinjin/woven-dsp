package com.info.baymax.data.elasticsearch.sql;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

public class EsSqlTest {
    String url = "http://localhost:9200/_sql?format=json";

    @Test
    public void showTable() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", "show tables");
        String str = OkHttpUtil.postResponseWithParamsInJson(url, jsonObject.toJSONString());
        System.out.println(str);
    }

    @Test
    public void sql() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", "SELECT * FROM commodity order by price desc LIMIT 10");
        String str = OkHttpUtil.postResponseWithParamsInJson(url, jsonObject.toJSONString());
        System.out.println(str);
    }

    @Test
    public void sql2() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query",
            "SELECT * FROM commodity where price > 200 and name like '%10%' order by price desc LIMIT 10");
        String str = OkHttpUtil.postResponseWithParamsInJson(url, jsonObject.toJSONString());
        System.out.println(str);
    }

    @Test
    public void sql3() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", "SELECT * FROM commodity where price > 200 and name like '%10%' LIMIT 10");
        String str = OkHttpUtil.postResponseWithParamsInJson(url, jsonObject.toJSONString());
        System.out.println(str);
    }

    @Test
    public void sql4() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", "SELECT count(*) as num FROM commodity where price > 200");
        String str = OkHttpUtil.postResponseWithParamsInJson(url, jsonObject.toJSONString());
        System.out.println(str);
    }

    @Test
    public void sql5() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", "SELECT name, count(*) as count, sum(price) as sum FROM commodity group by name");
        String str = OkHttpUtil.postResponseWithParamsInJson(url, jsonObject.toJSONString());
        System.out.println(str);
    }
}
