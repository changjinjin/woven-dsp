package com.info.baymax.dsp.access.dataapi.data.elasticsearch.jdbc;

import com.info.baymax.common.page.IPage;
import com.info.baymax.dsp.access.dataapi.data.DataReadException;
import com.info.baymax.dsp.access.dataapi.data.MapEntity;
import com.info.baymax.dsp.access.dataapi.data.Query;
import com.info.baymax.dsp.access.dataapi.data.StorageConf;
import com.info.baymax.dsp.access.dataapi.data.elasticsearch.ElasticSearchStorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.AbstractJdbcDataReader;
import com.info.baymax.dsp.data.consumer.beans.source.DBType;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchJdbcDataReader extends AbstractJdbcDataReader {
    public ElasticSearchJdbcDataReader() {
        super(DBType.Elasticsearch);
    }

    @Override
    public boolean supports(StorageConf conf) {
        return (conf instanceof ElasticSearchStorageConf) && DBType.Elasticsearch.name()
            .equals((new ElasticSearchJdbcStorageConf((ElasticSearchStorageConf) conf)).getDBType());
    }

    @Override
    public IPage<MapEntity> read(StorageConf conf, Query query) throws DataReadException {
        return super.read(new ElasticSearchJdbcStorageConf((ElasticSearchStorageConf) conf), query);
    }

    // 优先级要高（低）于ElasticSearchDataReader
    @Override
    public int getOrder() {
        return 1;
    }

}
