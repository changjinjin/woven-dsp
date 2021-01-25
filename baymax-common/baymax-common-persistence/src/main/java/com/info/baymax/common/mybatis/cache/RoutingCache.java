package com.info.baymax.common.mybatis.cache;

import com.info.baymax.common.mybatis.cache.redis.RedisCache;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.mybatis.caches.ehcache.EhcacheCache;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Cache 路由类，在Mapper接口上统一使用该类型作为Cache的实现类，但在实例化时根据配置文件动态的切换真正的实现类以达到缓存动态切换目的，目前支持redis和ehcache
 *
 * @author jingwei.yang
 * @date 2020年3月10日 下午3:24:38
 */
@Slf4j
public class RoutingCache implements Cache {

    private static final Map<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

    private final String id;

    /**
     * 缓存类型：redis,ehcache
     */
    @Setter
    private String cacheType;

    /**
     * 真实的cache实现类
     */
    private Cache cache;

    public RoutingCache(final String id) {
        if (null == id || "".equals(id)) {
            throw new IllegalArgumentException("Routing cache need an id.");
        }
        this.id = id;
    }

    public Cache getCache() {
        cache = caches.get(id);
        if (cache == null) {
            // 根据配置决定cache实现
            Class<? extends Cache> cacheImplementation = getCacheImplementation();
            synchronized (id) {
				cache = newBaseCacheInstance(cacheImplementation, id);
			}
            caches.put(id, cache);
            if (log.isDebugEnabled()) {
                log.debug("create Routing Cache:id={}, implementation={}", id, cacheImplementation.getName());
            }
        }
        return cache;
    }

    public Class<? extends Cache> getCacheImplementation() {
        switch (cacheType) {
            case "redis":
                return RedisCache.class;
            case "ehcache":
                return EhcacheCache.class;
            default:
                return PerpetualCache.class;
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        getCache().putObject(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return getCache().getObject(key);
    }

    @Override
    public Object removeObject(Object key) {
        return getCache().removeObject(key);
    }

    @Override
    public void clear() {
        getCache().clear();
    }

    @Override
    public int getSize() {
        return getCache().getSize();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return getCache().getReadWriteLock();
    }

    private Cache newBaseCacheInstance(Class<? extends Cache> cacheClass, String id) {
        Constructor<? extends Cache> cacheConstructor = getBaseCacheConstructor(cacheClass);
        try {
            return cacheConstructor.newInstance(id);
        } catch (Exception e) {
            throw new CacheException("Could not instantiate cache implementation (" + cacheClass + "). Cause: " + e, e);
        }
    }

    private Constructor<? extends Cache> getBaseCacheConstructor(Class<? extends Cache> cacheClass) {
        try {
            return cacheClass.getConstructor(String.class);
        } catch (Exception e) {
            throw new CacheException("Invalid base cache implementation (" + cacheClass + ").  "
                + "Base cache implementations must have a constructor that takes a String id as a parameter.  Cause: "
                + e, e);
        }
    }
}
