package com.info.baymax.common.comp.config.db;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbContextHolder {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

	private static final AtomicInteger counter = new AtomicInteger(0);

	public static void set(String lookupKey) {
		contextHolder.set(lookupKey);
	}

	public static String get() {
		return contextHolder.get();
	}

	public static void master() {
		set(DbType.MASTER.name());
		log.debug("routing to datasource " + DbType.MASTER.name());
	}

	public static void slave(DbConfig<?> dbConfig) {
		if (counter.get() > dbConfig.getSlavesNum()) {
			counter.set(1);
		}
		String lookupKey = DbType.SLAVE.name() + counter.getAndIncrement();
		log.debug("routing to datasource " + lookupKey);
		set(lookupKey);
	}

	public static void clearDbType() {
		master();
	}

}
