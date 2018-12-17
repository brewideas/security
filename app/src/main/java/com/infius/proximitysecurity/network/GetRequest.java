package com.infius.proximitysecurity.network;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.google.gsonhtcfix.Gson;
import com.infius.proximitysecurity.model.DataModel;

import java.util.Map;

/**
 * Created by kshemendra on 31/01/18.
 */

public class GetRequest extends Request<DataModel> {

    private final Response.Listener<DataModel> mListener;
    private DataModel mDataModel;
    private Map<String, String> mHeaders;
    private String TAG = GetRequest.class.getName();
    public static final int MY_SOCKET_TIMEOUT_MS = 60000;
    private String mUrl;
    private String mRequestBody;

    public GetRequest(String url, Response.Listener<DataModel> listener, Response.ErrorListener
            errorListener, DataModel model, Map headers, String requestBody) {
        super(Method.GET, url, errorListener);
        mListener = listener;
        mDataModel = model;
        mUrl = url;
        mRequestBody = requestBody;
        mHeaders = headers;
        printRequest();
    }

    private void printRequest() {
        if (!TextUtils.isEmpty(mUrl)) {
            Log.d(TAG, "URL : " + mUrl);
        }

        if (mHeaders != null) {
            Log.d(TAG, "Headers : " + mHeaders.toString());
        }

        if (!TextUtils.isEmpty(mRequestBody)) {
            Log.d(TAG, "RequestBody : " + mRequestBody);
        }
    }

    @Override
    protected Response<DataModel> parseNetworkResponse(NetworkResponse response) {
        String jsonString = "";
        int statusCode = response.statusCode;
        jsonString = new String(response.data);
        try {
            Log.d(TAG, jsonString);
            Gson gson = new Gson();
            String jData = new String(response.data);

            mDataModel = gson.fromJson(jData, mDataModel.getClass());
            return Response.success(mDataModel, getCacheEntry());
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void deliverResponse(DataModel response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        retryPolicy = new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        return super.setRetryPolicy(retryPolicy);
    }
}
