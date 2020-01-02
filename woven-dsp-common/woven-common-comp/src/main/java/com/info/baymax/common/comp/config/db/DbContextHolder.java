package com.info.baymax.common.comp.config.db;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    private static final AtomicInteger counter = new AtomicInteger(1);

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
        // 如果从节点数为0，则设置master
        int slavesNum = dbConfig.getSlavesNum();
        if (slavesNum == 0) {
            set(DbType.MASTER.name());
            log.debug("no slaves,routing to master:" + DbType.MASTER.name());
            return;
        }

        int i = counter.get();
        if (i > slavesNum) {
            counter.set(1);
        }
        String lookupKey = DbType.SLAVE.name() + counter.getAndIncrement();
        log.debug("slaves nums: " + slavesNum + "routing to slave: " + lookupKey);
        set(lookupKey);
    }

    public static void clearDbType() {
        contextHolder.remove();
        log.debug("clear datasource contextHolder");
    }

}
