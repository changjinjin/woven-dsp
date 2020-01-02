package com.info.baymax.common.comp.config.db;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class HikariDbProperties implements DbConfig<HikariDataSource> {
    /**
     * 主数据源
     */
    private HikariDataSource master;

    /**
     * 从数据源列表
     */
    private HikariDataSource[] slaves;

}
