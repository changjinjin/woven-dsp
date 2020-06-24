package com.info.baymax.dsp.data.consumer.beans.source;

public enum DBType {

    Mysql("Mysql"),
    Teradata("Teradata"),
    JDBCODBCBridge("JDBC-ODBC Bridge"),
    OracleThin("Oracle Thin"),
    SQLServerMicrosoftDriver("Microsoft SQL Server (Microsoft Driver)"),
    QLServerMicrosoftJTDS("Microsoft SQL Server(JTDS)"),
    Sybase("Sybase"),
    PostgreSQL("PostgreSQL"),
    HSQLDB("HSQLDB"),
    Greenplum("Greenplum"),
    GBase("GBase"),
    GenericDB("Generic DB"),
    DB2("DB2"),
    DBONE("DBONE"),
    SnowBall("SnowBall"),
    HIVE("HIVE"),
    Kingbase("Kingbase");

    private final String value;

    DBType(String type) {
        this.value = type;
    }

    public static boolean contains(String type) {
        for (DBType typeEnum : DBType.values()) {
            if (typeEnum.value.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public static DBType valueOfType(String type) {
        for (DBType typeEnum : DBType.values()) {
            if (typeEnum.value.equals(type)) {
                return typeEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }


}