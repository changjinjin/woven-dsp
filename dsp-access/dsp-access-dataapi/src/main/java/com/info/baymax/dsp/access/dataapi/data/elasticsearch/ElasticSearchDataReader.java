package com.info.baymax.dsp.access.dataapi.data.elasticsearch;

import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.page.IPageable;
import com.info.baymax.dsp.access.dataapi.config.jest.JestClientUtils;
import com.info.baymax.dsp.access.dataapi.config.jest.JestConf;
import com.info.baymax.dsp.access.dataapi.data.*;
import io.searchbox.client.JestClient;
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
    public IPage<MapEntity> read(StorageConf conf, Query query) throws DataReadException {
        JestClient jestClient = null;
        IPageable pageable = query.getPageable();
        try {
            JestConf jestConf = JestConf.from((ElasticSearchStorageConf) conf);
            jestClient = JestClientUtils.jestClient(jestConf);
            SearchResult searchResult = jestClient
                .execute(ElasticsearchQueryParser.getInstance().parse((ElasticSearchStorageConf) conf, query));
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
