package com.info.baymax.dsp.access.dataapi.data.elasticsearch;

import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.page.IPageable;
import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.dsp.access.dataapi.config.jest.JestClientUtils;
import com.info.baymax.dsp.access.dataapi.config.jest.JestConf;
import com.info.baymax.dsp.access.dataapi.data.*;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ElasticSearchDataReader extends MapEntityDataReader {

    public ElasticSearchDataReader() {
        super(Engine.ELASTICSEARCH);
    }

    @Override
    public boolean supports(StorageConf conf) {
        return (conf instanceof ElasticSearchStorageConf) && super.supports(conf);
    }

    @Override
    public IPage<MapEntity> readRecord(StorageConf conf, RecordQuery query) throws Exception {
        return executeQuery(conf, query.getPageable(), QueryParser.getInstance(ElasticsearchQuerySqlParser.class)
            .parse((ElasticSearchStorageConf) conf, query));
    }

    @Override
    public IPage<MapEntity> readAgg(StorageConf conf, AggQuery query) throws Exception {
        return executeQuery(conf, query.getPageable(), QueryParser.getInstance(ElasticsearchQuerySqlParser.class)
            .parseAgg((ElasticSearchStorageConf) conf, query));

    }

    private IPage<MapEntity> executeQuery(StorageConf conf, IPageable pageable, Search sreach) throws Exception {
        JestClient jestClient = null;
        try {
            JestConf jestConf = JestConf.from((ElasticSearchStorageConf) conf);
            jestClient = JestClientUtils.jestClient(jestConf);
            SearchResult searchResult = new ISearchResult(jestClient.execute(sreach));
            if (searchResult.isSucceeded()) {
                return IPage.<MapEntity>of(pageable, searchResult.getTotal(),
                    searchResult.getSourceAsObjectList(MapEntity.class, false));
            } else {
                log.error("es query failed,errorMessage:" + searchResult.getErrorMessage());
            }
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
