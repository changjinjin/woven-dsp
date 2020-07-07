package com.info.baymax.dsp.access.dataapi.data.elasticsearch;

import com.info.baymax.common.page.IPageable;
import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.common.service.criteria.query.RecordQuery;
import com.info.baymax.dsp.access.dataapi.data.QueryParser;
import com.info.baymax.dsp.access.dataapi.data.StorageConf;
import com.info.baymax.dsp.access.dataapi.data.elasticsearch.jdbc.ElasticSearchJdbcStorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcQueryParser;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcStorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.AbstractQuerySql;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.AggQuerySql;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.QuerySql;
import io.searchbox.core.Search;
import lombok.extern.slf4j.Slf4j;
import org.datayoo.moql.MoqlException;
import org.datayoo.moql.sql.SqlDialectType;
import org.datayoo.moql.translator.MoqlTranslator;

@Slf4j
public class ElasticsearchQuerySqlParser extends ElasticsearchQueryParser {

    @Override
    public Search parse(ElasticSearchStorageConf storageConf, RecordQuery query) throws Exception {
        AbstractQuerySql<?> selectSql = parseLimit(query.getPageable(),
            parseSql(new ElasticSearchJdbcStorageConf(storageConf), query));
        return new Search.Builder(parseDsl(selectSql)).addIndex(storageConf.getIndex())
            .addType(storageConf.getIndexType()).allowNoIndices(true).ignoreUnavailable(true).build();
    }

    @Override
    public Search parseAgg(ElasticSearchStorageConf storageConf, AggQuery query) throws Exception {
        AbstractQuerySql<?> selectSql = parseLimit(query.getPageable(),
            parseAggSql(new ElasticSearchJdbcStorageConf(storageConf), query));
        return new Search.Builder(parseDsl(selectSql)).addIndex(storageConf.getIndex())
            .addType(storageConf.getIndexType()).allowNoIndices(true).ignoreUnavailable(true).build();
    }

    private String parseDsl(AbstractQuerySql<?> selectSql) throws MoqlException {
        String dsl = MoqlTranslator.translateMoql2Dialect(selectSql.getExecuteSql(), SqlDialectType.ELASTICSEARCH);
        log.debug("query dsl：" + dsl);
        return dsl;
    }

    private AbstractQuerySql<?> parseLimit(IPageable pageable, AbstractQuerySql<?> selectSql) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        selectSql.addParamValues(new Object[]{(int) offset, pageSize});
        selectSql.setPlaceholderSql(selectSql.getExecuteSql() + " limit " + offset + ", " + pageSize);
        log.debug("Execute sql:" + selectSql.getExecuteSql());
        return selectSql;
    }

    public QuerySql parseSql(StorageConf conf, RecordQuery query) throws Exception {
        return QueryParser.getInstance(JdbcQueryParser.class).parse((JdbcStorageConf) conf, query);
    }

    public AggQuerySql parseAggSql(StorageConf conf, AggQuery query) throws Exception {
        return QueryParser.getInstance(JdbcQueryParser.class).parseAgg((JdbcStorageConf) conf, query);
    }
}
