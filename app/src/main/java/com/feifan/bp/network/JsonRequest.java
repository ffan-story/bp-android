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

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.feifan.bp.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Json请求
 *
 * <pre>
 *     可以发送JSON信息体或key-value值作为参数，并得到T类型的对象
 * </pre>
 *
 * @param <T> JSON type of response expected
 */
public class JsonRequest<T> extends Request<T> {

    protected static final String TAG = "Request";

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

    public JsonRequest<T> listener(Listener listener) {
        mListener = listener;
        return this;
    }

    public JsonRequest<T> targetClass(Class<T> clazz) {
        mClazz = clazz;
        return this;
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
                return Response.success(constructor.newInstance(new JSONObject(jsonString)),
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (NoSuchMethodException e) {
            Response.error(new ParseError(e));
        } catch (InvocationTargetException e) {
            Response.error(new ParseError(e));
        } catch (InstantiationException e) {
            Response.error(new ParseError(e));
        } catch (IllegalAccessException e) {
            Response.error(new ParseError(e));
        } catch (JSONException e) {
            Response.error(new ParseError(e));
        }
        return null;
    }
}
