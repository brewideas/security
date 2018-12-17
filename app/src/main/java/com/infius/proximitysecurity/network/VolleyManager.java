package com.infius.proximitysecurity.network;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.infius.proximitysecurity.utilities.AppConstants;
import com.infius.proximitysecurity.utilities.Utils;

import java.io.File;

/**
 * Created by kshemendra on 31/01/18.
 */

public class VolleyManager {
    private static RequestQueue mRequestQueue;
    private static RequestQueue mImageRequestQueue;
    private static String TAG = VolleyManager.class.getSimpleName();
    private static Context mContext;

    @VisibleForTesting
    static final String CACHE_DIRECTORY_NAME = "gokhale-volley-cache";

    /**
     * initialize Volley
     */
    public static void init(Context context) {
        mContext = context;
        getRequestQueue(context);
        mImageRequestQueue = Volley.newRequestQueue(context);
        ImageCacheManager.INSTANCE.initImageCache();
    }

    public static <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setShouldCache(true);
        getRequestQueue(mContext).add(req);
    }

    public  static <T> Request<T> addSyncRequest(Request<T> req){
        return getRequestQueue(mContext).add(req);
    }

    public static RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            File volleyCacheDir = new File(context.getCacheDir().getPath() + File.separator
                    + CACHE_DIRECTORY_NAME);

            Cache cache = new DiskBasedCache(volleyCacheDir, (int) Utils.diskCacheSizeBytes
                    (volleyCacheDir, AppConstants.TEN_MB));
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }

        return mRequestQueue;
    }

    public static RequestQueue getImageRequestQueue() {

        if (mImageRequestQueue != null) {
            return mImageRequestQueue;
        } else {
            throw new IllegalStateException("Image RequestQueue not initialized");
        }
    }
}
