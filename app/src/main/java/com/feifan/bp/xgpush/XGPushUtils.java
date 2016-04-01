package com.feifan.bp.xgpush;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.biz.receiptsrecord.ReceiptsFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mengmeng on 16/3/31.
 */
public class XGPushUtils {
    private static final String TYPE_KEY = "type";
    private static final String PAYFLOWID_KEY = "payflowid";
    public static  final String PAYFLOW_TYPE = "1";

    public static String getPayFlowId(String customContent, Context context){
        if(!TextUtils.isEmpty(customContent)){
            if (!TextUtils.isEmpty(customContent)) {
                try {
                    JSONObject obj = new JSONObject(customContent);
                    String payFlowId = "";
                    if(!obj.isNull(PAYFLOWID_KEY)){
                        payFlowId = obj.getString(PAYFLOWID_KEY);
                    }
                    return payFlowId;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String getTypeId(String customContent, Context context){
        if(!TextUtils.isEmpty(customContent)){
            if (!TextUtils.isEmpty(customContent)) {
                try {
                    JSONObject obj = new JSONObject(customContent);
                    String typeValue = "";
                    // key1为前台配置的key
                    if (!obj.isNull(TYPE_KEY)) {
                        typeValue = obj.getString(TYPE_KEY);
                    }
                    return typeValue;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static Intent getPayFlowIntent(String payFlowId){
        Intent intent = new Intent(PlatformState.getApplicationContext(), NOPActivity.class);
        intent.putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO, ReceiptsFragment.class.getName());
        intent.putExtra(Constants.EXTRA_KEY_TITLE, PlatformState.getApplicationContext().getString(R.string.receipts_title));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ReceiptsFragment.PAY_FLOW_ID, payFlowId);
        return intent;
    }

    public static Intent[] getPayFlowIntents(String payFlowId){
        Intent[] intents = new Intent[2];
        intents[0] = Intent.makeRestartActivityTask(new ComponentName(PlatformState.getApplicationContext(), com.feifan.bp.LaunchActivity.class));
        intents[1] = new Intent(PlatformState.getApplicationContext(), NOPActivity.class);
        intents[1].putExtra(OnFragmentInteractionListener.INTERATION_KEY_TO, ReceiptsFragment.class.getName());
        intents[1].putExtra(Constants.EXTRA_KEY_TITLE, PlatformState.getApplicationContext().getString(R.string.receipts_title));
        intents[1].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intents[1].putExtra(ReceiptsFragment.PAY_FLOW_ID, payFlowId);
        return intents;
    }
}
