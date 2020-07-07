package com.info.baymax.dsp.access.dataapi.essql;

import org.datayoo.moql.MoqlException;
import org.datayoo.moql.sql.SqlDialectType;
import org.datayoo.moql.translator.MoqlTranslator;
import org.junit.Test;

public class EssqlTest {

    @Test
    public void test1() {
        // sql = "select w.skuId, w.name, w.category from commodity w where w.price > 200 and w.skuId between 100 and
        // 200 and w.name in ('zhangsan','lisi') order by w.price limit 1, 10 ";
        try {
//            String sql = "select w.skuId, w.name, w.category, avg(w.price) count_price, avg(w.price) avg_price, avg(w.price) max_price, avg(w.price) min_price, avg(w.price) sum_price from w.commodity where w.price > 200.0  group by w.skuId, w.name, w.category having avg_price  between 0 and 10000  order by max_price ASC limit 1, 3 ";
            String sql = "SELECT brand,category,count(price) count_price,avg(price) avg_price,sum(price) sum_price FROM commodity group by brand,category order by brand";
            System.out.println(MoqlTranslator.translateMoql2Dialect(sql, SqlDialectType.ELASTICSEARCH));
        } catch (MoqlException e) {
            e.printStackTrace();
        }
    }

}
