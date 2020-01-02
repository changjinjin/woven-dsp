package com.info.baymax.common.comp.config.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(value = "spring.datasource.multiple.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(HikariDbProperties.class)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class RoutingDataSourceAutoConfiguration {

    @Autowired
    private DbConfig<? extends DataSource> dbConfig;

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Primary
    @Bean
    public AbstractRoutingDataSource dataSource() {
        MultipleRoutingDataSource proxy = new MultipleRoutingDataSource(dbConfig);
        proxy.setDefaultTargetDataSource(dbConfig.getMaster());
        proxy.setTargetDataSources(dbConfig.getTargetDataSources());
        return proxy;
    }
}
