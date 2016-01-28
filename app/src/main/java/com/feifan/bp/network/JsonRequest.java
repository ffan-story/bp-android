/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.feifan.bp.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.feifan.bp.BuildConfig;
import com.feifan.bp.Constants;
import com.feifan.bp.UserProfile;
import com.feifan.bp.util.LogUtil;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Json请求
 *
 * <pre>
 *     可以发送JSON信息体或key-value值作为参数，并得到T类型的对象
 * </pre>
 *
 * @param <T> JSON type of response expected
 */
public class JsonRequest<T extends BaseModel> extends Request<T> {

    protected static final String TAG = "Request";

    protected static Map<String, String> REDUNDANT_PARAMS;

    private Map<String, String> mHeaders = new HashMap<String, String>();

    /**
     * 更新冗余参数内容
     * @param profile
     */
    public static void updateRedundantParams(UserProfile profile) {
        REDUNDANT_PARAMS = new HashMap<String, String>();
        REDUNDANT_PARAMS.put("appType", "bpMobile");
        REDUNDANT_PARAMS.put("clientType", "Android");
        REDUNDANT_PARAMS.put("version", String.valueOf(BuildConfig.VERSION_CODE));
//            mParams.put("clientAgent", )
        REDUNDANT_PARAMS.put("uid", String.valueOf(UserProfile.getInstance().getUid()));
        REDUNDANT_PARAMS.put("applicant", String.valueOf(UserProfile.getInstance().getUid()));//2016-1-11加操作者id
        REDUNDANT_PARAMS.put("agid", UserProfile.getInstance().getAuthRangeId());
        REDUNDANT_PARAMS.put("loginToken", UserProfile.getInstance().getLoginToken());
    }

    /**
     * 编码
     */
    protected static final String PROTOCOL_CHARSET = "utf-8";

    private Listener<T> mListener;

    protected Class<T> mClazz;

    public JsonRequest(int method, String url,
                       ErrorListener errorListener) {
        super(method, url, errorListener);
        LogUtil.i(TAG, "url=" + url + " by " + method + " method.");
    }

    public JsonRequest<T> header(String key, String value) {
        mHeaders.put(key, value);
        return this;
    }

    public JsonRequest<T> listener(Listener listener) {
        mListener = listener;
        return this;
    }

    public JsonRequest<T> targetClass(Class<T> clazz) {
        mClazz = clazz;
        return this;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        LogUtil.i(TAG, "headers:" + mHeaders.toString());
        return mHeaders;
    }

    @Override
    protected void deliverResponse(T response) {
        if (null != mListener) {
            mListener.onResponse(response);
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            LogUtil.i(TAG, "Receive:" + jsonString);
            if (mClazz != null) {
                Constructor<T> constructor = mClazz.getConstructor(JSONObject.class);
                T t = constructor.newInstance(new JSONObject(jsonString));
                if(t.status == Constants.RESPONSE_CODE_SUCCESS) {
                    return Response.success(t, HttpHeaderParser.parseCacheHeaders(response));
                }else {
                    return Response.error(new StatusError(t.status, t.msg));
                }
            } else {
                throw new IllegalArgumentException("Please provide target class for your request!");
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "Fatal error:Parsing response to json model failed!");
            return Response.error(new ParseError(e));
        }
    }

    /**
     * 带有状态码的错误类
     *
     * <pre>
     *     可以根据错误码进行处理
     * </pre>
     */
    public static class StatusError extends VolleyError {
        private final int mStatus;

        public StatusError(int status, String message) {
            super(message);
            mStatus = status;
        }

        public int getStatus() {
            return mStatus;
        }
    }
}
