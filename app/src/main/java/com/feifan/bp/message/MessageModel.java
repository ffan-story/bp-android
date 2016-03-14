package com.feifan.bp.message;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 系统消息Model
 * Created by apple on 16/3/3.
 */
public class MessageModel extends BaseModel {
    private MessageData mStrMessageData;
    public int totalCount;
    public ArrayList<MessageData> messageDataList;
    public MessageModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        totalCount = jsonObject.optInt("totalCount");
        JSONArray dataArray = jsonObject.getJSONArray("data");
        messageDataList = new ArrayList<MessageData>();
        for (int i = 0; i < dataArray.length(); i++) {
            mStrMessageData = new MessageData();
            try {
                mStrMessageData.mStrMailInboxId = dataArray.getJSONObject(i).optString("mailInboxId");
                mStrMessageData.mStrMessId = dataArray.getJSONObject(i).optString("userId");
                mStrMessageData.mStrMessStatus = dataArray.getJSONObject(i).optString("mailStatus");
                mStrMessageData.mStrDetailUrl = dataArray.getJSONObject(i).optString("detailH5Url");
                mStrMessageData.mStrMessSniper = dataArray.getJSONObject(i).optString("content");
                mStrMessageData.mStrMessTime = dataArray.getJSONObject(i).optString("createTime_text");
                mStrMessageData.mStrMessSender = dataArray.getJSONObject(i).optString("sender");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            messageDataList.add(mStrMessageData);
        }
    }

    public class MessageData {
        public String mStrMailInboxId;
        public String mStrMessSniper;
        public String mStrMessTime;
        public String mStrMessSender;
        public String mStrMessId;
        public String mStrMessStatus;
        public String mStrDetailUrl;
    }
}