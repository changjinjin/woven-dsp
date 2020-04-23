package com.info.baymax.common.cache.simple;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheListener {

    private CacheManagerImpl cacheManagerImpl;

    public CacheListener(CacheManagerImpl cacheManagerImpl) {
        this.cacheManagerImpl = cacheManagerImpl;
    }

    public void startListen() {
        new Thread() {
            public void run() {
                while (true) {
                    for (String key : cacheManagerImpl.getAllKeys()) {
                        if (cacheManagerImpl.isTimeOut(key)) {
                            cacheManagerImpl.clearByKey(key);
                            log.info(key + "缓存被清除");
                        }
                    }
                }
            }
        }.start();
    }
}
