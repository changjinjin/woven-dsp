package com.info.baymax.common.comp.config.db;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyAbstractRoutingDataSource extends AbstractRoutingDataSource {

	private final DbConfig<? extends DataSource> dbConfig;

	public MyAbstractRoutingDataSource(DbConfig<? extends DataSource> dbConfig) {
		this.dbConfig = dbConfig;
	}

	@Override
	protected Object determineCurrentLookupKey() {
		String lookupKey = DbContextHolder.get();
		if (DbType.MASTER.name().equalsIgnoreCase(lookupKey)) {
			log.info("use master datasource");
			return lookupKey;
		}
		// 使用随机轮询规则决定使用哪个读库
		DbContextHolder.slave(dbConfig);
		lookupKey = DbContextHolder.get();
		log.info("use slave datasource：{}", lookupKey);
		return lookupKey;
	}
}
