package com.info.baymax.common.jdbc.config;

import com.info.baymax.common.jdbc.datasource.lookup.MasterSlaveDataSources;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class HikariDataSourcesProperties implements MasterSlaveDataSources<HikariDataSource> {
    /**
     * 主数据源
     */
    private HikariDataSource master;

    /**
     * 从数据源列表
     */
    private HikariDataSource[] slaves;

}
