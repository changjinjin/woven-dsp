package com.info.baymax.common.comp.config.db;

import javax.sql.DataSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Aspect
//@Component
//@ConditionalOnBean(HikariDbProperties.class)
//@ConditionalOnClass(HikariDbProperties.class)
public class ReadOnlyInterceptor implements Ordered {

    @Autowired
    private DbConfig<? extends DataSource> dbConfig;

    @Around("@annotation(readOnly)")
    public Object setRead(ProceedingJoinPoint joinPoint, ReadOnly readOnly) throws Throwable {
        try {
            DbContextHolder.slave(dbConfig);
            return joinPoint.proceed();
        } finally {
            DbContextHolder.clearDbType();
            log.info("clear threadLocal");
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
