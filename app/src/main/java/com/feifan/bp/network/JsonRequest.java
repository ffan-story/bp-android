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

import android.os.Bundle;
import android.os.Message;

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
import com.feifan.bp.PlatformState;
import com.feifan.bp.LaunchActivity;
import com.feifan.bp.UserProfile;
import com.feifan.bp.util.LogUtil;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Json请求
 * <p>
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
     * 静态 LaunchActivity 用于获取 Handle ，发送Handle message
     */
//    public static  LaunchActivity myJsonLunchActivity;

    /**
     * 更新冗余参数内容
     *
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
        final String cookie = PlatformState.getInstance().retrieveCookie();
        if(PlatformState.getInstance().retrieveCookie() != null){
            mHeaders.put(Constants.COOKIE_KEY, cookie);
        }
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

//            if (BuildConfig.DEBUG){
//                Message message = new Message();
//                message.what = 1;
//                Bundle mBundle = new Bundle();
//                mBundle.putString("MESSAGE", formatJson(jsonString));
//                message.setData(mBundle);
//                myJsonLunchActivity.myHandler.sendMessage(message);
//            }

            // 保存最新Cookie
            if(response.headers != null) {
                String cookies = ProtocolHelper.pickCookies(response.headers.toString());
                if(cookies != null) {
                    PlatformState.getInstance().updateCookie(cookies);
                    LogUtil.i(TAG, "Update cookies to " + cookies);
                }
            }

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
     * <p>
     * <pre>
     *     可以根据错误码进行处理
     * </pre>
     */
    public static class StatusError extends VolleyError {

        /** 错误状态－Cookie过期 */
        public static final int STATUS_COOKIE_EXPIRE = 3001;

        private final int mStatus;

        public StatusError(int status, String message) {
            super(message);
            mStatus = status;
        }

        public int getStatus() {
            return mStatus;
        }
    }


    /**
     * 单位缩进字符串。
     */
    private static String SPACE = "   ";
    /**
     * 返回格式化JSON字符串。
     *
     * @param json 未格式化的JSON字符串。
     * @return 格式化的JSON字符串。
     */
    public String formatJson(String json)
    {
        StringBuffer result = new StringBuffer();

        int length = json.length();
        int number = 0;
        char key = 0;
        //遍历输入字符串。
        for (int i = 0; i < length; i++) {
            //1、获取当前字符。
            key = json.charAt(i);
            if((key == '[') || (key == '{') ) {  //2、如果当前字符是前方括号、前花括号做如下处理：
                if((i - 1 > 0) && (json.charAt(i - 1) == ':')){//（1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
                    result.append('\n');
                    result.append(indent(number));
                }
                //（2）打印：当前字符。
                result.append(key);
                //（3）前方括号、前花括号，的后面必须换行。打印：换行。
                result.append('\n');
                //（4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                number++;
                result.append(indent(number));
                //（5）进行下一次循环。
                continue;
            }

            //3、如果当前字符是后方括号、后花括号做如下处理：
            if((key == ']') || (key == '}') ) {
                //（1）后方括号、后花括号，的前面必须换行。打印：换行。
                result.append('\n');
                //（2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
                number--;
                result.append(indent(number));
                //（3）打印：当前字符。
                result.append(key);
                //（4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
                if(((i + 1) < length) && (json.charAt(i + 1) != ',')) {
                    result.append('\n');
                }
                //（5）继续下一次循环。
                continue;
            }

            //4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
            if((key == ',')) {
                result.append(key);
                result.append('\n');
                result.append(indent(number));
                continue;
            }
            //5、打印：当前字符。
            result.append(key);
        }
        return result.toString();
    }

    /**
     * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
     *
     * @param number 缩进次数。
     * @return 指定缩进次数的字符串。
     */
    private String indent(int number) {
        StringBuffer result = new StringBuffer();
        for(int i = 0; i < number; i++) {
            result.append(SPACE);
        }
        return result.toString();
    }

}
