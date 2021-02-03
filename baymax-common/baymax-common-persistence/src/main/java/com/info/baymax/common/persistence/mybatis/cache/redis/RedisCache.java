package com.info.baymax.common.persistence.mybatis.cache.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class RedisCache implements Cache {
    private final String id;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    // 最大的过期时间
    private final int MAX_EXPIRATION_SECONDS = 7200000;

    private static RedisConnectionFactory redisConnectionFactory;

    public static void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        RedisCache.redisConnectionFactory = redisConnectionFactory;
    }

    private RedisSerializer<String> keySerializer;
    private RedisSerializer<Object> valueSerializer;

    public RedisCache(final String id) {
        if (null == id || "".equals(id)) {
            throw new IllegalArgumentException("mybatis redis cache need an id.");
        }
        this.id = id;
        log.debug("mybatis redis cache id: {}", id);

        // init Serializers
        initSerializers();
    }

    @SuppressWarnings("deprecation")
    private void initSerializers() {
        keySerializer = new StringRedisSerializer();
        valueSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        // 解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.setSerializationInclusion(Include.NON_NULL);
        ((Jackson2JsonRedisSerializer<Object>) valueSerializer).setObjectMapper(om);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        if (null == key) {
            return;
        }
        log.debug("mybatis redis cache put. key={}, value={}", key, value);
        RedisConnection conn = null;
        try {
            conn = redisConnectionFactory.getConnection();
            conn.setEx(keySerializer.serialize(key.toString()), MAX_EXPIRATION_SECONDS,
                valueSerializer.serialize(value));

            // 将key保存到redis.list中
            conn.lPush(keySerializer.serialize(id), keySerializer.serialize(key.toString()));
            conn.expire(keySerializer.serialize(id), MAX_EXPIRATION_SECONDS);
        } catch (Exception e) {
            log.error("mybatis redis cache put exception. key=" + key + ", value=" + value + "", e);
        } finally {
            if (null != conn) {
                conn.close();
            }
        }
    }

    @Override
    public Object getObject(Object key) {
        if (null == key) {
            return null;
        }
        log.debug("mybatis redis cache get. key={}", key);
        RedisConnection conn = null;
        Object result = null;
        try {
            conn = redisConnectionFactory.getConnection();
            result = valueSerializer.deserialize(conn.get(keySerializer.serialize(key.toString())));
        } catch (Exception e) {
            log.error("mybatis redis cache get exception. key=" + key + ", value=" + result + "", e);
        } finally {
            if (null != conn) {
                conn.close();
            }
        }
        return result;
    }

    @Override
    public Object removeObject(Object key) {
        if (null == key) {
            return null;
        }
        log.debug("mybatis redis cache remove. key={}", key);
        RedisConnection conn = null;
        Object result = null;
        try {
            conn = redisConnectionFactory.getConnection();
            // 将key设置为立即过期
            result = conn.expireAt(keySerializer.serialize(key.toString()), 0);
            // 将key从redis.list中删除
            conn.lRem(keySerializer.serialize(id), 0, keySerializer.serialize(key.toString()));
        } catch (Exception e) {
            log.error("mybatis redis cache remove exception. key=" + key + ", value=" + result + "", e);
        } finally {
            if (null != conn) {
                conn.close();
            }
        }
        return result;
    }

    @Override
    public void clear() {
        log.debug("mybatis redis cache clear. ");
        RedisConnection conn = null;
        try {
            conn = redisConnectionFactory.getConnection();
            Long length = conn.lLen(keySerializer.serialize(id));
            if (0 == length) {
                return;
            }
            List<byte[]> keyList = conn.lRange(keySerializer.serialize(id), 0, length - 1);
            for (byte[] key : keyList) {
                conn.expireAt(key, 0);
            }
            conn.expireAt(keySerializer.serialize(id), 0);
            keyList.clear();
        } catch (Exception e) {
            log.error("mybatis redis cache clear exception. ", e);
        } finally {
            if (null != conn) {
                conn.close();
            }
        }
    }

    @Override
    public int getSize() {
        int result = 0;
        try {
            RedisConnection redisConnection = redisConnectionFactory.getConnection();
            result = Math.toIntExact(redisConnection.dbSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }
}