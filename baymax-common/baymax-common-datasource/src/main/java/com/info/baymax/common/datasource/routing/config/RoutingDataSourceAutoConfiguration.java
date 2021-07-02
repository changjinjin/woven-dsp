package com.info.baymax.common.datasource.routing.config;

import com.info.baymax.common.core.annotation.condition.ConditionalOnPropertyNotEmpty;
import com.info.baymax.common.datasource.routing.annotation.ReadOnly;
import com.info.baymax.common.datasource.routing.lookup.LookupKeyContext;
import com.info.baymax.common.datasource.routing.lookup.MasterSlavesDataSources;
import com.info.baymax.common.datasource.routing.lookup.MasterSlavesRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Slf4j
@Configuration
@ConditionalOnPropertyNotEmpty("spring.routing-datasource.master.jdbcUrl")
@EnableConfigurationProperties(HikariMasterSlavesDataSourcesProperties.class)
public class RoutingDataSourceAutoConfiguration {

	@Autowired
	private MasterSlavesDataSources<? extends DataSource> dbConfig;

	@Order(value = 1)
	@Primary
	@Bean
	@ConditionalOnMissingBean
	public DataSourceTransactionManager dataSourceTransactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Primary
	@Bean
	@ConditionalOnMissingBean
	public AbstractRoutingDataSource dataSource() {
		MasterSlavesRoutingDataSource proxy = new MasterSlavesRoutingDataSource(dbConfig);
		proxy.setDefaultTargetDataSource(dbConfig.getMaster());
		proxy.setTargetDataSources(dbConfig.getTargetDataSources());
		return proxy;
	}

	@Aspect
	@Component
	@Order(value = 0)
	class RoutingDataSourceInterceptor {

		@Pointcut("@annotation(com.info.baymax.common.datasource.routing.annotation.ReadOnly) "
			+ "|| execution(* com.info.baymax.common.jdbc.mybatis.mapper..*.select*(..)) "
			+ "|| execution(* com.info.baymax.common.jdbc.mybatis.mapper..*.find*(..)) "
			+ "|| execution(* com.info.baymax.common.jdbc.mybatis.mapper..*.fetch*(..)) "
			+ "|| execution(* com.info.baymax.common.jdbc.mybatis.mapper..*.get*(..)) "
			+ "|| execution(* com.info.baymax.common.jdbc.mybatis.mapper..*.count*(..)) "
			+ "|| execution(* com.info.baymax.common.jdbc.mybatis.mapper..*.exist*(..)) ")
		public void readPointcut() {
		}

		@Pointcut("!@annotation(com.info.baymax.common.datasource.routing.annotation.ReadOnly) "
			+ "&&(execution(* com.info.baymax.common.jdbc.mybatis.mapper..*.insert*(..)) "
			+ "|| execution(* com.info.baymax.common.jdbc.mybatis.mapper..*.save*(..)) "
			+ "|| execution(* com.info.baymax.common.jdbc.mybatis.mapper..*.add*(..)) "
			+ "|| execution(* com.info.baymax.common.jdbc.mybatis.mapper..*.update*(..)) "
			+ "|| execution(* com.info.baymax.common.jdbc.mybatis.mapper..*.modify*(..)) "
			+ "|| execution(* com.info.baymax.common.jdbc.mybatis.mapper..*.edit*(..)) "
			+ "|| execution(* com.info.baymax.common.jdbc.mybatis.mapper..*.delete*(..)) "
			+ "|| execution(* com.info.baymax.common.jdbc.mybatis.mapper..*.remove*(..)))")
		public void writePointcut() {
		}

		@Before("readPointcut()")
		public void read() {
			LookupKeyContext.slave(dbConfig);
		}

		@Before("writePointcut()")
		public void write() {
			LookupKeyContext.master();
		}
	}

	// @Aspect
	// @Component
	class ReadOnlyInterceptor implements Ordered {

		@Around("@annotation(readOnly)")
		public Object setRead(ProceedingJoinPoint joinPoint, ReadOnly readOnly) throws Throwable {
			try {
				LookupKeyContext.slave(dbConfig);
				return joinPoint.proceed();
			} finally {
				LookupKeyContext.clearDbType();
				log.info("clear threadLocal");
			}
		}

		@Override
		public int getOrder() {
			return 0;
		}
	}
}
