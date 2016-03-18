package com.feifan.bp.xg;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

/**
 * Created by mengmeng on 16/3/4.
 */
public class XGPushManger {
    private static XGPushManger mXGPushManger;
    private Context mContext;
    private String mToken;

    public static XGPushManger getInstance(Context context) {
        if (mXGPushManger == null) {
            synchronized (XGPushManger.class) {
                if (mXGPushManger == null) {
                    mXGPushManger = new XGPushManger(context);
                }
            }
        }
        return mXGPushManger;
    }

    private XGPushManger(Context context) {
        mContext = context;
    }

    public void registerApp() {
        XGPushManager.registerPush(mContext);
        XGPushManager.startPushService(mContext);
        Intent service = new Intent(mContext, XGPushService.class);
        mContext.startService(service);
    }

    public void registerPush() {
        if (mContext == null) {
            throw new IllegalArgumentException("the uuid or context can not bu null");
        }
        XGPushManager.registerPush(mContext);
    }

    public void registerApp(String uuid) {
        if (TextUtils.isEmpty(uuid) || mContext == null) {
            throw new IllegalArgumentException("the uuid or context can not bu null");
        }
        XGPushManager.registerPush(mContext, uuid,
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        Log.d("TPush", "注册成功，设备token为：" + data);
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                    }
                });
    }

    public void unRegister() {
        if (mContext == null) {
            throw new IllegalArgumentException("the uuid or context can not bu null");
        }
        XGPushManager.registerPush(mContext, "*");
    }
}
