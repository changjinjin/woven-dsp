package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.record.RecordQuery;
import com.info.baymax.common.queryapi.sql.AggQuerySql;
import com.info.baymax.common.queryapi.sql.RecordQuerySql;
import com.info.baymax.dsp.access.dataapi.data.QueryParser;

public class JdbcQueryParser
        implements QueryParser<JdbcStorageConf, RecordQuery, RecordQuerySql, AggQuery, AggQuerySql> {

    @Override
    public RecordQuerySql parseRecordQuery(JdbcStorageConf conf, RecordQuery query) throws Exception {
        return RecordQuerySql.builder(convertRecordQuery(conf, query));
    }

    @Override
    public RecordQuery convertRecordQuery(JdbcStorageConf conf, RecordQuery query) throws Exception {
        return query.table(conf.getTable());
    }

    @Override
    public AggQuerySql parseAggQuery(JdbcStorageConf conf, AggQuery query) throws Exception {
        return AggQuerySql.builder(convertAggQuery(conf, query));
    }

    @Override
    public AggQuery convertAggQuery(JdbcStorageConf conf, AggQuery query) throws Exception {
        return query.table(conf.getTable());
    }
}
