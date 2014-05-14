package com.techcamp.disaster.data;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.techcamp.disaster.Config;
import com.techcamp.disaster.LoginActivity;
import com.techcamp.disaster.models.Site;
import com.techcamp.disaster.models.User;
import com.techcamp.disaster.utils.SSLHack;
import com.techcamp.disaster.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * Created by joseph on 5/10/14.
 */
public class DataManager {

    private static final String TAG = DataManager.class.getName();
    private static DataManager INSTANCE;
    private RequestQueue reqQueue;


    private DataManager() {
    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();

        }
        return INSTANCE;
    }

    public void init(Context c) {
        reqQueue = Volley.newRequestQueue(c);

        SSLHack.allowAllSSL();
    }

    public void querySiteList(Context context, final OnSiteQueryCompleteListener onSiteQueryCompleteListener) {

        String url = Config.SERVICE_URL + Config.LOCATION_CLEARANCES;
        OAuthArrayRequest request = new OAuthArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Site> sites = DataHelper.getSitesFromJson(response);
                onSiteQueryCompleteListener.onQueryComplete(sites);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "messge = " + error.getMessage());
                onSiteQueryCompleteListener.onFailure();
            }
        }, getAuthHeader(context));
        request.setShouldCache(false);
        request.setCacheEntry(null);
        reqQueue.add(request);
    }

    public void postInspectionResults(Context context, long id, String notes, int status, final OnInspectionListener listener) {
        String url = null;
        try {
            url = Config.SERVICE_URL + Config.INSPECTION_STATUS + "?id=" + id + "&inspector_note="
                    + URLEncoder.encode(notes, "UTF-8") + "&inspector_decision=" + status;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OAuthRequest request = new OAuthRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onQueryComplete();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "messge = " + error.getMessage());
                listener.onFailure();
            }
        }, getAuthHeader(context));
        request.setShouldCache(false);
        request.setCacheEntry(null);
        reqQueue.add(request);
    }


    public void querySiteDetail(Context context, long id, final OnSiteDetailListener onSiteDetailListener) {
        String url = Config.SERVICE_URL + Config.LOCATION_CLEARANCES_DETAIL + "/" + id + ".json";
        OAuthRequest request = new OAuthRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Site site = DataHelper.getSiteFromJson(response);
                onSiteDetailListener.onQueryComplete(site);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "messge = " + error.getMessage());
                onSiteDetailListener.onFailure();
            }
        }, getAuthHeader(context));
        request.setShouldCache(false);
        request.setCacheEntry(null);
        reqQueue.add(request);

//        try {
//            JSONObject result = new JSONObject(Utils.getSampleResponse(context, "site_detail_response"));
//            onSiteDetailListener.onQueryComplete(DataHelper.getSiteFromJson(result));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    public void requestAuthentication(Context context, final UserLoginListener userLoginListener) {
        String url = Config.SERVICE_URL + Config.AUTHENTICATION;
        OAuthArrayRequest request = new OAuthArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                userLoginListener.onSuccess();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "messge = " + error.getMessage());
                userLoginListener.onFailure();
            }
        }, getAuthHeader(context));
        request.setShouldCache(false);
        request.setCacheEntry(null);
        reqQueue.add(request);
    }

    HashMap<String, String> getAuthHeader(Context context) {
        HashMap<String, String> params = new HashMap<String, String>();
        User user = Utils.getCurrentUserData(context);

        String headerString = user.getEmail() + ":" + user.getPassword();
        params.put("Authorization", "Basic " + Base64.encodeToString(headerString.getBytes(), Base64.NO_WRAP));
        return params;
    }
}
