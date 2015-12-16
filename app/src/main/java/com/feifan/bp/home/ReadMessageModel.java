package com.feifan.bp.home;

import android.text.TextUtils;

import com.feifan.bp.BuildConfig;
import com.feifan.bp.envir.AuthSupplier;
import com.feifan.bp.envir.EnvironmentManager;
import com.feifan.bp.network.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tianjun on 2015-11-30.
 */
public class ReadMessageModel extends BaseModel {
    public static final String MESSAGE_KEY = "message";
    public static final String COMPONENT_KEY = "component";

    /**
     * 消息未读数
     */
    public int messageCount;
    /**
     * 退款售后未读数
     */
    public int refundCount;


    public ReadMessageModel(JSONObject json) {
        super(json);
    }

    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
        messageCount = data.optInt("message");
        JSONObject component = data.getJSONObject("component");

        if (!TextUtils.isEmpty(component.optString(EnvironmentManager.getAuthFactory().getRefundId()))) {
            refundCount = component.optInt(EnvironmentManager.getAuthFactory().getRefundId());
        }
    }

}
