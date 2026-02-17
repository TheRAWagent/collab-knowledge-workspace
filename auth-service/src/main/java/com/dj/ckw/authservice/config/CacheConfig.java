package com.dj.ckw.authservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * Cache configuration with fallback support.
 * Uses in-memory SimpleCacheManager as the default when Redis is not available.
 * This allows the application to start without Redis connectivity.
 */
@Configuration
public class CacheConfig {
    /**
     * Provides a fallback in-memory cache manager.
     * This bean is used only if no other CacheManager bean is defined,
     * which happens when Redis is not available or not configured.
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(Set.of(new ConcurrentMapCache("users")));
        return simpleCacheManager;
    }
}
