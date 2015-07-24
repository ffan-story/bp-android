package com.feifan.bp.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by maning on 15/7/27.
 */
public class DefaultHttpProcessor implements HttpProcessor, RequestQueue.RequestFinishedListener {

    private static DefaultHttpProcessor sHttpProcessor;

    private RequestQueue mQueue;

    public static synchronized DefaultHttpProcessor instance(Context context) {
        if (sHttpProcessor == null) {
            sHttpProcessor = new DefaultHttpProcessor(context);
        }
        return sHttpProcessor;
    }

    private DefaultHttpProcessor(Context context) {
        mQueue = Volley.newRequestQueue(context);
        mQueue.addRequestFinishedListener(this);
        mQueue.start();
    }

    @Override
    public void onRequestFinished(Request request) {
        if (request instanceof BaseRequest) {
            ((BaseRequest) request).onFinish();
        }
    }

    @Override
    public void process(BaseRequest request) {
        mQueue.add(request);
    }

}
