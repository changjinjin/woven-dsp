package com.info.baymax.dsp.access.dataapi.essql;

import org.es.sql.bean.ElasticSqlParseResult;
import org.es.sql.parser.ElasticSql2DslParser;
import org.junit.Test;

public class EssqlTest {

    @Test
    public void test1() throws Exception {
        String sql = "select skuId, name, category, avg(price) count_price, avg(price) avg_price, avg(price) max_price, avg(price) min_price, avg(price) sum_price from commodity where  price > ?  group by skuId, name, category having  avg_price  between ? and ?  order by max_price ASC limit ?, ? ";
        Object[] params = new Object[]{200.0, 0, 10000, 0, 3};

        ElasticSql2DslParser sql2DslParser = new ElasticSql2DslParser();
        ElasticSqlParseResult parseResult = sql2DslParser.parse(sql, params);
        System.out.println(parseResult.toDsl());
    }

}
