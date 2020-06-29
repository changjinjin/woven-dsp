package com.info.baymax.data.elasticsearch.sql;

import org.junit.Test;

import java.sql.*;

public class JdbcClientTest {

	@Test
	public void test1() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String url = "jdbc:elasticsearch://localhost:9200/commodity/_sql?fetchSize=1000";
			con = DriverManager.getConnection(url);
			ps = con.prepareStatement("SELECT * FROM commodity order by price desc LIMIT 10");
			rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnNumber = metaData.getColumnCount();
			for (int i = 1; i <= columnNumber; i++) {
				String columnName = metaData.getColumnName(i);
				int columnType = metaData.getColumnType(i);
				String columnClassName = metaData.getColumnClassName(columnNumber);
				String columnTypeName = metaData.getColumnTypeName(i);
				System.out.println("columnTypeName:" + columnTypeName + ", columnType:" + columnType
						+ ",columnClassName:" + columnClassName + ",columnName:" + columnName + "," + metaData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
