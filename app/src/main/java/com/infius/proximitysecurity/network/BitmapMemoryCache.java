package com.infius.proximitysecurity.network;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.infius.proximitysecurity.utilities.Utils;

/**
 * Created by kshemendra on 31/01/18.
 */

@SuppressLint("NewApi")
public class BitmapMemoryCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    private static final String TAG = "BitmapMemoryCache";

    public BitmapMemoryCache(int maxSize) {
        super(maxSize);
        Log.d(TAG, "Size: " + maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        if (Utils.getOSVersion() >= Build.VERSION_CODES.HONEYCOMB_MR1)
            return value.getByteCount();
        else
            return (value.getRowBytes() * value.getHeight());

    }

    @Override
    public Bitmap getBitmap(String url) {
        // Log.i(TAG, "getBitmap: " + url);
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        // Log.i(TAG, "putBitmap: " + url);
        put(url, bitmap);
    }

}

