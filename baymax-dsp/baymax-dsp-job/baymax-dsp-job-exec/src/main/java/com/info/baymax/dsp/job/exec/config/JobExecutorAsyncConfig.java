package com.info.baymax.dsp.job.exec.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @Author: haijun
 * @Date: 2020/4/28 20:35
 */
@Configuration
@EnableAsync
@Slf4j
public class JobExecutorAsyncConfig implements AsyncConfigurer {

    @Value("${dsp.job.executor.async-pool.size:15}")
    private int corePoolSize;
    @Value("${dsp.job.executor.async-pool.max-size:20}")
    private int maxPoolSize;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(corePoolSize);
        pool.setMaxPoolSize(maxPoolSize);
        pool.setThreadNamePrefix("jobExecutor-ops-async");
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.initialize();
        log.info("initialize jobExecutor async thread pool with core size {}, max size {}", corePoolSize, maxPoolSize);
        return pool;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            log.error("jobExecutor async thread throw exception message - " + throwable.getMessage() + " method " + method.getName(), throwable);
            for (Object param : params) {
                log.error("Parameter value - " + param);
            }
        };
    }
}
