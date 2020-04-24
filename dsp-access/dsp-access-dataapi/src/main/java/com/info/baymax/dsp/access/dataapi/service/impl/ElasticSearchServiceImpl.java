package com.info.baymax.dsp.access.dataapi.service.impl;

import com.info.baymax.dsp.access.dataapi.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @Author: guofeng.wu
 * @Date: 2019/12/20
 */
@Service
@Slf4j
public class ElasticSearchServiceImpl implements ElasticSearchService {

    @Override
    public SearchResponse query(Map<String, String> conf, int offset, int size, String[] includes) {
        String clusterName = conf.get("clusterName");
        String ipAddresses = conf.get("ipAddresses");
        String index = conf.get("index");
        String indexType = conf.get("indexType");
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();
        System.setProperty("es.set.netty.runtime.available.processors", "false");

        TransportClient client = new PreBuiltTransportClient(settings);
        try {
            for (String ipAndPort : ipAddresses.split(",")) {
                String[] ipPort = ipAndPort.split(":");
                client.addTransportAddress(
                    new InetSocketTransportAddress(InetAddress.getByName(ipPort[0]), 9303));
                //todo dataset 配置es端口
            }
        } catch (UnknownHostException e) {
            log.error("unknown es host", e);
            client.close();
            throw new RuntimeException(e);
        }

        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        String[] excludes = new String[0];

        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource().fetchSource(includes, excludes);

        SearchResponse response = client.prepareSearch(index).setTypes(indexType).setSource(searchSourceBuilder)
            .setQuery(queryBuilder).setFrom(offset).setSize(size).get();

        client.close();
        return response;
    }
}
