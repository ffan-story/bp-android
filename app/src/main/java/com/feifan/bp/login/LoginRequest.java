package com.feifan.bp.login;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.feifan.bp.factory.FactorySet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by xuchunlei on 15/6/18.
 */
public class LoginRequest extends Request<UserModel> {

    private static final String URL = FactorySet.getUrlFactory().getFFanHostUrl() + "xadmin/login";

    private Map<String, String> mParams;

    private Listener<UserModel> mListener;

    public LoginRequest(Listener<UserModel> listener, ErrorListener errorListener, Map<String, String> params) {
        super(Method.POST, URL, errorListener);
        mParams = params;
        mListener = listener;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected Response<UserModel> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String jsonStr = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            JSONObject json = new JSONObject(jsonStr);
            return Response.success(new UserModel(json.optJSONObject("data")), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void deliverResponse(UserModel userModel) {
        mListener.onResponse(userModel);
    }

}
