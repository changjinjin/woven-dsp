package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.info.baymax.dsp.access.dataapi.data.Query;
import com.info.baymax.dsp.access.dataapi.data.QueryParser;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.SelectSql;

public class JdbcQueryParser implements QueryParser<JdbcStorageConf, JdbcQuery, SelectSql> {

    private JdbcQueryParser() {
    }

    private static JdbcQueryParser parser = null;

    public static JdbcQueryParser getInstance() {
        if (parser == null) {
            synchronized (JdbcQueryParser.class) {
                if (parser == null) {
                    parser = new JdbcQueryParser();
                }
            }
        }
        return parser;
    }

    @Override
    public SelectSql parse(JdbcStorageConf storageConf, Query query) throws Exception {
        return SelectSql.build(parseQuery(storageConf, query));
    }

    @Override
    public JdbcQuery parseQuery(JdbcStorageConf storageConf, Query query) throws Exception {
        return JdbcQuery.from(query).table(storageConf.getTable());
    }
}
