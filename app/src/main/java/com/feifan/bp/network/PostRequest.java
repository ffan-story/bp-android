package com.feifan.bp.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;

import java.util.HashMap;
import java.util.Map;

/**
 * POST请求
 * Created by xuchunlei on 15/10/26.
 */
public class PostRequest<T extends BaseModel> extends JsonRequest<T> {

    private Map<String, String> mParams = new HashMap<>();

    public PostRequest(String url, ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
    }

    public PostRequest<T> param(String key, String value) {
        mParams.put(key, value);
        return this;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }
}
