package com.jn.sqlhelper.dialect.elasticsearch;

import com.jn.langx.util.StringMaker;
import com.jn.sqlhelper.dialect.internal.urlparser.CommonUrlParser;
import com.jn.sqlhelper.dialect.internal.urlparser.DefaultDatabaseInfo;
import com.jn.sqlhelper.dialect.internal.urlparser.UnKnownDatabaseInfo;
import com.jn.sqlhelper.dialect.urlparser.DatabaseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElasticsearchUrlParser extends CommonUrlParser {
    private static final String URL_PREFIX = "jdbc:elasticsearch:";
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchUrlParser.class);
    private static final List<String> URL_SCHEMAS = Arrays.asList(new String[]{URL_PREFIX});

    @Override
    public List<String> getUrlSchemas() {
        return URL_SCHEMAS;
    }

    public ElasticsearchUrlParser() {
    }

    @Override
    public DatabaseInfo parse(final String jdbcUrl) {
        if (jdbcUrl == null) {
            logger.info("jdbcUrl may not be null");
            return UnKnownDatabaseInfo.INSTANCE;
        }
        if (!jdbcUrl.startsWith(URL_PREFIX)) {
            logger.info("jdbcUrl has invalid prefix.(url:{}, prefix:{})", (Object) jdbcUrl, (Object) URL_PREFIX);
            return UnKnownDatabaseInfo.INSTANCE;
        }
        DatabaseInfo result = null;
        try {
            result = this.parse0(jdbcUrl);
        } catch (Exception e) {
            logger.info("Elasticsearch JdbcUrl parse error. url: {}, Caused: {}",
                new Object[]{jdbcUrl, e.getMessage(), e});
            result = UnKnownDatabaseInfo.createUnknownDataBase("elasticsearch", jdbcUrl);
        }
        return result;
    }

    private DatabaseInfo parse0(final String jdbcUrl) {
        return this.parseNormal(jdbcUrl);
    }

    private DatabaseInfo parseNormal(final String jdbcUrl) {
        final StringMaker maker = new StringMaker(jdbcUrl);
        maker.after(URL_PREFIX);
        final String host = maker.after("//").before('/').value();
        final List<String> hostList = new ArrayList<String>(1);
        hostList.add(host);
        final String databaseId = maker.next().afterLast('/').before('?').value();
        final String normalizedUrl = maker.clear().before('?').value();
        return new DefaultDatabaseInfo("elasticsearch", jdbcUrl, normalizedUrl, hostList, databaseId);
    }
}
