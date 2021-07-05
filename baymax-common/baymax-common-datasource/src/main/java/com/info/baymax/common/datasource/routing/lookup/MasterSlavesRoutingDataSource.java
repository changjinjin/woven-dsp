package com.info.baymax.common.datasource.routing.lookup;

import com.info.baymax.common.persistence.mybatis.Dialect;
import com.info.baymax.common.persistence.mybatis.DialectContext;
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
			masterDialect(dbConfig);
			return lookupKey;
		}
		// 使用随机轮询规则决定使用哪个读库
		LookupKeyContext.slave(dbConfig);
		lookupKey = LookupKeyContext.get();
		slaveDialect(dbConfig, lookupKey);
		log.trace("use slave datasource：{}", lookupKey);
		return lookupKey;
	}

	public void masterDialect(MasterSlavesDataSources<?> dbConfig) {
		DialectContext.set(dbConfig.getMasterDialect());
		log.debug("set datasource dialect:" + dbConfig.getMasterDialect().getValue());
	}

	public void slaveDialect(MasterSlavesDataSources<?> dbConfig, String lookupKey) {
		// 如果从节点数为0，则设置master
		int slavesNum = dbConfig.getSlavesNum();
		if (slavesNum <= 0 || !LookupKeyContext.isSlave(lookupKey)) {
			masterDialect(dbConfig);
			return;
		}

		Dialect[] slavesDialects = dbConfig.getSlavesDialects();
		int slaveIndex = LookupKeyContext.getSlaveIndex(lookupKey);
		if (slaveIndex >= slavesDialects.length) {
			slaveIndex = slavesDialects.length - 1;
		}
		Dialect dialect = slavesDialects[slaveIndex];
		DialectContext.set(dialect);
		log.debug("set datasource dialect:" + dialect.getValue());
	}
}
