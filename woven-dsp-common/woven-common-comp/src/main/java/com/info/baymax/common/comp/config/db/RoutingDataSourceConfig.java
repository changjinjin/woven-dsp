package com.info.baymax.common.comp.config.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Description
 *
 * @author fxb
 * @date 2018-09-03
 */
@Configuration
@ConditionalOnBean(DbProperties.class)
@ConditionalOnClass(DbProperties.class)
public class RoutingDataSourceConfig {

	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Autowired
	private DbProperties dbProperties;

	@Bean
	public AbstractRoutingDataSource dataSource() {
		MyAbstractRoutingDataSource proxy = new MyAbstractRoutingDataSource(dbProperties);
		proxy.setDefaultTargetDataSource(dbProperties.getMaster());
		proxy.setTargetDataSources(dbProperties.getTargetDataSources());
		return proxy;
	}

}
