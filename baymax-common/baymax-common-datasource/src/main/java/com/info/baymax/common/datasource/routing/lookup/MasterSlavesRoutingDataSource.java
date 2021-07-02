package com.info.baymax.common.datasource.routing.lookup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

@Slf4j
public class MasterSlavesRoutingDataSource extends AbstractRoutingDataSource {

	private final MasterSlavesDataSources<? extends DataSource> dbConfig;

	public MasterSlavesRoutingDataSource(MasterSlavesDataSources<? extends DataSource> dbConfig) {
		this.dbConfig = dbConfig;
	}

	@Override
	protected Object determineCurrentLookupKey() {
		String lookupKey = LookupKeyContext.get();
		if (LookupType.master.name().equalsIgnoreCase(lookupKey)) {
			log.trace("use master datasource");
			DialectContext.master(dbConfig);
			return lookupKey;
		}
		// 使用随机轮询规则决定使用哪个读库
		LookupKeyContext.slave(dbConfig);
		lookupKey = LookupKeyContext.get();
		DialectContext.slave(dbConfig, lookupKey);
		log.trace("use slave datasource：{}", lookupKey);
		return lookupKey;
	}
}
