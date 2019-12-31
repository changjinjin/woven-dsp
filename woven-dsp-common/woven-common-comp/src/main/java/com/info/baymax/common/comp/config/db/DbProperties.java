package com.info.baymax.common.comp.config.db;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConditionalOnProperty(prefix = DbProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@ConfigurationProperties(prefix = DbProperties.PREFIX)
public class DbProperties implements DbConfig<HikariDataSource> {
	public static final String PREFIX = "spring.datasource.multiple";

	/**
	 * 主数据源
	 */
	private HikariDataSource master;

	/**
	 * 从数据源列表
	 */
	private HikariDataSource[] slaves;

}
