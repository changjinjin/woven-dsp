package com.info.baymax.common.datasource.routing.config;

import com.info.baymax.common.datasource.multiple.MapperProperties;
import com.info.baymax.common.datasource.routing.lookup.Dialect;
import com.info.baymax.common.datasource.routing.lookup.MasterSlavesDataSources;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.routing-datasource")
public class HikariMasterSlavesDataSourcesProperties implements MasterSlavesDataSources<HikariDataSource> {

	/**
	 * 主数据源
	 */
	private HikariDataSource master;

	/**
	 * 从数据源列表
	 */
	private HikariDataSource[] slaves;

	@Primary
	public HikariDataSource getMaster() {
		return master;
	}

	@Override
	public Dialect parseDialect(DataSource dataSource) {
		HikariDataSource ds = (HikariDataSource) dataSource;
		return Dialect.fromJdbcUrl(ds.getJdbcUrl());
	}

	@Getter
	@Setter
	@Deprecated
	public static class WrapedDataSource {

		/**
		 * 数据库方言
		 */
		private String dialect;

		/**
		 * 数据源配置
		 */
		private HikariDataSource hikari;

		/**
		 * mapper配置
		 */
		private MapperProperties mapper;

		/**
		 * 获取方言类型
		 */
		public String getDialect() {
			if (StringUtils.isEmpty(dialect) && mapper != null) {
				this.dialect = mapper.getIdentity();
			}
			return dialect;
		}

		/**
		 * 获取mapper配置
		 */
		public MapperProperties getMapper() {
			if (mapper != null && StringUtils.isEmpty(mapper.getIdentity()) && StringUtils.isNotEmpty(dialect)) {
				this.mapper.setIdentity(dialect);
			}
			return mapper;
		}
	}
}
