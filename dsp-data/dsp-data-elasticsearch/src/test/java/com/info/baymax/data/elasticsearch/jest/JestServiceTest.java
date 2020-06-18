package com.info.baymax.data.elasticsearch.jest;

import com.alibaba.fastjson.JSON;
import com.info.baymax.data.elasticsearch.AbstractBootTest;
import com.info.baymax.data.elasticsearch.repository.MetricsEntity;
import com.info.baymax.data.elasticsearch.service.JestService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.indices.IndicesExists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class JestServiceTest extends AbstractBootTest {
    @Autowired
    private JestService jestService;
    @Autowired
    private JestClient jestClient;

    @Test
    public void test() {
        List<Hit<MetricsEntity, Void>> list = jestService.searchAll("metrics-entity", MetricsEntity.class);
        for (Hit<MetricsEntity, Void> hit : list) {
            System.out.println(hit.source);
        }
    }

    @Test
    public void test1() throws IOException {
        JestResult jestResult = jestClient.execute(new IndicesExists.Builder("metrics-2020-06").build());
        Object indexFound = jestResult.getValue("found");
        System.out.println(jestResult + "_" + indexFound);
        System.out.println(JSON.toJSONString(jestResult));
    }
}
