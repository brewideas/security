package com.infius.proximitysecurity.network;


import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by kshemendra on 31/01/18.
 */


public enum ImageCacheManager {
    INSTANCE;

    private BitmapMemoryCache mBitmapMemoryCache;
    private ImageLoader mImageLoader;

    ImageCacheManager() {

    }

    public void initImageCache() {

        mBitmapMemoryCache = new BitmapMemoryCache(getCacheSize());

        //Log.i("Cache size:", "Cache size" + getCacheSize());

        mImageLoader = new ImageLoader(VolleyManager.getImageRequestQueue(), mBitmapMemoryCache);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public Bitmap getBitmap(String key) {
        return mBitmapMemoryCache.getBitmap(key);
    }

    private int getCacheSize() {

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        return cacheSize;
    }

    public void evictAll() {
        if (mBitmapMemoryCache != null)
            mBitmapMemoryCache.evictAll();

    }

    public void remove(String key, int maxWidth, int maxHeight) {
        if (mBitmapMemoryCache != null)
            mBitmapMemoryCache.remove(getCacheKey(key, maxWidth, maxHeight));
        //Log.i("ImageCacheManager:", "Removed from in Memory Cache:" + getCacheKey(key, maxWidth, maxHeight));
    }

    private static String getCacheKey(String url, int maxWidth, int maxHeight) {
        return new StringBuilder(url.length() + 12).append("#W").append(maxWidth).append("#H").append(maxHeight)
                .append(url).toString();
    }
}

