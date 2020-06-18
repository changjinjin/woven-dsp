package com.info.baymax.data.elasticsearch.jest;

import com.alibaba.fastjson.JSON;
import com.info.baymax.data.elasticsearch.AbstractBootTest;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.IndicesExists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

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
}
