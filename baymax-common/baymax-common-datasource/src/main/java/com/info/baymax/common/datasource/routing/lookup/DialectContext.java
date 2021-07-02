package com.info.baymax.common.datasource.routing.lookup;

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

    public static void master(MasterSlavesDataSources<?> dbConfig) {
        set(dbConfig.getMasterDialect());
        log.debug("set datasource dialect:" + dbConfig.getMasterDialect().getValue());
    }

    public static void slave(MasterSlavesDataSources<?> dbConfig, String lookupKey) {
        // 如果从节点数为0，则设置master
        int slavesNum = dbConfig.getSlavesNum();
        if (slavesNum <= 0 || !LookupKeyContext.isSlave(lookupKey)) {
            master(dbConfig);
            return;
        }

        Dialect[] slavesDialects = dbConfig.getSlavesDialects();
        int slaveIndex = LookupKeyContext.getSlaveIndex(lookupKey);
        if (slaveIndex >= slavesDialects.length) {
            slaveIndex = slavesDialects.length - 1;
        }
        Dialect dialect = slavesDialects[slaveIndex];
        set(dialect);
        log.debug("set datasource dialect:" + dialect.getValue());
    }

    public static void clearDbType() {
        CONTEXTHOLDER.remove();
        log.debug("clear datasource dialect contextHolder");
    }
}
