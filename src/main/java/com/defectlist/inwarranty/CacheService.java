package com.defectlist.inwarranty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CacheService {

    private final CacheManager cacheManager;

    @Autowired
    public CacheService(final CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public <T> Optional<T> get(final String cacheName, final String key, final Class<T> type) {
        final Cache.ValueWrapper wrapper = Objects.requireNonNull(cacheManager.getCache(cacheName)).get(key);
        if (wrapper == null || wrapper.get() == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(type.cast(wrapper.get()));
    }

    public void put(final String cacheName, final String key, final Object object) {
        cacheManager.getCache(cacheName).put(key, object);
    }
}
