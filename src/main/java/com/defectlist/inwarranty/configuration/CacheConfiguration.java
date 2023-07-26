package com.defectlist.inwarranty.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfiguration {

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }

    @Bean
    public CacheManager appCacheManager(final Ticker ticker) {
        final CaffeineCache cacheSessionId = new CaffeineCache(CacheType.SESSION.getCacheName(), Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(1000)
                .ticker(ticker)
                .build());
        final CaffeineCache cacheGridItems = new CaffeineCache(CacheType.GRID_ITEM.getCacheName(), Caffeine.newBuilder()
                .expireAfterWrite(1440, TimeUnit.MINUTES)
                .maximumSize(1000)
                .ticker(ticker)
                .build());
        final CaffeineCache cacheLineUrl = new CaffeineCache(CacheType.LINE_URL.getCacheName(), Caffeine.newBuilder()
                .expireAfterWrite(4, TimeUnit.MINUTES)
                .maximumSize(1000)
                .ticker(ticker)
                .build());

        final SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(cacheSessionId, cacheGridItems, cacheLineUrl));
        return cacheManager;
    }
}
