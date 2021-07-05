package com.info.baymax.common.datasource.multiple;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;

public abstract class AbstractDataSourceConfiguration {

	@SuppressWarnings("unchecked")
	protected <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
		return (T) properties.initializeDataSourceBuilder().type(type).build();
	}
}
