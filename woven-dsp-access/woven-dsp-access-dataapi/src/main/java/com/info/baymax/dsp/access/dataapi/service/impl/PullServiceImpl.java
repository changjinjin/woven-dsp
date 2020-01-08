package com.info.baymax.dsp.access.dataapi.service.impl;

import com.info.baymax.dsp.access.dataapi.service.ElasticSearchService;
import com.info.baymax.dsp.access.dataapi.service.Engine;
import com.info.baymax.dsp.access.dataapi.service.PullService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: guofeng.wu
 * @Date: 2020/1/8
 */
@Service
@Slf4j
public class PullServiceImpl implements PullService {

    @Autowired
    ElasticSearchService elasticSearchService;

    @Override
    public List<Map<String, Object>> query(String storage, Map<String, String> fieldMap, Map<String, String> conf,
                                           int offset, int size, String[] includes) {
        List<Map<String, Object>> res = new ArrayList<>();
        switch (Engine.valueOf(storage.toUpperCase())) {
            case ELASTICSEARCH:
                SearchResponse response = elasticSearchService.query(conf, offset, size, includes);
                SearchHit[] searchHits = response.getHits().getHits();
                for (SearchHit hit : searchHits) {
                    Map<String, Object> src = hit.getSourceAsMap();
                    Map<String, Object> target = new HashMap<>();
                    for (String key : src.keySet()) {
                        target.put(fieldMap.get(key), src.get(key));
                    }
                    res.add(target);
                }
                return res;
            default:
                return null;
        }
    }
}
