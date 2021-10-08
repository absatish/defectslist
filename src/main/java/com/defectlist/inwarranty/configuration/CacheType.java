package com.defectlist.inwarranty.configuration;

public enum CacheType {

    SESSION("session");

    private final String cacheName;

    CacheType(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getCacheName() {
        return cacheName;
    }
}
