package com.feifan.bp.network;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by xuchunlei on 15/10/25.
 */
public abstract class BaseRequest<M> extends Request<M> {

    protected BaseRequest(RequestBuilder<M> builder) {
        super(builder.mMethod, builder.mUrl, builder.mErrorListener);
    }

    /**
     * 请求构建器
     * Created by xuchunlei on 15/10/25.
     */
    public abstract static class RequestBuilder<M> {

        /** 请求方式,GET/POST */
        protected int mMethod;

        /** 请求Url */
        protected String mUrl;
        /** 回调Listener */
        protected Response.Listener<M> mListener;
        /** 回调ErrorListener*/
        protected Response.ErrorListener mErrorListener;

        protected String mRequestBody;

        /**
         * 构造指定方式的请求
         * @param method
         * @return
         */
        public RequestBuilder(int method, String url, Response.Listener<M> listener) {
            this.mMethod = method;
            this.mUrl = url;
            this.mListener = listener;
        }

        public abstract  <T extends BaseRequest<M>> T build();
    }


}
