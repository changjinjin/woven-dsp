package com.info.baymax.common.cache.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.common.cache.CustomRedisCacheManager;

@EnableCaching
@Configuration
@ConditionalOnProperty(prefix = RedisCacheProperties.PREFIX, name = "type", havingValue = "redis", matchIfMissing = true)
@EnableConfigurationProperties(RedisCacheProperties.class)
public class RedisConfig extends CachingConfigurerSupport {

    private final RedisCacheProperties properties;

    public RedisConfig(RedisCacheProperties properties) {
        this.properties = properties;
    }

    @Bean
    public CustomRedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                Object.class);

        // 解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 配置序列化(解决乱码的问题)
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // .entryTtl(Duration.ZERO)
                .entryTtl(Duration.ofSeconds(15L)) // 设置默认缓存15秒
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();

        /**
         * 根据配置文件设置配置信息
         */
        List<CacheInstance> instances = properties.getInstances();
        Set<String> cacheNames = new HashSet<>();
        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        if (instances != null && instances.size() > 0) {
            for (CacheInstance cacheInstance : instances) {
                cacheNames.add(cacheInstance.getCacheName());
                configMap.put(cacheInstance.getCacheName(),
                        config.entryTtl(Duration.ofSeconds(cacheInstance.getExpiration())));// 这个缓存空间30秒
            }
        }
        CustomRedisCacheManager cacheManager = CustomRedisCacheManager
                .customBuilder(factory)
                .cacheDefaults(config)
                .initialCacheNames(cacheNames)// 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
                .withInitialCacheConfigurations(configMap).build();
        return cacheManager;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
}
