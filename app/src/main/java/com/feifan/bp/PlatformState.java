package com.feifan.bp;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.feifan.bp.login.UserProfile;

/**
 * 状态类
 * <p>
 *     存取平台相关的状态变量，目前包括登录信息
 * </p>
 * Created by xuchunlei on 15/6/17.
 */
public class PlatformState {

    private static final String TAG = "PlatformState";

    private static PlatformState INSTANCE;
    // 供全局使用的Context
    private static Context sContext;

    private UserProfile mProfile;

    // 网络请求队列
    private RequestQueue mQueue;

    private PlatformState(){
        mProfile = new UserProfile(sContext);
        mQueue = Volley.newRequestQueue(sContext);

    }

    public static PlatformState getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PlatformState();
        }
        return INSTANCE;
    }

    public static void setApplicationContext(Context context) {
        if (sContext != null) {
            Log.w(TAG, "setApplicationContext called twice! old=" + sContext + " new=" + context);
        }
        sContext = context.getApplicationContext();
    }

    public UserProfile getUserProfile() {
        return mProfile;
    }

    public RequestQueue getRequestQueue() {
        return mQueue;
    }
}
