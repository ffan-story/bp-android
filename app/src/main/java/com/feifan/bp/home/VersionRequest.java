package com.feifan.bp.home;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.feifan.bp.BuildConfig;
import com.feifan.bp.Constants;
import com.feifan.bp.LogUtil;
import com.feifan.bp.factory.FactorySet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by xuchunlei on 15/7/7.
 */
public class VersionRequest extends Request<VersionModel> {

    private static final String TAG = "VersionRequest";

    private static final String URL = FactorySet.getUrlFactory().getFFanHostUrl() + "xadmin/getversioninfo?appType=bpMobile";

    private Response.Listener<VersionModel> mListener;

    public VersionRequest(Response.Listener<VersionModel> listener, Response.ErrorListener errorListener) {
        super(Method.GET, URL, errorListener);
        mListener = listener;
        LogUtil.i(TAG, getUrl());
    }

    @Override
    protected Response<VersionModel> parseNetworkResponse(NetworkResponse networkResponse) {

        String jsonStr = null;
        String errStr = null;
        try {
            jsonStr = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            LogUtil.i(TAG, jsonStr);
            JSONObject json = new JSONObject(jsonStr);
            int status = json.optInt("status");
            if(status == Constants.RESPONSE_CODE_SUCCESS) {
                return Response.success(new VersionModel(json.optJSONObject("data")), HttpHeaderParser.parseCacheHeaders(networkResponse));
            }else {
                LogUtil.w(TAG, "error status:" + jsonStr);
                errStr = json.optString("msg");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            LogUtil.w(TAG, "Response:" + jsonStr);
            e.printStackTrace();
        }

        return Response.error(new VolleyError(errStr));

    }

    @Override
    protected void deliverResponse(VersionModel versionModel) {
        mListener.onResponse(versionModel);
    }
}
