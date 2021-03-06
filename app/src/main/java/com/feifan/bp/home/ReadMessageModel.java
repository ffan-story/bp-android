package com.feifan.bp.home;

import android.text.TextUtils;

import com.feifan.bp.base.envir.EnvironmentManager;
import com.feifan.bp.base.network.BaseModel;

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


    public int mIntSystemMessCount;
    public int mIntNoticeMessCount;

    public ReadMessageModel(JSONObject json) {
        super(json);
    }

    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
        JSONObject messageJson = data.getJSONObject("message");
        messageCount = messageJson.optInt("total");
        mIntSystemMessCount = messageJson.optInt("system");
        mIntNoticeMessCount = messageJson.optInt("notice");
        JSONObject component = data.getJSONObject("component");

        if (!TextUtils.isEmpty(component.optString(EnvironmentManager.getAuthFactory().getRefundId()))) {
            refundCount = component.optInt(EnvironmentManager.getAuthFactory().getRefundId());
        }
    }

}
