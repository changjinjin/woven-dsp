package com.info.baymax.common.comp.config.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Configuration
@EnableConfigurationProperties(HikariDbProperties.class)
public class RoutingDataSourceAutoConfiguration {

    @Autowired
    private DbConfig<? extends DataSource> dbConfig;

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public AbstractRoutingDataSource dataSource() {
        MultipleRoutingDataSource proxy = new MultipleRoutingDataSource(dbConfig);
        proxy.setDefaultTargetDataSource(dbConfig.getMaster());
        proxy.setTargetDataSources(dbConfig.getTargetDataSources());
        return proxy;
    }
}
