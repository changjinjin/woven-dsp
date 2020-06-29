package com.amazon.opendistroforelasticsearch.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class JdbcClientTest {

	@Test
	public void test1() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String url = "jdbc:elasticsearch://localhost:9200?format=json&fetchSize=1000";
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
