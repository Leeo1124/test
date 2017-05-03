package com.leeo.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;

public class MemcachedCacheManager extends AbstractCacheManager {

    private Collection<Cache> caches;

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return this.caches;
    }

    public void setCaches(Collection<Cache> caches) {
        this.caches = caches;
    }

    @Override
    public Cache getCache(String name) {
        return super.getCache(name);
    }

}