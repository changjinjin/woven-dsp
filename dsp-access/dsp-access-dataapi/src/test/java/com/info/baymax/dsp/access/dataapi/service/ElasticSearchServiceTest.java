package com.info.baymax.dsp.access.dataapi.service;

import com.google.common.collect.Maps;
import com.info.baymax.dsp.access.dataapi.DspDataapiStarter;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DspDataapiStarter.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ElasticSearchServiceTest {
    @Autowired
    private ElasticSearchService elasticSearchService;

    @Test
    public void search() {
        try {
            Map<String, String> conf = Maps.newHashMap();
            conf.put("clusterName", "elasticsearch");
            conf.put("index", "test_es0421");
            conf.put("indexType", "test_es0421");
            conf.put("ipAddresses", "192.168.1.85:9303");
            // conf.put("client.transport.sniff", "false");
            // conf.put("xpack.security.user", "elastic:changeme");
            SearchResponse query = elasticSearchService.query(conf, 0, 10, new String[]{"id", "ts"});
            SearchHit[] hits = query.getHits().getHits();
            for (SearchHit searchHit : hits) {
                System.out.println(searchHit.getSourceAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
