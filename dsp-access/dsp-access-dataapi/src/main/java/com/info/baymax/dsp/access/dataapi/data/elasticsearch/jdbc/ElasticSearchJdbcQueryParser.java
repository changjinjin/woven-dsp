package com.info.baymax.dsp.access.dataapi.data.elasticsearch.jdbc;

import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcQueryParser;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcStorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.sql.AggQuerySql;

public class ElasticSearchJdbcQueryParser extends JdbcQueryParser {

    @Override
    public AggQuerySql parseAggQuery(JdbcStorageConf conf, AggQuery query) throws Exception {
        return ElasticSearchAggQuerySql.builder(convertAggQuery(conf, query));
    }

}
