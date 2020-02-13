package com.info.baymax.common.cache;

import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static org.springframework.util.StringUtils.isEmpty;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.info.baymax.common.cache.annotation.CacheDuration;

public class CustomRedisCacheManager extends RedisCacheManager implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;

    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                                   boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation);
    }

    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                                   String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheNames);
    }

    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                                   Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations);
    }

    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                                   Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        parseCacheDuration(applicationContext);
    }

    private void parseCacheDuration(ApplicationContext applicationContext) {
        final Map<String, Long> cacheExpires = new HashMap<>();
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        for (String beanName : beanNames) {
            final Class<?> clazz = applicationContext.getType(beanName);
            Service service = findAnnotation(clazz, Service.class);
            Component component = findAnnotation(clazz, Component.class);
            if (null == service && null == component) {
                continue;
            }
            addCacheExpires(clazz, cacheExpires);
        }
    }

    private void addCacheExpires(final Class<?> clazz, final Map<String, Long> cacheExpires) {
        ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                ReflectionUtils.makeAccessible(method);
                // 根据CacheDuration注解获取时间
                CacheDuration cacheDuration = findCacheDuration(clazz, method);
                if (cacheDuration != null) {
                    Cacheable cacheable = findAnnotation(method, Cacheable.class);
                    CacheConfig cacheConfig = findAnnotation(clazz, CacheConfig.class);
                    Set<String> cacheNames = findCacheNames(cacheConfig, cacheable);
                    for (String cacheName : cacheNames) {
                        // cacheExpires.put(cacheName,
                        // cacheDuration.duration());
                        getCacheConfigurations().getOrDefault(cacheName, RedisCacheConfiguration.defaultCacheConfig())
                                .entryTtl(Duration.ofSeconds(cacheDuration.duration()));
                    }
                }
            }
        }, new ReflectionUtils.MethodFilter() {
            @Override
            public boolean matches(Method method) {
                return null != findAnnotation(method, Cacheable.class);
            }
        });
    }

    // CacheDuration标注的有效期，优先使用方法上标注的有效期
    private CacheDuration findCacheDuration(Class<?> clazz, Method method) {
        CacheDuration methodCacheDuration = findAnnotation(method, CacheDuration.class);
        if (null != methodCacheDuration) {
            return methodCacheDuration;
        }
        CacheDuration classCacheDuration = findAnnotation(clazz, CacheDuration.class);
        if (null != classCacheDuration) {
            return classCacheDuration;
        }
        return null;
    }

    private Set<String> findCacheNames(CacheConfig cacheConfig, Cacheable cacheable) {
        return isEmpty(cacheable.value()) ? newHashSet(cacheConfig.cacheNames()) : newHashSet(cacheable.value());
    }

    /**
     * Entry point for builder style {@link RedisCacheManager} configuration.
     *
     * @param connectionFactory must not be {@literal null}.
     * @return new {@link RedisCacheManagerBuilder}.
     */
    public static CustomRedisCacheManagerBuilder customBuilder(RedisConnectionFactory connectionFactory) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");

        return CustomRedisCacheManagerBuilder.fromConnectionFactory(connectionFactory);
    }

    /**
     * Entry point for builder style {@link RedisCacheManager} configuration.
     *
     * @param cacheWriter must not be {@literal null}.
     * @return new {@link RedisCacheManagerBuilder}.
     */
    public static CustomRedisCacheManagerBuilder customBuilder(RedisCacheWriter cacheWriter) {

        Assert.notNull(cacheWriter, "CacheWriter must not be null!");

        return CustomRedisCacheManagerBuilder.fromCacheWriter(cacheWriter);
    }

    /**
     * Configurator for creating {@link RedisCacheManager}.
     *
     * @author Christoph Strobl
     * @author Mark Strobl
     * @author Kezhu Wang
     * @since 2.0
     */
    public static class CustomRedisCacheManagerBuilder {

        private final RedisCacheWriter cacheWriter;
        private RedisCacheConfiguration defaultCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        private final Map<String, RedisCacheConfiguration> initialCaches = new LinkedHashMap<>();
        private boolean enableTransactions;
        boolean allowInFlightCacheCreation = true;

        private CustomRedisCacheManagerBuilder(RedisCacheWriter cacheWriter) {
            this.cacheWriter = cacheWriter;
        }

        /**
         * Entry point for builder style {@link RedisCacheManager}
         * configuration.
         *
         * @param connectionFactory must not be {@literal null}.
         * @return new {@link RedisCacheManagerBuilder}.
         */
        public static CustomRedisCacheManagerBuilder fromConnectionFactory(RedisConnectionFactory connectionFactory) {

            Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");

            return customBuilder(new CustomRedisCacheWriter(connectionFactory));
        }

        /**
         * Entry point for builder style {@link RedisCacheManager}
         * configuration.
         *
         * @param cacheWriter must not be {@literal null}.
         * @return new {@link RedisCacheManagerBuilder}.
         */
        public static CustomRedisCacheManagerBuilder fromCacheWriter(RedisCacheWriter cacheWriter) {

            Assert.notNull(cacheWriter, "CacheWriter must not be null!");

            return new CustomRedisCacheManagerBuilder(cacheWriter);
        }

        /**
         * Define a default {@link RedisCacheConfiguration} applied to
         * dynamically created {@link RedisCache}s.
         *
         * @param defaultCacheConfiguration must not be {@literal null}.
         * @return this {@link RedisCacheManagerBuilder}.
         */
        public CustomRedisCacheManagerBuilder cacheDefaults(RedisCacheConfiguration defaultCacheConfiguration) {

            Assert.notNull(defaultCacheConfiguration, "DefaultCacheConfiguration must not be null!");

            this.defaultCacheConfiguration = defaultCacheConfiguration;

            return this;
        }

        /**
         * Enable {@link RedisCache}s to synchronize cache put/evict operations
         * with ongoing Spring-managed transactions.
         *
         * @return this {@link RedisCacheManagerBuilder}.
         */
        public CustomRedisCacheManagerBuilder transactionAware() {

            this.enableTransactions = true;

            return this;
        }

        /**
         * Append a {@link Set} of cache names to be pre initialized with
         * current {@link RedisCacheConfiguration}. <strong>NOTE:</strong> This
         * calls depends on {@link #cacheDefaults(RedisCacheConfiguration)}
         * using whatever default {@link RedisCacheConfiguration} is present at
         * the time of invoking this method.
         *
         * @param cacheNames must not be {@literal null}.
         * @return this {@link RedisCacheManagerBuilder}.
         */
        public CustomRedisCacheManagerBuilder initialCacheNames(Set<String> cacheNames) {

            Assert.notNull(cacheNames, "CacheNames must not be null!");

            Map<String, RedisCacheConfiguration> cacheConfigMap = new LinkedHashMap<>(cacheNames.size());
            cacheNames.forEach(it -> cacheConfigMap.put(it, defaultCacheConfiguration));

            return withInitialCacheConfigurations(cacheConfigMap);
        }

        /**
         * Append a {@link Map} of cache name/{@link RedisCacheConfiguration}
         * pairs to be pre initialized.
         *
         * @param cacheConfigurations must not be {@literal null}.
         * @return this {@link RedisCacheManagerBuilder}.
         */
        public CustomRedisCacheManagerBuilder withInitialCacheConfigurations(
                Map<String, RedisCacheConfiguration> cacheConfigurations) {

            Assert.notNull(cacheConfigurations, "CacheConfigurations must not be null!");
            cacheConfigurations.forEach((cacheName, configuration) -> Assert.notNull(configuration,
                    String.format("RedisCacheConfiguration for cache %s must not be null!", cacheName)));

            this.initialCaches.putAll(cacheConfigurations);

            return this;
        }

        /**
         * Disable in-flight {@link org.springframework.cache.Cache} creation
         * for unconfigured caches.
         * <p/>
         * {@link RedisCacheManager#getMissingCache(String)} returns
         * {@literal null} for any unconfigured
         * {@link org.springframework.cache.Cache} instead of a new
         * {@link RedisCache} instance. This allows eg.
         * {@link org.springframework.cache.support.CompositeCacheManager} to
         * chime in.
         *
         * @return this {@link RedisCacheManagerBuilder}.
         * @since 2.0.4
         */
        public CustomRedisCacheManagerBuilder disableCreateOnMissingCache() {

            this.allowInFlightCacheCreation = false;
            return this;
        }

        /**
         * Create new instance of {@link RedisCacheManager} with configuration
         * options applied.
         *
         * @return new instance of {@link RedisCacheManager}.
         */
        public CustomRedisCacheManager build() {

            CustomRedisCacheManager cm = new CustomRedisCacheManager(cacheWriter, defaultCacheConfiguration,
                    initialCaches, allowInFlightCacheCreation);

            cm.setTransactionAware(enableTransactions);

            return cm;
        }
    }
}
