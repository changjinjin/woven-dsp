package com.info.baymax.dsp.access.dataapi.service.impl;

import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.page.IPageable;
import com.info.baymax.dsp.access.dataapi.config.jest.JestClientUtils;
import com.info.baymax.dsp.access.dataapi.config.jest.JestConf;
import com.info.baymax.dsp.access.dataapi.service.ElasticSearchService;
import com.info.baymax.dsp.access.dataapi.service.MapEntity;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.params.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: guofeng.wu
 * @Date: 2019/12/20
 */
@Service
@Slf4j
public class ElasticSearchServiceImpl implements ElasticSearchService {

    @Override
    public IPage<MapEntity> query(Map<String, String> conf, String[] includes, IPageable pageable) {
        JestClient jestClient = null;
        try {
            JestConf jestConf = JestConf.fromMap(conf);
            jestClient = JestClientUtils.jestClient(jestConf);
            SearchResult searchResult = jestClient.execute(new Search.Builder(SearchSourceBuilder.searchSource()
                .query(QueryBuilders.matchAllQuery()).fetchSource(includes, new String[]{}).toString())
                .addIndices(jestConf.getIndices()).addTypes(jestConf.getIndexTypes())
                .setParameter(Parameters.FROM, pageable.getOffset())
                .setParameter(Parameters.SIZE, pageable.getPageSize()).build());
            if (searchResult.isSucceeded()) {
                return IPage.<MapEntity>of(pageable, searchResult.getTotal(),
                    searchResult.getSourceAsObjectList(MapEntity.class, false));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
        } finally {
            if (jestClient != null) {
                try {
                    jestClient.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
                }
            }
        }
        return IPage.<MapEntity>of(pageable, 0, null);
    }
}
