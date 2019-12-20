package com.info.baymax.dsp.data.platform.service.impl;

import com.info.baymax.dsp.data.platform.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
    public SearchResponse query(Map<String, String> conf) {
        String clusterName = conf.get("clusterName");
        String ipAddresses = conf.get("ipAddresses");
        String index = conf.get("index");
        String indexType = conf.get("indexType");
        Settings settings = Settings.builder()
                .put("cluster.name", clusterName).build();

        TransportClient client = new PreBuiltTransportClient(settings);
        try {
            for (String ipAndPort : ipAddresses.split(",")) {
                String[] ipPort = ipAndPort.split(":");
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipPort[0]), Integer.parseInt(ipPort[1])));
            }
        } catch (UnknownHostException e) {
            log.error("unknown es host", e);
            throw new RuntimeException(e);
        }

        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        SearchResponse response = client.prepareSearch(index).setTypes(indexType).setQuery(queryBuilder).setFrom(1).setSize(1).get();

        client.close();
        return response;
    }
}
