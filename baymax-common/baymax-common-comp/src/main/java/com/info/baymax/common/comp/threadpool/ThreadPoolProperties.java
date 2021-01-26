package com.info.baymax.common.comp.threadpool;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = ThreadPoolProperties.PREFIX)
public class ThreadPoolProperties {
    public static final String PREFIX = "thread.pool";

    private int corePoolSize = 5;
    private int maxPoolSize = 20;
    private int keepAliveSeconds = 60;
    private int queueCapacity = 99999;
    private boolean allowCoreThreadTimeOut = false;
    private String threadNamePrefix = "thread-pool-";

}
