package com.aeiric.thumb.lib;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.LruCache;

/**
 * @author xujian
 * @desc ThumbCache
 * @from v1.0.0
 */
class ThumbCache {

    private LruCache<String, Bitmap> mLruCache;


    /**
     * @param maxCacheSize 最大缓存容量(byte)
     */
    ThumbCache(int maxCacheSize) {
        mLruCache = new LruCache<String, Bitmap>(maxCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    void cacheBitmap(@NonNull String key, @NonNull Bitmap bitmap) {
        if (getBitmap(key) == null) {
            mLruCache.put(key, bitmap);
        }
    }

    Bitmap getBitmap(@NonNull String key) {
        return mLruCache.get(key);
    }

    void clear() {
        mLruCache.evictAll();
    }
}
