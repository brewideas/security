package com.infius.proximitysecurity.utilities;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.infius.proximitysecurity.model.DataModel;
import com.infius.proximitysecurity.model.GuestHistoryModel;
import com.infius.proximitysecurity.model.InvitationModel;
import com.infius.proximitysecurity.model.QRInfoResponse;
import com.infius.proximitysecurity.network.GetRequest;
import com.infius.proximitysecurity.network.PostRequest;
import com.infius.proximitysecurity.network.VolleyManager;

import java.util.HashMap;

public class ApiRequestHelper {

    public static void requestGuestList(Context context, String param, Response.Listener<DataModel> listener, Response.ErrorListener errorListener) {
        Uri.Builder builder = new Uri.Builder();
        builder.appendPath("http://34.231.195.192:9090/services/proximity/api/guest/list");
        builder.appendQueryParameter("param", "HISTORY");
//        String url = builder.build().toString();

        String url = "http://34.231.195.192:9090/services/proximity/api/guest/list?param=" + param;
        GetRequest request = new GetRequest(url, listener, errorListener, new GuestHistoryModel(), null, null);
        VolleyManager.getRequestQueue(context).add(request);
    }

    public static void requestInvitation(Context context, String requestBody, Response.Listener<DataModel> listener, Response.ErrorListener errorListener) {
        String url = "http://34.231.195.192:9090/services/proximity/api/guest";
        HashMap<String, String> header = new HashMap<>();
        header.put("content-type", "application/json");
        PostRequest request = new PostRequest(url, listener, errorListener, new InvitationModel(), header, requestBody);
        VolleyManager.getRequestQueue(context).add(request);
    }

    public static void requestQRInfo(Context context, String qrCodeId, Response.Listener<DataModel> listener, Response.ErrorListener errorListener) {
//        String url = "http://34.231.195.192:9090/services/proximity/api/guest/list?param=" + param;
        String url = "http://34.231.195.192:9090/services/proximity/api/guest?qrCode=" + qrCodeId;
        GetRequest request = new GetRequest(url, listener, errorListener, new QRInfoResponse(), null, null);
        VolleyManager.getRequestQueue(context).add(request);
    }
}
