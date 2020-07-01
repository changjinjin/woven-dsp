package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.info.baymax.dsp.access.dataapi.data.Query;
import com.info.baymax.dsp.access.dataapi.data.QueryParser;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.SelectSql;

public class JdbcQueryParser implements QueryParser<JdbcStorageConf, JdbcQuery, SelectSql> {

    @Override
    public SelectSql parse(JdbcStorageConf storageConf, Query query) throws Exception {
        return SelectSql.build(convert(storageConf, query));
    }

    @Override
    public JdbcQuery convert(JdbcStorageConf storageConf, Query query) throws Exception {
        return JdbcQuery.from(query).table(storageConf.getTable());
    }
}
