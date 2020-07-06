package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.dsp.access.dataapi.data.QueryParser;
import com.info.baymax.dsp.access.dataapi.data.RecordQuery;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.AggQuerySql;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.QuerySql;

public class JdbcQueryParser implements QueryParser<JdbcStorageConf, JdbcQuery, QuerySql, AggQuery, AggQuerySql> {

    @Override
    public QuerySql parse(JdbcStorageConf storageConf, RecordQuery query) throws Exception {
        return QuerySql.builder("w", convert(storageConf, query));
    }

    @Override
    public JdbcQuery convert(JdbcStorageConf storageConf, RecordQuery query) throws Exception {
        return JdbcQuery.from(query).table(storageConf.getTable());
    }

    @Override
    public AggQuerySql parseAgg(JdbcStorageConf conf, AggQuery query) throws Exception {
        return AggQuerySql.builder("w", convertAgg(conf, query));
    }

    @Override
    public AggQuery convertAgg(JdbcStorageConf conf, AggQuery query) throws Exception {
        return query.table(conf.getTable());
    }

}
