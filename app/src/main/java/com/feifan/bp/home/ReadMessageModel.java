package com.feifan.bp.home;

import android.text.TextUtils;

import com.feifan.bp.network.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tianjun on 2015-11-30.
 */
public class ReadMessageModel extends BaseModel {
    public static final int REFUND_INDEX = 1162;
    public static final String MESSAGE_KEY = "message";
    public static final String COMPONENT_KEY = "component";

    /**
     * 消息
     */
    private String message;
    /**
     * 退款售后
     */
    private String refund;


    public ReadMessageModel(JSONObject json) {
        super(json);
    }

    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
        message = data.optString(MESSAGE_KEY);
        JSONObject component = data.getJSONObject(COMPONENT_KEY);
        if (!TextUtils.isEmpty(component.optString(String.valueOf(REFUND_INDEX)))) {
            refund = component.optString(String.valueOf(REFUND_INDEX));
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }
}
