package com.info.baymax.dsp.access.dataapi.client;

import com.info.baymax.data.elasticsearch.config.jest.ISearchResult;
import com.info.baymax.data.elasticsearch.config.jest.JestClientUtils;
import com.info.baymax.data.elasticsearch.config.jest.JestConf;
import com.inforefiner.repackaged.org.apache.curator.shaded.com.google.common.collect.Lists;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

import java.io.IOException;

public class JestClientTest {

    @Test
    public void testEs5() throws IOException {
        String url = "http://192.168.1.85:9203";
        String indexName = "sink_es";
        String indexType = "sink_es";
        search(url, indexName, indexType);
    }

    @Test
    public void testEs6() throws IOException {
        String url = "http://192.168.2.145:9201";
        String indexName = "commodity";
        String indexType = "commodity";
        search(url, indexName, indexType);
    }

    @Test
    public void testEs7() throws IOException {
        // String url = "http://192.168.2.145:9201";
        String url = "http://192.168.2.145:9200";
        String indexName = "commodity";
        String indexType = "commodity";
        search(url, indexName, indexType);
    }

    private void search(String url, String indexName, String indexType) throws IOException {
        JestClient jestClient = jestClient(url);
        String query = QueryBuilders.matchAllQuery().toString().trim();
        System.out.println("query:\r\n" + query);
        SearchResult result = new ISearchResult(
            jestClient.execute(new Search.Builder(query).addIndex(indexName).addType(indexType).build()));
        if (result.isSucceeded()) {
            System.out.println("result to string:" + result.getJsonString());
            System.out.println("total hits:" + result.getTotal());
        } else {
            System.out.println("ErrorMessage:" + result.getErrorMessage());
        }
    }

    private JestClient jestClient(String url) {
        return JestClientUtils.jestClient(JestConf.builder().uris(Lists.newArrayList(url)).build());
    }
}
