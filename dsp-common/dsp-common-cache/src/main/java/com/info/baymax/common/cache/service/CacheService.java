package com.info.baymax.common.cache.service;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.utils.ICollections;

/**
 * 说明： 缓存管理. <br>
 *
 * @author yjw@jusfoun.com
 * @date 2017年9月22日 上午9:51:19
 */
public interface CacheService {

    /**
     * 说明：获取一个缓存管理器. <br>
     *
     * @return 缓存管理器
     * @author yjw@jusfoun.com
     * @date 2018年9月6日 下午2:32:44
     */
    CacheManager getCacheManager();

    /**
     * 说明： 获取所有的缓存名称列表. <br>
     *
     * @return 缓存名称列表
     * @throws ServiceException
     * @author yjw@jusfoun.com
     * @date 2017年9月22日 上午9:51:01
     */
    default Collection<String> getCacheNames() throws ServiceException {
        return getCacheManager().getCacheNames();
    }

    /**
     * 说明： 向指定的缓存实例中存入键值. <br>
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @param value     缓存值
     * @throws ServiceException
     * @author yjw@jusfoun.com
     * @date 2017年9月22日 上午9:50:30
     */
    default void put(String cacheName, String key, String value) throws ServiceException {
        Cache cache = get(cacheName);
        if (cache != null) {
            cache.put(key, value);
        }
    }

    /**
     * 说明： 向指定的缓存实例中存入键值. <br>
     *
     * @param cacheName
     * @param key
     * @param value
     * @throws ServiceException
     * @author yjw@jusfoun.com
     * @date 2017年9月22日 上午9:50:06
     */
    default void put(String cacheName, String key, Object value) throws ServiceException {
        Cache cache = get(cacheName);
        if (cache != null) {
            cache.put(key, value);
        }
    }

    /**
     * 说明： 获取指定名称的缓存实例. <br>
     *
     * @param cacheName 缓存名称
     * @return
     * @throws ServiceException
     * @author yjw@jusfoun.com
     * @date 2017年9月22日 上午9:49:19
     */
    default Cache get(String cacheName) throws ServiceException {
        return getCacheManager().getCache(cacheName);
    }

    /**
     * 说明： 获取缓存数据. <br>
     *
     * @param cacheName 缓存实例名称
     * @param key       缓存键
     * @return
     * @throws ServiceException
     * @author yjw@jusfoun.com
     * @date 2017年9月22日 上午9:48:49
     */
    default Object get(String cacheName, String key) throws ServiceException {
        Cache cache = get(cacheName);
        if (cache != null) {
            return cache.get(key);
        }
        return null;
    }

    /**
     * 说明： 删除缓存数据. <br>
     *
     * @param cacheName 缓存实例名称
     * @param key       缓存键
     * @throws ServiceException
     * @author yjw@jusfoun.com
     * @date 2017年9月22日 上午9:48:33
     */
    default void evict(String cacheName, String key) throws ServiceException {
        Cache cache = get(cacheName);
        if (cache != null) {
            cache.evict(key);
        }
    }

    /**
     * 说明： 清空指定实例的缓存. <br>
     *
     * @param cacheName 缓存实例名称
     * @throws ServiceException
     * @author yjw@jusfoun.com
     * @date 2017年9月22日 上午9:48:20
     */
    default void clear(String cacheName) throws ServiceException {
        Cache cache = get(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }

    /**
     * 说明：清空所有的缓存. <br>
     *
     * @throws ServiceException
     * @author yjw@jusfoun.com
     * @date 2017年9月22日 上午9:48:10
     */
    default void clear() throws ServiceException {
        Collection<String> cacheNames = getCacheNames();
        if (ICollections.hasElements(cacheNames)) {
            Cache cache = null;
            for (String cacheName : cacheNames) {
                cache = get(cacheName);
                if (cache != null) {
                    cache.clear();
                }
            }
        }
    }
}