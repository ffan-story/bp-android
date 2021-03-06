package com.feifan.bp.base.network;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Get请求
 *
 * Created by xuchunlei on 15/10/27.
 */
public class GetRequest<T extends BaseModel> extends JsonRequest<T> {

    public GetRequest(String url, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
    }

    public static class Builder<T extends BaseModel> {

        private String mUrl;
        private Map<String, String> mParams;
        private Response.ErrorListener mErrorListener;

        public Builder(String url){
            this.mUrl = url;
            mParams = new HashMap<String, String>();
            mParams.putAll(JsonRequest.REDUNDANT_PARAMS);

            mErrorListener = new DefaultErrorListener();
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
                if (!mUrl.contains("?")){
                    mUrl = mUrl.concat("?");
                }else{
                    mUrl = mUrl.concat("&");
                }

                for(Map.Entry<String, String> entry : mParams.entrySet()){
                    if(entry.getValue() != null) {
                        mUrl = mUrl.concat(entry.getKey()).concat("=").concat(entry.getValue()).concat("&");
                    }
                }
                mUrl = mUrl.substring(0, mUrl.length() - 1);
            }
            return new GetRequest<T>(mUrl, mErrorListener);
        }


    }
}
