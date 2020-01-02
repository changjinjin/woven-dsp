package com.info.baymax.common.comp.config.db;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

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
