package com.feifan.bp.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.feifan.bp.Constants;
import com.feifan.bp.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Frank on 16/2/16.
 */
public class CookieRequest<T extends BaseModel> extends JsonRequest<T> {

    protected static final String TAG = "CookieRequest";

    private String mHeader;

    public String cookieFromResponse;

    private Map<String, String> mParams = new HashMap<>();

    public      CookieRequest(String url, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mParams.putAll(JsonRequest.REDUNDANT_PARAMS);
    }

    public CookieRequest<T> param(String key, String value) {
        mParams.put(key, value);
        return this;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            LogUtil.i(TAG, "Receive:" + jsonString);
            mHeader = response.headers.toString();
            LogUtil.i(TAG, "get headers in parseNetworkResponse " + response.headers.toString());
            //使用正则表达式从reponse的头中提取cookie内容的子串
            Pattern pattern = Pattern.compile("Set-Cookie.*/;");
            Matcher m = pattern.matcher(mHeader);
            if (m.find()) {
                cookieFromResponse = m.group();
                LogUtil.i(TAG, "cookie from server " + cookieFromResponse);
            }
            //去掉cookie末尾的分号
            cookieFromResponse = cookieFromResponse.substring(11, cookieFromResponse.length() - 1);
            LogUtil.i(TAG, "cookie substring " + cookieFromResponse);
            if (mClazz != null) {
                Constructor<T> constructor = mClazz.getConstructor(JSONObject.class);
                //将cookie字符串添加到jsonObject中，该jsonObject会被deliverResponse递交，调用请求时则能在onResponse中得到
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONObject data = jsonObject.getJSONObject("data");
                data.put(Constants.COOKIE_KEY, cookieFromResponse);
                LogUtil.i(TAG, "jsonObject " + jsonObject.toString());
                T t = constructor.newInstance(jsonObject);
                if (t.status == Constants.RESPONSE_CODE_SUCCESS) {
                    return Response.success(t, HttpHeaderParser.parseCacheHeaders(response));
                } else {
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

    @Override
    protected void deliverResponse(T response) {
        super.deliverResponse(response);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        LogUtil.i(TAG, mParams.toString());
        return mParams;
    }

}
