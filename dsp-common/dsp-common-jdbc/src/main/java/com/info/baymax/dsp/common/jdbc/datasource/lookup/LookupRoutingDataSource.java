package com.info.baymax.dsp.common.jdbc.datasource.lookup;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LookupRoutingDataSource extends AbstractRoutingDataSource {

	private final MasterSlaveDataSources<? extends DataSource> dbConfig;

	public LookupRoutingDataSource(MasterSlaveDataSources<? extends DataSource> dbConfig) {
		this.dbConfig = dbConfig;
	}

	@Override
	protected Object determineCurrentLookupKey() {
		String lookupKey = DataSourceContextHolder.get();
		if (DataSourceType.MASTER.name().equalsIgnoreCase(lookupKey)) {
			log.trace("use master datasource");
			return lookupKey;
		}
		// 使用随机轮询规则决定使用哪个读库
		DataSourceContextHolder.slave(dbConfig);
		lookupKey = DataSourceContextHolder.get();
		log.trace("use slave datasource：{}", lookupKey);
		return lookupKey;
	}
}
