package com.info.baymax.dsp.access.dataapi.jdbc;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class JdbcUtils {

    /**
     * 数据源缓存
     */
    private static final Cache<String, DataSource> dsCache = CacheBuilder.newBuilder().maximumSize(100) // 设置缓存的最大容量
        .expireAfterWrite(10, TimeUnit.MINUTES) // 设置缓存在写入一分钟后失效
        .concurrencyLevel(10) // 设置并发级别为10
        .recordStats() // 开启缓存统计
        .build();

    public static DataSource createDataSource(String driverClassName, String jdbcUrl, String username, String password,
                                              Properties properties) {
        String key = driverClassName + "_" + jdbcUrl + "_" + username;
        DataSource dataSource = dsCache.getIfPresent(key);
        if (dataSource == null) {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.setDriverClassName(driverClassName);
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            properties.forEach((k, v) -> {
                hikariConfig.addDataSourceProperty(k.toString(), v.toString());
            });
            dataSource = new HikariDataSource(hikariConfig);
        }
        dsCache.put(key, dataSource);
        return dataSource;
    }
}
