package com.info.baymax.common.sqlhelper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.info.baymax.common.utils.JsonUtils;
import lombok.*;

import java.util.HashMap;
import java.util.Properties;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class JdbcConf {
    @JsonProperty(value = "DBType")
    private String DBType;// "Mysql"
    private String batchsize;// "10000"
    private String catalog;// ""
    private String chineseName;// ""
    private String database;// "test"
    private String dateToTimestamp;// "false"
    private String driver;// "com.mysql.jdbc.Driver"
    private String host;// "192.168.1.85"
    private String id;// "0a75e414-8491-4a96-b9af-561a92c93f31"
    private String jarPath;// "mysql-connector-java-5.1.48.jar"
    private String name;// "test0612"
    private String password;// "AES(cad2fb721d282f6e5151605a1874ffe4)"
    private String port;// "3306"
    private String resType;// "DB"
    private String schema;// ""
    private String table;// "table_empty"
    private String url;// "jdbc:mysql:192.168.1.85:3306/test"
    private String user;// "merce"
    private String username;// "merce"
    private Properties properties;// "merce"

    public static JdbcConf from(HashMap<String, Object> attributes) {
        return JsonUtils.fromObject(attributes, JdbcConf.class);
    }
}
