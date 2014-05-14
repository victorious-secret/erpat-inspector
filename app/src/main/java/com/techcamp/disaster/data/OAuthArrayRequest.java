package com.techcamp.disaster.data;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by joseph on 5/10/14.
 */
public class OAuthArrayRequest extends JsonRequest<JSONArray> {

    Map<String,String> params;
    Map<String,String> headers;


    public OAuthArrayRequest(int method, String url, Map<String, String> params,
                        Response.Listener<JSONArray> listener,
                        Response.ErrorListener errorListener,
                        Map<String, String> headers) {
        super(method, url, null, listener, errorListener);
        this.params = params;
        this.headers = headers;
    }

    @Override
    public Map<String, String> getHeaders() {
        return  headers;
    }

    String toParams(Map<String, String> params) {
        ArrayList<String> paramsArray = new ArrayList<String>();
        for (String key : params.keySet()) {
            String value = params.get(key);
            try {
                paramsArray.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return StringUtils.join(paramsArray, "&");
    }

    @Override
    public byte[] getBody() {
        try {
            if (params!=null) {
                return toParams(params).toString().getBytes("UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getBodyContentType() {
        return null;
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

}
