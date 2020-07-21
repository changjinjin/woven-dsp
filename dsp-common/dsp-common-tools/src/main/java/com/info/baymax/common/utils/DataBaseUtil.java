package com.info.baymax.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.*;

/**
 * Created by DebugSy on 2018/5/17.
 */
@Slf4j
public class DataBaseUtil {

    public static DBConf DB_MYSQL = new DBConf(true, "?", "&", false, true, false, false, "com.mysql.jdbc.Driver");
    public static DBConf DB_Teradata = new DBConf(true, ",", ",", false, true, false, false, "com.teradata.jdbc.TeraDriver");
    public static DBConf DB_PostSQL = new DBConf(true, "?", "&", false, true, false, false, "org.postgresql.Driver");
    public static DBConf DB_MsSQL = new DBConf(true, ";", ";", false, true, false, false, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
    public static DBConf DB_Sybase = new DBConf(false, ";", ";", true, true, false, false, "net.sourceforge.jtds.jdbc.Driver");
    public static DBConf DB_HSQLDB = new DBConf(true, ";", ";", false, true, false, false, "org.hsqldb.jdbcDriver");
    public static DBConf DB_Greenplum = new DBConf(true, ";", ";", false, true, false, false, "com.pivotal.jdbc.GreenplumDriver");
    public static DBConf DB_ODBCBridge = new DBConf(false, ";", ";", false, true, false, false, "sun.jdbc.odbc.JdbcOdbcDriver");
    public static DBConf DB_Informix = new DBConf(true, ":", ";", false, true, false, false, "com.informix.jdbc.IfxDriver");
    public static DBConf DB_Oracle = new DBConf(false, ":", ";", false, false, false, true, "oracle.jdbc.driver.OracleDriver");
    public static DBConf DB_ONE = new DBConf(false, ":", ";", false, false, false, true, "com.intple.dbone.Driver");
    public static DBConf DB_DB2 = new DBConf(false, ":", ";", false, false, false, true, "com.ibm.db2.jcc.DB2Driver");
    public static DBConf DB_Kingbase = new DBConf(false, ":", ";", false, false, false, true, "com.kingbase.Driver");
    public static DBConf DB_Kingbase8 = new DBConf(false, ":", ";", false, false, false, true, "com.kingbase8.Driver");
    public static DBConf DB_SNOW_BALL = new DBConf(true, "?", "&", false, true, false, false, "com.inforefiner.snowball.SnowballDriver");
    public static DBConf DB_Elasticsearch = new DBConf(false, "?", "&", true, false, true, false, "com.amazon.opendistroforelasticsearch.jdbc.Driver");
    public static DBConf DB_DMbase = new DBConf(false, ":", ";", false, false, false, true, "dm.jdbc.driver.DmDriver");

    public static DBConf[] dbs = {DB_MYSQL, DB_Teradata, DB_PostSQL, DB_MsSQL, DB_Sybase, DB_HSQLDB, DB_Greenplum,
        DB_ODBCBridge, DB_Informix, DB_Oracle, DB_ONE, DB_DB2, DB_Kingbase, DB_Kingbase8, DB_SNOW_BALL, DB_Elasticsearch,DB_DMbase};

    private static final Map<String, String> jdbcVersionMap = new HashMap<>();
    private static final Map<String, Driver> tmpDriverMap = new HashMap<String, Driver>();

    public static void connect(String driver, String url, String user, String password, Properties params,
                               String jarPath) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection(driver, url, user, password, params, jarPath);
        closeConnection(conn);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("close connection failed");
            }
        }
    }

    public static DBConf getDB(String driver) {
        for (DBConf db : dbs) {
            if (db.driver.equals(driver)) {
                return db;
            }
        }
        return null;
    }

    public static Connection getConnection(String driver, String url, String user, String password, Properties params,
                                           String jarPath) throws SQLException, ClassNotFoundException {
        long start = System.currentTimeMillis();
        if (driver == null || url == null)
            throw new IllegalArgumentException("driver and url are required!");
        url = url.trim();
        driver = driver.trim();
        Connection conn = null;
        for (DBConf db : dbs) {
            if (db.driver.equals(driver)) {
                conn = getCommonConnection(driver, url, user, password, params, db, jarPath);
                break;
            }
        }
        if (conn == null)
            conn = getGenericConnection(driver, url, user, password, params);

        long end = System.currentTimeMillis();
        log.info("static getConnection() takes " + (end - start) / 1000 + " seconds to get connection");
        return conn;
    }

    public static List<String> getTableList(String driver, String url, String user, String password, String catalog,
                                            String schema, Properties params, String jarPath) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection(driver, url, user, password, params, jarPath);
        try {
            DBConf db = getDB(driver);
            return getTableList(conn, db, user, catalog, schema);
        } finally {
            closeConnection(conn);
        }
    }

    public static List<String> getTableColumnList(String driver, String url, String user, String password,
                                                  Properties params, String table, String catalog, String schema, String jarPath)
        throws SQLException, ClassNotFoundException {
        Connection conn = getConnection(driver, url, user, password, params, jarPath);
        try {
            DBConf db = getDB(driver);
            return getTableColumnList(conn, db, user, table, catalog, schema);
        } finally {
            closeConnection(conn);
        }
    }

    public static Map<String, Object> getTableData(String driver, String url, String user, String password,
                                                   Properties params, String sql, String catalog, String schema, Map paraMap, String jarPath)
        throws SQLException, ClassNotFoundException {
        Connection conn = getConnection(driver, url, user, password, params, jarPath);
        if (StringUtils.isNotBlank(catalog)) {
            conn.setCatalog(catalog);
        }
        if (StringUtils.isNotBlank(schema)) {
            conn.setSchema(schema);
        }
        try {
            DBConf db = getDB(driver);
            return getTableData(conn, db, user, sql, paraMap);
        } finally {
            closeConnection(conn);
        }
    }

    public static List<String> getTableList(Connection conn, DBConf db, String user, String catalog, String schema)
        throws SQLException {
        log.info("getTableList DBConf = {}, user = {}, catalog = {}, schema = {}", db, user, catalog, schema);
        long start = System.currentTimeMillis();
        DatabaseMetaData metaData = conn.getMetaData();
        if (StringUtils.isBlank(catalog)) {
            catalog = conn.getCatalog();
        }
        if (StringUtils.isBlank(schema)) {
            schema = user.toUpperCase();
        }
        ResultSet rs = null;
        List<String> tables = new ArrayList<>();
        if (db == DB_MYSQL || db == DB_Oracle || db == DB_DMbase) {
            rs = metaData.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});
        } else if (db == DB_ONE) {
            rs = metaData.getTables(catalog, "public", null, new String[]{"TABLE", "VIEW"});
        } else if (db == DB_PostSQL || db == DB_Kingbase || db == DB_Kingbase8) {
            rs = metaData.getTables(catalog, null, null, new String[]{"TABLE", "VIEW"});
        } else if (db == DB_MsSQL) {
            rs = metaData.getTables(catalog, "dbo", null, new String[]{"TABLE", "VIEW"});
        } else if (db == DB_SNOW_BALL) {
            rs = metaData.getTables(catalog, catalog, null, null);
        } else
            rs = metaData.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});

        while (rs.next())
            tables.add(rs.getString("TABLE_NAME"));

        rs.close();
        long end = System.currentTimeMillis();
        log.info("static getTableList() takes " + (end - start) / 1000 + " seconds to fetch metadata table list");
        return tables;
    }

    public static List<String> getTableColumnList(Connection conn, DBConf db, String user, String table, String catalog,
                                                  String schema) throws SQLException {
        long start = System.currentTimeMillis();
        DatabaseMetaData metaData = conn.getMetaData();
        if (StringUtils.isBlank(catalog)) {
            catalog = conn.getCatalog();
        }
        if (StringUtils.isBlank(schema)) {
            schema = user.toUpperCase();
        }
        ResultSet rs = null;
        List<String> columns = new ArrayList<>();
        if (db == DB_MYSQL || db == DB_Oracle) {
            rs = metaData.getColumns(catalog, schema, table, null);
        } else if (db == DB_ONE) {
            rs = metaData.getColumns(catalog, "public", table, null);
        } else if (db == DB_PostSQL || db == DB_SNOW_BALL || db == DB_Kingbase || db == DB_Kingbase8 || db == DB_DMbase) {
            rs = metaData.getColumns(catalog, null, table, null);
        } else if (db == DB_MsSQL) {
            rs = metaData.getColumns(catalog, "dbo", table, null);
        } else
            rs = metaData.getColumns(catalog, schema, table, null);

        // refer to
        // https://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html#getColumns-java.lang.String-java.lang.String-java.lang.String-java.lang.String-
        while (rs.next()) {
            String name = rs.getString("COLUMN_NAME");
            columns.add(name);
            String type = rs.getString("TYPE_NAME");
            if (type.equalsIgnoreCase("NUMBER") || type.equalsIgnoreCase("DECIMAL")) {
                int precision = rs.getInt("COLUMN_SIZE");
                int scale = rs.getInt("DECIMAL_DIGITS");
                columns.add(type + "(" + precision + "," + scale + ")");
            } else {
                columns.add(type);
            }
        }

        rs.close();
        long end = System.currentTimeMillis();
        log.info(
            "static getTableColumnList() takes " + (end - start) / 1000 + " seconds to fetch metadata column list");
        return columns;
    }

    public static Map<String, Object> getTableData(Connection conn, DBConf db, String user, String sql, Map paraMap)
        throws SQLException {
        long start = System.currentTimeMillis();

        Integer limit = 100;
        if (paraMap.containsKey("limit")) {
            limit = Math.max(limit, Integer.parseInt((String) paraMap.get("limit")));
        }
        Integer offset = 0;
        if (paraMap.containsKey("offset")) {
            offset = Integer.parseInt((String) paraMap.get("offset"));
        }

        String table = null;
        if (paraMap.containsKey("table")) {
            table = (String) paraMap.get("table");
        }

        String primeKey = "ID";
        if (paraMap.containsKey("primeKey")) {
            primeKey = (String) paraMap.get("primeKey");
        }

        if (db == DB_MYSQL || db == DB_PostSQL || db == DB_Kingbase || db == DB_DMbase) {
            sql = sql + " limit " + limit;
        } else if (db == DB_Oracle) {
            sql = "select * from (" + sql + ") where rownum <= " + limit;
        } else if (db == DB_DB2) {
            String select = sql.replace("*", "*, ROW_NUMBER() OVER() AS RN");
            sql = "select * from ( " + select + " ) where RN >= 0 and RN < " + limit;
        } else if (db == DB_MsSQL) {
            sql = "select top " + limit + " * from " + table + " where " + primeKey + " not in (select top 0 "
                + primeKey + " from " + table + ")";
            // String select = sql.toUpperCase().replace("SELECT", "SELECT TOP "
            // + limit);
            // sql = select + " where ID not in (select top 0 " + primeKey + "
            // FROM " + table + ")";
            // String select = sql.replace("*", "*, ROW_NUMBER() OVER(ORDER BY
            // ID ASC) AS RN");
            // sql = "select top " + limit + " * from (" + select + ") temp
            // where RN > 0";
        }

        // Object rowCount = paraMap.get("rowCount");
        Map map = new HashMap();
        List<List<String>> rows = new ArrayList();
        List<String> nameList = new ArrayList();
        List<String> typeList = new ArrayList();
        map.put("names", nameList);
        map.put("types", typeList);
        map.put("rows", rows);

        try (Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setFetchSize(limit);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                long end = System.currentTimeMillis();
                log.info("static getTableData() takes " + (end - start) / 1000 + " seconds to fetch table data");

                int count = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= count; i++) {
                    nameList.add(rs.getMetaData().getColumnName(i));

                    String type = rs.getMetaData().getColumnTypeName(i);
                    if (type.equalsIgnoreCase("NUMBER")) {
                        int scale = rs.getMetaData().getScale(i);
                        int precision = rs.getMetaData().getPrecision(i);
                        typeList.add(type + "(" + precision + "," + scale + ")");
                    } else {
                        typeList.add(rs.getMetaData().getColumnTypeName(i));
                    }
                }

                int index = 0;
                while (rs.next()) {
                    if (index++ < offset)
                        continue;
                    if (rows.size() >= limit)
                        break;
                    List list = new ArrayList<>();
                    for (int i = 1; i <= count; i++) {
                        try {
                            String v = rs.getString(i);
                            if (v != null && v.length() > 30)
                                v = v.substring(0, 30) + "...";
                            list.add(v);
                        } catch (Exception e) {
                            list.add("?");
                        }
                    }
                    rows.add(list);
                }
            }
        }

        long end = System.currentTimeMillis();
        log.info("static getTableData() takes " + (end - start) / 1000 + " seconds to complete getTableData()");
        return map;
    }

    public static Connection geConnetion(String dbType, String url, String user, String password, Map properties)
        throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        if (url.contains("?")) {
            if (!url.endsWith("?"))
                url += "&";
            url += "user=" + user + "&password=" + password;
        } else
            url += "?" + "user=" + user + "&password=" + password;

        Connection conn = DriverManager.getConnection(url);

        return conn;
    }

    public static Connection getGenericConnection(String dbType, String url, String user, String password,
                                                  Map properties) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        if (url.contains("?")) {
            if (!url.endsWith("?"))
                url += "&";
            url += "user=" + user + "&password=" + password;
        } else
            url += "?" + "user=" + user + "&password=" + password;

        Connection conn = DriverManager.getConnection(url);
        return conn;
    }

    public static Connection getCommonConnection(String dbType, String url, String user, String password,
                                                 Properties properties, DBConf db, String jarPath) throws ClassNotFoundException, SQLException {
        if (db.propUrl && db.userInUrl) {
            if (url.contains(db.propHeader)) {
                if (!url.endsWith(db.propSep))
                    url += db.propSep;
                url += "user=" + user + db.propSep + "password=" + password;
            } else
                url += db.propHeader + "user=" + user + db.propSep + "password=" + password;
        }

        if (db.propUrl && properties != null) {

            for (Map.Entry<Object, Object> e : properties.entrySet()) {
                url += db.propSep + e.getKey() + "=" + e.getValue();
            }
        }

        if (tmpDriverMap.containsKey(db.driver)) {
            DriverManager.deregisterDriver(tmpDriverMap.get(db.driver));
        }

        if (StringUtils.isNotBlank(jarPath)) {
            jdbcVersionMap.put(jarPath, db.driver);
            try {
                dynamicLoadJdbc(jarPath);
            } catch (Throwable e) {
                log.error("dynamicLoadJdbc has error", e);
            }
        } else {
            Class.forName(db.driver);
        }

        if (db.needProperties)
            return DriverManager.getConnection(url, properties);

        if (!db.userInUrl) {
            if (user == null)
                user = "";
            if (password == null)
                password = "";
            return DriverManager.getConnection(url, user, password);
        }

        return DriverManager.getConnection(url);
    }

    // 动态加载jdbc驱动
    private static void dynamicLoadJdbc(String mysqlJdbcFile) throws Exception {
        URL u = new URL("jar:file:" + mysqlJdbcFile + "!/");
        String classname = jdbcVersionMap.get(mysqlJdbcFile);
        URLClassLoader ucl = new URLClassLoader(new URL[]{u});
        Driver d = (Driver) Class.forName(classname, true, ucl).newInstance();
        DriverImpl driver = new DriverImpl(d);
        DriverManager.registerDriver(driver);
        tmpDriverMap.put(classname, driver);
    }

    static class DriverImpl implements Driver {
        private Driver driver;

        DriverImpl(Driver d) {
            this.driver = d;
        }

        public boolean acceptsURL(String u) throws SQLException {
            return this.driver.acceptsURL(u);
        }

        public Connection connect(String u, Properties p) throws SQLException {
            return this.driver.connect(u, p);
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
            return this.driver.getPropertyInfo(url, info);
        }

        @Override
        public int getMajorVersion() {
            return this.driver.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return this.driver.getMinorVersion();
        }

        @Override
        public boolean jdbcCompliant() {
            return this.driver.jdbcCompliant();
        }

        @Override
        public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return this.driver.getParentLogger();
        }
    }

    static class DBConf {
        String driver;
        boolean propUrl;
        String propHeader;
        String propSep;
        boolean needProperties;
        boolean userInUrl;
        boolean userInProp;
        boolean userParam;

        public DBConf(boolean propUrl, String propHeader, String propSep, boolean needProperties, boolean userInUrl,
                      boolean userInProp, boolean userParam, String driver) {
            this.driver = driver;
            this.propUrl = propUrl;
            this.propHeader = propHeader;
            this.propSep = propSep;
            this.needProperties = needProperties;
            this.userInUrl = userInUrl;
            this.userInProp = userInProp;
            this.userParam = userParam;
        }

        @Override
        public String toString() {
            return "DBConf{" + "driver='" + driver + '\'' + ", propUrl=" + propUrl + ", propHeader='" + propHeader
                + '\'' + ", propSep='" + propSep + '\'' + ", needProperties=" + needProperties + ", userInUrl="
                + userInUrl + ", userInProp=" + userInProp + ", userParam=" + userParam + '}';
        }
    }

    public static void main(String[] args) {

        try {
            Connection conn = getConnection("com.kingbase.Driver", "jdbc:kingbase://192.168.1.44:54321/TEST", "SYSTEM",
                "Ruifan" + "@123", null,
                "/tmp/flowconfig/jarUploadPath/save/9a80d8f5-d99d-4563-818e-61872aa63fa7_kingbasejdbc4.jar");
            // List<String> list = getTableList(conn, DB_Kingbase, "SYSTEM", "",
            // "");
            // for (String str : list) {
            // System.out.println(str);
            // }
            Map map = new HashMap();
            map.put("limit", "5");
            Map<String, Object> ret = getTableData(conn, DB_Kingbase, "SYSTEM", "select * from TEST_510", map);
            System.out.println(ret);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
       
    }
}
