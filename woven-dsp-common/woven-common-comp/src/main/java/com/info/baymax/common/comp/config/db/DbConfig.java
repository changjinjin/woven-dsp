package com.info.baymax.common.comp.config.db;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

public interface DbConfig<T extends DataSource> {

	/**
	 * 主数据源
	 */
	T getMaster();

	/**
	 * 从数据源
	 */
	T[] getSlaves();

	default int getSlavesNum() {
		T[] slaves = getSlaves();
		return slaves == null ? 0 : slaves.length;
	}

	/**
	 * 获取目标数据源
	 * 
	 * @return 目标数据源
	 */
	default Map<Object, Object> getTargetDataSources() {
		Map<Object, Object> targetDataSources = new HashMap<>(2);
		targetDataSources.put(DbType.MASTER.name(), getMaster());
		T[] slaves = getSlaves();
		if (slaves != null) {
			int i = 1;
			for (T t : slaves) {
				targetDataSources.put(DbType.SLAVE.name() + (i++), t);
			}
		}
		return targetDataSources;
	}
}
