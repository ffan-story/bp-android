package com.feifan.bp.xgpush;

/**
 * Created by mengmeng on 16/3/14.
 */

import android.content.Context;
import android.util.Log;

import com.feifan.bp.PlatformState;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * Created by mengmeng on 16/3/7.
 */
public class XGPushMsgReceiver  extends XGPushBaseReceiver {

    private final String TAG = "MessageReceiver";
    private final String TYPE_KEY = "type";
    private final String PAYFLOWID_KEY = "payflowid";
    private final String PAYFLOW_TYPE = "1";
    private static int mNum = 0;

    @Override
    public void onRegisterResult(Context context, int errorCode,
                                 XGPushRegisterResult message) {
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = message + "注册成功";
            // 在这里拿token
            String token = message.getToken();
        } else {
            text = message + "注册失败，错误码：" + errorCode;
            if(mNum < 5) {
                PlatformState.getInstance().onAccountChange();
                mNum ++;
            }
        }
        Log.e(TAG, text);
    }


    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "反注册成功";
        } else {
            text = "反注册失败" + errorCode;
        }
        Log.d(TAG, text);
    }

    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"设置成功";
        } else {
            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
        }
        Log.d(TAG, text);
    }

    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }
        Log.d(TAG, text);


    }


    // 消息透传
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        String msg = message.getContent();
        Log.e("onTextMessage",msg);
    }


    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
    @Override
    public void onNotifactionClickedResult(Context context,
                                           XGPushClickedResult message) {
//        if (context == null || message == null) {
//            return;
//        }
//        if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
//            String customContent = message.getCustomContent();
//            Log.e("content",customContent);
//            if (!TextUtils.isEmpty(customContent)) {
//                try {
//                    JSONObject obj = new JSONObject(customContent);
//                    String typeValue = "";
//                    String payFlowId = "";
//                    // key1为前台配置的key
//                    if (!obj.isNull(TYPE_KEY)) {
//                        typeValue = obj.getString(TYPE_KEY);
//                    }
//                    if(!obj.isNull(PAYFLOWID_KEY)){
//                        payFlowId = obj.getString(PAYFLOWID_KEY);
//                    }
//                    if(PAYFLOW_TYPE.equals(typeValue) && !TextUtils.isEmpty(payFlowId)){
//                        final UserProfile profile = UserProfile.getInstance();
//                        if(profile != null && profile.getUid() != -1){
//                            FmsAgent.onEvent(context.getApplicationContext(), Statistics.FB_PUSHMES_READ);
//                            ReceiptsFragment.start(payFlowId);
//                        }else {
//                            PlatformTopbarActivity.startActivityFromOther(PlatformState.getApplicationContext(), LoginFragment.class.getName(), context.getString(R.string.login_login_text));
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            return;
//        } else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
//
//        }
    }

    // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context,
                                          XGPushShowedResult notifiShowedRlt) {
        if (context == null || notifiShowedRlt == null) {
            return;
        }
    }
}
