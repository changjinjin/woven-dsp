package com.info.baymax.common.queryapi;

import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.aggregate.AggType;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.queryapi.query.record.RecordQuery;
import com.info.baymax.common.queryapi.sql.AggQuerySql;
import com.info.baymax.common.queryapi.sql.RecordQuerySql;
import org.junit.Test;

import java.util.Random;

public class QueryApiTest {

    @Test
    public void testQuery() {
        try {
            RecordQuery query = RecordQuery.builder()//
                    .page(2, 10)//
                    .allProperties("id", "name", "age", "weight", "height", "remark")//
                    .selectProperties("id", "name", "age", "weight")//
                    .excludeProperties("weight")//
                    .fieldGroup(FieldGroup.builder()//
                            .andGroup(FieldGroup.builder().andEqualTo("name", "1").orNotEqualToIfNotNull("name", "2"))//
                            .andGreaterThan("age", 1, 5 > 1)//
                            .andNotEqualTo("name", "zhangsan")//
                            .andNotBetween("height", 100, 190)//
                            .andIn("id", new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 })//
                    ).orderByAsc("id")//
                    .orderByDesc("age");
            RecordQuerySql sql = RecordQuerySql.builder(query.table("t_user"));
            System.out.println(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAggQuery() {
        try {
            Random ran = new Random();
            AggQuery query = AggQuery.builder();
            query.table("t_user")//
                    .page(2, 10)//
                    // .allProperties("id","gender","nation", "name", "age", "weight", "height",
                    // "remark")//
                    // .selectProperties("id", "name", "age", "weight")//
                    // .excludeProperties("weight")//
                    .fieldGroup(FieldGroup.builder()//
                            .andGreaterThan("age", 1, ran.nextBoolean())//
                            .andNotBetween("height", 100, 190, ran.nextBoolean())//
                            .andBetween("id", 1, 10000)//
                    )//
                    .aggField("age", AggType.COUNT)//
                    .aggField("age", AggType.AVG, true)//
                    .aggField("age", AggType.MAX)//
                    .aggField("age", AggType.MIN)//
                    .aggField("age", AggType.SUM)//
                    .groupFields("gender", "nation")//
                    .havingFieldGroup(FieldGroup.builder().andGreaterThan("sum_age", 100))//
                    .orderByDesc("count_age", "sum_age");

            AggQuerySql sql = AggQuerySql.builder(query);
            System.out.println(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
