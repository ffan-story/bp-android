package com.feifan.bp.network;

import com.android.volley.Response.ErrorListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Get请求
 *
 * Created by xuchunlei on 15/10/27.
 */
public class GetRequest<T> extends JsonRequest<T> {

    public GetRequest(String url, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
    }

    public static class Builder<T> {

        private String mUrl;
        private Map<String, String> mParams;
        private ErrorListener mErrorListener;

        public Builder(String url){
            this.mUrl = url;
            mParams = new HashMap<String, String>();
        }

        public Builder<T> errorListener(ErrorListener listener) {
            mErrorListener = listener;
            return this;
        }

        public Builder<T> param(String key, String value) {
            mParams.put(key,value);
            return this;
        }

        public GetRequest<T> build(){
            if(!mParams.isEmpty()) {
                mUrl = mUrl.concat("?");
                for(Map.Entry<String, String> entry : mParams.entrySet()){
                    mUrl = mUrl.concat(entry.getKey()).concat("=").concat(entry.getValue()).concat("&");
                }
                mUrl = mUrl.substring(0, mUrl.length() - 1);
            }
            return new GetRequest<T>(mUrl, mErrorListener);
        }
    }
}
