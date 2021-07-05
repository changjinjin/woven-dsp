package com.info.baymax.common.datasource.routing.lookup;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LookupKeyContext {
	private static final ThreadLocal<String> CONTEXTHOLDER = new ThreadLocal<>();

	public static final AtomicInteger COUNTER = new AtomicInteger(0);

	public static void set(String lookupKey) {
		CONTEXTHOLDER.set(lookupKey);
	}

	public static String get() {
		return CONTEXTHOLDER.get();
	}

	public static void master() {
		set(LookupType.master.name());
		log.debug("routing to datasource " + LookupType.master.name());
	}

	public static void slave(MasterSlavesDataSources<?> dbConfig) {
		// 如果从节点数为0，则设置master
		int slavesNum = dbConfig.getSlavesNum();
		if (slavesNum == 0) {
			set(LookupType.master.name());
			log.debug("no slaves,routing to master:" + LookupType.master.name());
			return;
		}

		int i = COUNTER.get();
		if (i > slavesNum) {
			COUNTER.set(0);
		}
		String lookupKey = makeLookupKey(COUNTER.getAndIncrement());
		log.debug("slaves nums: " + slavesNum + ",routing to slave: " + lookupKey);
		set(lookupKey);
	}

	public static boolean isMaster() {
		return isMaster(get());
	}

	public static boolean isMaster(String lookupKey) {
		return !isSlave(lookupKey);
	}

	public static boolean isSlave() {
		return isSlave(get());
	}

	public static boolean isSlave(String lookupKey) {
		return lookupKey.startsWith(LookupType.slave.name());
	}

	public static String makeLookupKey(int index) {
		return LookupType.slave.name().concat(index + "");
	}

	public static int getSlaveIndex(String lookupKey) {
		String name = LookupType.slave.name();
		if (StringUtils.isNotEmpty(lookupKey) && lookupKey.startsWith(name)) {
			return Integer.valueOf(lookupKey.replaceFirst(name, ""));
		}
		return 0;
	}

	public static void clearDbType() {
		CONTEXTHOLDER.remove();
		log.debug("clear datasource contextHolder");
	}
}
