//package com.info.baymax.common.comp.config.db;
//
//import javax.sql.DataSource;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Aspect
//@Component
//public class RoutingDataSourceInterceptor {
//
//	@Autowired
//	private DbConfig<? extends DataSource> dbConfig;
//
////	@Pointcut("@annotation(readOnly) "
////			+ "&& (execution(* com.info.baymax..service..*.select*(..)) "
////			+ "|| execution(* com.info.baymax..service..*.find*(..)) "
////			+ "|| execution(* com.info.baymax..service..*.fetch*(..)) "
////			+ "|| execution(* com.info.baymax..service..*.get*(..)))")
////	public void readPointcut(ReadOnly readOnly) {
////
////	}
////
////	@Pointcut("!@annotation(readOnly) "
////			+ "|| execution(* com.info.baymax..service..*.insert*(..)) "
////			+ "|| execution(* com.info.baymax..service..*.add*(..)) "
////			+ "|| execution(* com.info.baymax..service..*.update*(..)) "
////			+ "|| execution(* com.info.baymax..service..*.edit*(..)) "
////			+ "|| execution(* com.info.baymax..service..*.delete*(..)) "
////			+ "|| execution(* com.info.baymax..service..*.remove*(..))")
////	public void writePointcut(ReadOnly readOnly) {
////	}
//
////	@Before("readPointcut()")
//	@Around("@annotation(readOnly) "
//			+ "&& (execution(* com.info.baymax.service..*.select*(..)) "
//			+ "|| execution(* com.info.baymax.service..*.find*(..)) "
//			+ "|| execution(* com.info.baymax.service..*.fetch*(..)) "
//			+ "|| execution(* com.info.baymax.service..*.get*(..)))")
//	public Object read(ProceedingJoinPoint joinPoint,ReadOnly readOnly) throws Throwable {
//		try {
//			DbContextHolder.slave(dbConfig);
//			return joinPoint.proceed();
//		} finally {
//			DbContextHolder.clearDbType();
//			log.info("clear threadLocal");
//		}
//	}
//
////	@Before("writePointcut()")
//	@Around("!@annotation(readOnly) "
//			+ "|| execution(* com.info.baymax.service..*.insert*(..)) "
//			+ "|| execution(* com.info.baymax.service..*.add*(..)) "
//			+ "|| execution(* com.info.baymax.service..*.update*(..)) "
//			+ "|| execution(* com.info.baymax.service..*.edit*(..)) "
//			+ "|| execution(* com.info.baymax.service..*.delete*(..)) "
//			+ "|| execution(* com.info.baymax.service..*.remove*(..))")
//	public Object write(ProceedingJoinPoint joinPoint,ReadOnly readOnly) throws Throwable {
//		try {
//			DbContextHolder.master();
//			return joinPoint.proceed();
//		} finally {
//			DbContextHolder.clearDbType();
//			log.info("clear threadLocal");
//		}
//	}
//}
