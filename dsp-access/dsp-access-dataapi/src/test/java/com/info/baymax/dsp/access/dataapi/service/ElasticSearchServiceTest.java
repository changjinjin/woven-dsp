package com.info.baymax.dsp.access.dataapi.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.page.IPageable;
import com.info.baymax.dsp.access.dataapi.DspDataapiStarter;
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
            IPage<MapEntity> page = elasticSearchService.query(conf, new String[]{"id", "ts"},
                IPageable.offset(1, 10));
            System.out.println(JSON.toJSONString(page));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
