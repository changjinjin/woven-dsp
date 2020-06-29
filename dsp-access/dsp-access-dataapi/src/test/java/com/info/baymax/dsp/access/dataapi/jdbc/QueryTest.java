package com.info.baymax.dsp.access.dataapi.jdbc;

import com.info.baymax.dsp.access.dataapi.data.Query;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcQuery;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.SelectSql;
import org.junit.Test;

public class QueryTest {

    @Test
    public void test() {
        try {
            Query query = Query.builder()//
                .page(2, 10)//
                .allProperties("id", "name", "age", "weight", "height", "remark")//
                .selectProperties("id", "name", "age", "weight")//
                .excludeProperties("weight")//
                .fieldGroup()//
                .andGreaterThan("age", 1, 5 > 1)//
                .andNotEqualTo("name", "zhangsan")//
                .andNotBetween("height", 100, 190)//
                .andIn("id", new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9})//
                .end()//
                .orderByAsc("id")//
                .orderByDesc("age");
            SelectSql sql = SelectSql.build(JdbcQuery.from(query).table("t_user"));
            System.out.println(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
