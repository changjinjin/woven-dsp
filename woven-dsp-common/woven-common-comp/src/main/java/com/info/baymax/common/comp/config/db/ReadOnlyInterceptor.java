package com.info.baymax.common.comp.config.db;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
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
			log.info("清除threadLocal");
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
