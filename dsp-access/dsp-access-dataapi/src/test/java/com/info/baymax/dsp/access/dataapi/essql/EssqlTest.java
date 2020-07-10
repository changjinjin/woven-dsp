package com.info.baymax.dsp.access.dataapi.essql;

import org.apache.commons.lang3.StringUtils;
import org.datayoo.moql.MoqlException;
import org.datayoo.moql.sql.SqlDialectType;
import org.datayoo.moql.translator.MoqlTranslator;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.pipeline.bucketselector.BucketSelectorPipelineAggregationBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EssqlTest {

    @Test
    public void test1() {
        // sql = "select w.skuId, w.name, w.category from commodity w where w.price > 200 and w.skuId between 100 and
        // 200 and w.name in ('zhangsan','lisi') order by w.price limit 1, 10 ";
        try {
            // String sql = "select w.skuId, w.name, w.category, avg(w.price) count_price, avg(w.price) avg_price,
            // avg(w.price) max_price, avg(w.price) min_price, avg(w.price) sum_price from w.commodity where w.price >
            // 200.0 group by w.skuId, w.name, w.category having avg_price between 0 and 10000 order by max_price ASC
            // limit 1, 3 ";
            String sql = "SELECT brand,category,count(price) count_price,avg(price) avg_price,sum(price) sum_price FROM commodity group by brand,category order by brand";
            System.out.println(MoqlTranslator.translateMoql2Dialect(sql, SqlDialectType.ELASTICSEARCH));
        } catch (MoqlException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try {
            String sql = "select w.name, w.brand, w.price from commodity w where w.name like '%A%' and w.brand not like '%G%' having price > 1000 order by price ASC limit 0, 500";
            System.out.println(MoqlTranslator.translateMoql2Dialect(sql, SqlDialectType.ELASTICSEARCH));
        } catch (MoqlException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        try {
            String sql = "select w.category, w.name, w.brand, avg(w.price) avg_price, sum(w.price) sum_price, min(w.price) min_price, max(w.price) max_price, count(w.price) count_price from commodity w group by w.category, w.name, w.brand having max_price > 1000 and (min_price < 1000 or min_price >200) order by w.category asc, w.brand asc, min_price ASC, count_price DESC limit 0, 500";
            System.out.println(MoqlTranslator.translateMoql2Dialect(sql, SqlDialectType.ELASTICSEARCH));
        } catch (MoqlException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4() {
        try {
            String having = "max_price > 300 and min_price < 1000";
            if (!StringUtils.isEmpty(having)) {
                String kvRegex = "\\s*(?<key>[^><=!\\s&|]+)\\s*(?<oprator>>=|>|<=|<|==|!=)\\s*(?<value>[^><=!\\s&|]+)\\s*";
                List<String> havingFields = new ArrayList<>();
                Pattern pattern = Pattern.compile(kvRegex);
                Matcher matcher = pattern.matcher(having);
                while (matcher.find()) {
                    havingFields.add(matcher.group("key"));
                }

                // 声明BucketPath，用于后面的bucket筛选
                Map<String, String> bucketsPathsMap = new HashMap<>();
                for (String key : havingFields) {
                    bucketsPathsMap.put(key, key);
                    // 将key前面加上 param.参数
                    having = having.replace(key, "params." + key);
                }
                // 将having语句中的 AND 和 OR 替换为&& 和 || ,painless 脚本只支持程序中的 && 和 || 逻辑判断
                having = having.replace("AND", "&&").replace("and", "&&").replace("OR", "||").replace("or", "||").replace("\\n", " ");

                // 设置脚本
                Script script = new Script(having);

                // 构建bucket选择器
                BucketSelectorPipelineAggregationBuilder bs = PipelineAggregatorBuilders.bucketSelector("having",
                    bucketsPathsMap, script);
                System.out.println(bs.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
