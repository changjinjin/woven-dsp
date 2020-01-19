package com.info.baymax.dsp.access.dataapi.service;

import org.elasticsearch.action.search.SearchResponse;

import java.util.Map;

/**
 * @Author: guofeng.wu
 * @Date: 2019/12/20
 */
public interface ElasticSearchService {

    SearchResponse query(Map<String, String> conf, int offset, int size, String[] includes);

}