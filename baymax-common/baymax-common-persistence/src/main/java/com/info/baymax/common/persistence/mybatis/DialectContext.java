package com.info.baymax.common.persistence.mybatis;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DialectContext {
    private static final ThreadLocal<Dialect> CONTEXTHOLDER = new ThreadLocal<>();

    public static void set(Dialect dialect) {
        CONTEXTHOLDER.set(dialect);
    }

    public static Dialect get() {
        return CONTEXTHOLDER.get();
    }

    public static void clearDbType() {
        CONTEXTHOLDER.remove();
        log.debug("clear datasource dialect contextHolder");
    }
}
