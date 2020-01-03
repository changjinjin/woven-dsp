//package com.info.baymax.common.comp.config.db;
//
//import javax.sql.DataSource;
//
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class RoutingDataSourceInterceptor {
//
//    @Autowired
//    private DbConfig<? extends DataSource> dbConfig;
//
//    @Pointcut("@annotation(com.info.baymax.common.comp.config.db.ReadOnly) "
//        + "|| execution(* com.info.baymax.dsp..service..*.select*(..)) "
//        + "|| execution(* com.info.baymax.dsp..service..*.find*(..)) "
//        + "|| execution(* com.info.baymax.dsp..service..*.fetch*(..)) "
//        + "|| execution(* com.info.baymax.dsp..service..*.get*(..)) "
//        + "|| execution(* com.info.baymax.dsp..service..*.count*(..)) "
//        + "|| execution(* com.info.baymax.dsp..service..*.exist*(..)) "
//    )
//    public void readPointcut() {
//    }
//
//    @Pointcut("!@annotation(com.info.baymax.common.comp.config.db.ReadOnly) "
//        + "&&(execution(* com.info.baymax.dsp..service..*.insert*(..)) "
//        + "|| execution(* com.info.baymax.dsp..service..*.save*(..)) "
//        + "|| execution(* com.info.baymax.dsp..service..*.add*(..)) "
//        + "|| execution(* com.info.baymax.dsp..service..*.update*(..)) "
//        + "|| execution(* com.info.baymax.dsp..service..*.edit*(..)) "
//        + "|| execution(* com.info.baymax.dsp..service..*.delete*(..)) "
//        + "|| execution(* com.info.baymax.dsp..service..*.remove*(..)))")
//    public void writePointcut() {
//    }
//
//    @Before("readPointcut()")
//    public void read() {
//        DbContextHolder.slave(dbConfig);
//    }
//
//    @Before("writePointcut()")
//    public void write() {
//        DbContextHolder.master();
//    }
//}
