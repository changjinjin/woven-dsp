package com.info.baymax.dsp.access.dataapi.data.jdbc.oracle;

import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcQueryParser;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcStorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.AggQuerySql;

public class OracleJdbcQueryParser extends JdbcQueryParser {

    @Override
    public AggQuerySql parseAggQuery(JdbcStorageConf conf, AggQuery query) throws Exception {
        return OracleAggQuerySql.builder(convertAggQuery(conf, query));
    }

}
