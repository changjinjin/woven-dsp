package com.info.baymax.common.config.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定义一个线程池用于处理系统中异步业务需求
 *
 * @author jingwei.yang
 * @date 2019年5月15日 下午6:59:35
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = ThreadPoolProperties.class)
public class ThreadPoolTaskExecutorConfig {

    @Autowired
    private ThreadPoolProperties poolProperties;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        if (log.isInfoEnabled()) {
            log.info(
                "Start a new ThreadPoolTaskExecutor: threadNamePrefix [{}],corePoolSize [{}],maxPoolSize [{}],queueCapacity [{}].",
                poolProperties.getThreadNamePrefix(), poolProperties.getCorePoolSize(),
                poolProperties.getMaxPoolSize(), poolProperties.getQueueCapacity());
        }
        ThreadPoolTaskExecutor executor = new VisiableThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolProperties.getCorePoolSize());
        executor.setMaxPoolSize(poolProperties.getMaxPoolSize());
        executor.setQueueCapacity(poolProperties.getQueueCapacity());
        executor.setThreadNamePrefix(poolProperties.getThreadNamePrefix());
        executor.setKeepAliveSeconds(poolProperties.getKeepAliveSeconds());
        executor.setAllowCoreThreadTimeOut(poolProperties.isAllowCoreThreadTimeOut());

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();

        if (log.isInfoEnabled()) {
            log.info("ThreadPoolTaskExecutor initialized: threadNamePrefix [{}], poolSize [{}], activeCount [{}].",
                executor.getThreadNamePrefix(), executor.getPoolSize(), executor.getActiveCount());
        }
        return executor;
    }
}
