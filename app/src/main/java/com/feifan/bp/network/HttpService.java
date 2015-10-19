package com.feifan.bp.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

/**
 * 网络服务
 * <p>
 *     提供网络请求、加载图片及上传服务
 * </p>
 * Created by xuchunlei on 15/10/19.
 */
public class HttpService {

    private final static HttpService INSTANCE = new HttpService();

    //请求队列
    private RequestQueue mQueue;

    private HttpService(){}

    public void initialize(Context context) {
        if(mQueue == null) {
            mQueue = Volley.newRequestQueue(context);
        }
    }

    public static HttpService getInstance(){
        return INSTANCE;
    }

    /**
     * 异步请求Json数据
     * @param request
     */
    public void requestAsync(JsonObjectRequest request) {
        mQueue.add(request);
    }
}
