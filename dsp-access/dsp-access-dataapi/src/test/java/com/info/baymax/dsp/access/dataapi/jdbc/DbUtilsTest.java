package com.info.baymax.dsp.access.dataapi.jdbc;

import com.info.baymax.common.utils.DataBaseUtil;
import com.info.baymax.dsp.access.dataapi.data.MapEntity;
import com.info.baymax.dsp.access.dataapi.data.jdbc.MapEntityListHandler;
import com.jn.langx.util.collection.Collects;
import com.jn.sqlhelper.apachedbutils.QueryRunner;
import com.jn.sqlhelper.dialect.pagination.PagingRequest;
import com.jn.sqlhelper.dialect.pagination.PagingResult;
import com.jn.sqlhelper.dialect.pagination.SqlPaginations;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class DbUtilsTest {

    @Test
    public void testSnowball() throws Exception {
        Connection conn = DataBaseUtil.getConnection("com.inforefiner.snowball.SnowballDriver",
            "jdbc:snowball://192.168.1.153:8123/replica_1?socket_timeout=3000000", "default", "", null, null);
        QueryRunner runner = new QueryRunner();

        long totalrecord = runner.query(conn, "select count(*) from ontime_local", new ScalarHandler<BigInteger>(1))
            .longValue();
        System.out.println(totalrecord);
        List<MapEntity> list = runner.query(conn,
            "select * from ontime_local where DayOfMonth = ? limit 10 order by FlightNum desc",
            new MapEntityListHandler(), 5);
        System.out.println(list);
    }

    @Test
    public void testEs() throws Exception {
        String url = "jdbc:elasticsearch://192.168.2.145:9200";
        Class.forName("com.amazon.opendistroforelasticsearch.jdbc.Driver");
        Properties properties = new Properties();
        // properties.put("useSSL", "false");
        // properties.put("trustSelfSigned", "true");
        properties.put("hostnameVerification", "false");
        properties.put("user", "admin");
        properties.put("password", "admin");

        Connection conn = DriverManager.getConnection(url, properties);
        QueryRunner runner = new QueryRunner();
        long totalrecord = runner.query(conn, "select count(*) from commodity", new ScalarHandler<Integer>(1))
            .longValue();
        System.out.println(totalrecord);
        // List<MapEntity> list = runner.query(conn, "select * from commodity order by price desc",
        // new MapEntityListHandler());
        List<MapEntity> list = runner.query(conn,
            "select skuId, name, count(*) count_price, MAX(price) max_price from commodity GROUP BY (skuId,name)",
            new MapEntityListHandler());
        System.out.println(list);
    }


    @Test
    public void test1() throws SQLException {
        PagingRequest<?, MapEntity> request = SqlPaginations.preparePagination(1, 10, "FlightNum");
        String sql = "select * from ontime_local where DayOfMonth = ? and  FlightNum <= ?";
        List<Object> params = Collects.emptyArrayList();
        params.add(5);
        params.add(3340);

        QueryRunner runner = new QueryRunner();
        List<MapEntity> list = runner.query(sql, new MapEntityListHandler(), Collects.toArray(params));
        System.out.println(list);
        PagingResult<MapEntity> result = request.getResult();
        System.out.println(result);
    }

}
