package com.info.baymax.common.comp.config.db;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(value = "spring.datasource.multiple.enabled", havingValue = "true", matchIfMissing = true)
// @ConditionalOnExpression(value = "${spring.datasource.multiple.enabled:false}")
@EnableConfigurationProperties(HikariDbProperties.class)
public class RoutingDataSourceAutoConfiguration {

    @Autowired
    private DbConfig<? extends DataSource> dbConfig;

    @Bean
    @ConditionalOnMissingBean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Primary
    @Bean
    @ConditionalOnMissingBean
    public AbstractRoutingDataSource dataSource() {
        MultipleRoutingDataSource proxy = new MultipleRoutingDataSource(dbConfig);
        proxy.setDefaultTargetDataSource(dbConfig.getMaster());
        proxy.setTargetDataSources(dbConfig.getTargetDataSources());
        return proxy;
    }

    @Aspect
    @Component
    public class RoutingDataSourceInterceptor {

        @Pointcut("@annotation(com.info.baymax.common.comp.config.db.ReadOnly) "
            + "|| execution(* com.info.baymax.dsp..service..*.select*(..)) "
            + "|| execution(* com.info.baymax.dsp..service..*.find*(..)) "
            + "|| execution(* com.info.baymax.dsp..service..*.fetch*(..)) "
            + "|| execution(* com.info.baymax.dsp..service..*.get*(..)) "
            + "|| execution(* com.info.baymax.dsp..service..*.count*(..)) "
            + "|| execution(* com.info.baymax.dsp..service..*.exist*(..)) ")
        public void readPointcut() {
        }

        @Pointcut("!@annotation(com.info.baymax.common.comp.config.db.ReadOnly) "
            + "&&(execution(* com.info.baymax.dsp..service..*.insert*(..)) "
            + "|| execution(* com.info.baymax.dsp..service..*.save*(..)) "
            + "|| execution(* com.info.baymax.dsp..service..*.add*(..)) "
            + "|| execution(* com.info.baymax.dsp..service..*.update*(..)) "
            + "|| execution(* com.info.baymax.dsp..service..*.edit*(..)) "
            + "|| execution(* com.info.baymax.dsp..service..*.delete*(..)) "
            + "|| execution(* com.info.baymax.dsp..service..*.remove*(..)))")
        public void writePointcut() {
        }

        @Before("readPointcut()")
        public void read() {
            DbContextHolder.slave(dbConfig);
        }

        @Before("writePointcut()")
        public void write() {
            DbContextHolder.master();
        }
    }

}
