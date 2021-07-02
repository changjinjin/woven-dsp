package com.info.baymax.common.persistence.mybatis;

import lombok.Getter;

@Getter
public enum Dialect {
    hsqldb("hsqldb"), //
    h2("h2"), //
    postgresql("postgresql"), //
    phoenix("phoenix"), //
    mysql("mysql"), //
    mariadb("mariadb"), //
    sqlite("sqlite"), //
    herddb("herddb"), //
    oracle("oracle"), //
    oracle9i("oracle9i"), //
    db2("db2"), //
    informix("informix"), //
    informixsqli("informix-sqli"), //
    sqlserver("sqlserver"), //
    sqlserver2012("sqlserver2012"), //
    derby("derby"), //
    dm("dm"), //
    edb("edb"), //
    oscar("oscar"), //
    clickhouse("clickhouse"), //
    kingbase("kingbase"), //
    kingbase8("kingbase8"), //
    elasticsearch("elasticsearch")//
    ;

    private final String value;

    private Dialect(String value) {
        this.value = value;
    }

    public static Dialect fromJdbcUrl(String jdbcUrl) {
        final String url = jdbcUrl.toLowerCase();
        for (Dialect dialect : values()) {
            if (url.contains(":" + dialect.value.toLowerCase() + ":")) {
                return dialect;
            }
        }
        return mysql;
    }
}
