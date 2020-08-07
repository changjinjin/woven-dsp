package com.info.baymax.dsp.access.dataapi.data.jdbc.oracle;

import com.info.baymax.common.page.IPage;
import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.dsp.access.dataapi.data.MapEntity;
import com.info.baymax.dsp.access.dataapi.data.QueryParser;
import com.info.baymax.dsp.access.dataapi.data.StorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.AbstractJdbcDataReader;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcStorageConf;
import com.info.baymax.dsp.data.consumer.beans.source.DBType;
import org.springframework.stereotype.Component;

@Component
public class OracleDataReader extends AbstractJdbcDataReader {
    public OracleDataReader() {
        super(DBType.OracleThin);
    }

    @Override
    public IPage<MapEntity> readAgg(StorageConf conf, AggQuery query) throws Exception {
        return executeQuery(conf, query.getPageable(),
            QueryParser.getInstance(OracleJdbcQueryParser.class).parseAggQuery((JdbcStorageConf) conf, query));
    }
}
