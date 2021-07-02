package com.info.baymax.common.datasource.routing.lookup;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 一主多从数据源
 *
 * @param <T> 数据源类型
 * @author jingwei.yang
 * @date 2021年7月2日 下午12:06:59
 */
public interface MasterSlavesDataSources<T extends DataSource> {

	/**
	 * master数据源
	 *
	 * @return master数据源
	 */
	T getMaster();

	/**
	 * 从数据源
	 */
	T[] getSlaves();

	/**
	 * 获取master数据源方言
	 *
	 * @return master数据源方言
	 */
	default Dialect getMasterDialect() {
		Dialect dialect = parseDialect(getMaster());
		return dialect == null ? Dialect.mysql : dialect;
	}

	/**
	 * 获取从数据源方言
	 *
	 * @return 从数据源方言
	 */
	default Dialect[] getSlavesDialects() {
		T[] slaves = getSlaves();
		if (slaves != null && slaves.length > 0) {
			return Stream.of(slaves).map(t -> parseDialect(t)).toArray(Dialect[]::new);
		}
		return null;
	}

	/**
	 * 获取从数据源方言
	 *
	 * @return 从数据源方言
	 */
	Dialect parseDialect(DataSource dataSource);

	/**
	 * 获取从数据源的个数
	 *
	 * @return 从源个数
	 */
	default int getSlavesNum() {
		T[] slaves = getSlaves();
		return (slaves == null || slaves.length == 0) ? 0 : slaves.length;
	}

	/**
	 * 获取目标数据源
	 *
	 * @return 目标数据源
	 */
	default Map<Object, Object> getTargetDataSources() {
		Map<Object, Object> targetDataSources = new HashMap<>(2);
		targetDataSources.put(LookupType.master.name(), getMaster());
		T[] slaves = getSlaves();
		if (slaves != null && slaves.length > 0) {
			int i = 0;
			for (T t : slaves) {
				targetDataSources.put(LookupType.slave.name() + (i++), t);
			}
		}
		return targetDataSources;
	}
}
