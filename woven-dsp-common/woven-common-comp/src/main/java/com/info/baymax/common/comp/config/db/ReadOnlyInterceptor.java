package com.info.baymax.common.comp.config.db;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Description
 *
 * @author fxb
 * @date 2018-08-31
 */
@Slf4j
@Aspect
@Component
@ConditionalOnBean(DbProperties.class)
@ConditionalOnClass(DbProperties.class)
public class ReadOnlyInterceptor implements Ordered {

	@Autowired
	private DbProperties dbProperties;

	@Around("@annotation(readOnly)")
	public Object setRead(ProceedingJoinPoint joinPoint, ReadOnly readOnly) throws Throwable {
		try {
			DbContextHolder.slave(dbProperties);
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
