package com.feifan.bp.net;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
//import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.io.File;

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
        mQueue = newRequestQueue(context, null);
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


    public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
        File cacheDir = new File(context.getCacheDir(), "wandabp");
        String userAgent = "wandabp/0";

        try {
            String network = context.getPackageName();
            PackageInfo queue = context.getPackageManager().getPackageInfo(network, 0);
            userAgent = network + "/" + queue.versionCode;
        } catch (PackageManager.NameNotFoundException var6) {

        }

        if(stack == null) {
            if(Build.VERSION.SDK_INT >= 9) {
                stack = new HttpsUrlStack();
            } else {
//                stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
            }
        }

        BasicNetwork network1 = new BasicNetwork((HttpStack)stack);
        RequestQueue queue1 = new RequestQueue(new DiskBasedCache(cacheDir), network1);
        queue1.start();
        return queue1;
    }

}
