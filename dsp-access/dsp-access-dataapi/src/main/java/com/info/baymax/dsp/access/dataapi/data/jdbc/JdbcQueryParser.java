package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.common.service.criteria.query.RecordQuery;
import com.info.baymax.dsp.access.dataapi.data.QueryParser;
import com.info.baymax.dsp.access.dataapi.data.jdbc.sql.AggQuerySql;
import com.info.baymax.dsp.access.dataapi.data.jdbc.sql.RecordQuerySql;

public class JdbcQueryParser implements QueryParser<JdbcStorageConf, JdbcQuery, RecordQuerySql, AggQuery, AggQuerySql> {

    @Override
    public RecordQuerySql parseRecordQuery(JdbcStorageConf storageConf, RecordQuery query) throws Exception {
        return RecordQuerySql.builder(convertRecordQuery(storageConf, query));
    }

    @Override
    public JdbcQuery convertRecordQuery(JdbcStorageConf storageConf, RecordQuery query) throws Exception {
        return JdbcQuery.from(query).table(storageConf.getTable());
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
