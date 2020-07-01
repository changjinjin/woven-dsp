package com.info.baymax.dsp.access.dataapi.es;

import org.es.sql.bean.ElasticSqlParseResult;
import org.es.sql.parser.ElasticSql2DslParser;
import org.junit.Assert;
import org.junit.Test;


public class SqlParserLimitTest {
    @Test
    public void testParseLimitExpr() {
        String sql = "select id,status from index.order t where t.status='SUCCESS' limit 5,15";
        ElasticSql2DslParser sql2DslParser = new ElasticSql2DslParser();
        ElasticSqlParseResult parseResult = sql2DslParser.parse(sql);

        Assert.assertEquals(parseResult.getFrom(), 5);
        Assert.assertEquals(parseResult.getSize(), 15);

        System.out.println(parseResult.toDsl());
    }

    @Test
    public void testParseLimitExprWithArgs() {
        String sql = "select id,status from index.order t where t.status='SUCCESS' order by name limit ?,?";
        ElasticSql2DslParser sql2DslParser = new ElasticSql2DslParser();
        ElasticSqlParseResult parseResult = sql2DslParser.parse(sql, new Object[]{5, 15});

        Assert.assertEquals(parseResult.getFrom(), 5);
        Assert.assertEquals(parseResult.getSize(), 15);

        System.out.println(parseResult.toDsl());
    }

    @Test
    public void testX() {
        //String sql = "select $bookCategories from index.library t where t.manager.managerName='lcy'";
        //String sql = "select $bookCategories from index.library where $bookCategories.$books.bookStock > 10";
        //String sql = "select $bookCategories from index.library where $bookCategories.$books.bookPublisher.$bookProvider.providerName='PVD_01'";
        //String sql = "select $bookCategories from index.library where $bookCategories.$books.bookStock > 10 and $bookCategories.$books.bookPublisher.$bookProvider.providerName='PVD_01'";

        String sql = "select * from index.library t query prefix(t.manager.managerName, 'lc') where $bookCategories.$books.bookStock > 10";

        ElasticSql2DslParser sql2DslParser = new ElasticSql2DslParser();
        ElasticSqlParseResult parseResult = sql2DslParser.parse(sql);
        System.out.println(parseResult.toDsl());
    }
}
