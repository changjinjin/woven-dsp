package com.info.baymax.common.mybatis.cache.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheTransfer {

    @Autowired
    public void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        if (redisConnectionFactory != null) {
            RedisCache.setRedisConnectionFactory(redisConnectionFactory);
        }
    }
}
