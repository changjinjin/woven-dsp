package com.info.baymax.dsp.common.jdbc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.info.baymax.dsp.common.jdbc.datasource.lookup.MasterSlaveDataSources;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;
import lombok.Setter;

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
