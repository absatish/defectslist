package com.defectlist.inwarranty.configuration;

public enum CacheType {

    GRID_ITEM("grid-item"),
    SESSION("session"),
    LINE_URL("line-url");

    private final String cacheName;

    CacheType(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getCacheName() {
        return cacheName;
    }
}
