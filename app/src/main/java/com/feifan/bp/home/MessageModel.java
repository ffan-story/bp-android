package com.feifan.bp.home;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by apple on 15-11-2.
 */
public class MessageModel extends BaseModel {

    private MessageData mStrMessageData;

    private ArrayList<MessageData> messageDataList;

    public MessageModel(JSONObject json) {
        super(json);
    }


    @Override
    protected void parseData(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray dataArray = jsonObject.optJSONArray("data");
        messageDataList = new ArrayList<MessageData>();
        for (int i = 0; i < dataArray.length(); i++) {
            mStrMessageData = new MessageData();
            try {
                mStrMessageData.setmStrMessageId(dataArray.getJSONObject(i).optString("userId"));
                mStrMessageData.setmStrMessageTitle(dataArray.getJSONObject(i).optString("content"));
                mStrMessageData.setmStrMessageTime(dataArray.getJSONObject(i).optString("createTime"));
                mStrMessageData.setmStrMessageStatus(dataArray.getJSONObject(i).optString("mailStatus"));
                mStrMessageData.setmStrMessageStatus(dataArray.getJSONObject(i).optString("detailH5Url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            messageDataList.add(mStrMessageData);
        }
    }

    public ArrayList<MessageData> getMessageDataList() {
        return messageDataList;
    }

    public class MessageData {
        private String mStrMessageId;
        private String mStrMessageTitle;
        private String mStrMessageTime;
        private String mStrMessageStatus;
        private String mStrDetailUrl;

        public String getmStrMessageStatus() {
            return mStrMessageStatus;
        }

        public void setmStrMessageStatus(String mStrMessageStatus) {
            this.mStrMessageStatus = mStrMessageStatus;
        }

        public String getmStrMessageTime() {
            return mStrMessageTime;
        }

        public void setmStrMessageTime(String mStrMessageTime) {
            this.mStrMessageTime = mStrMessageTime;
        }

        public String getmStrMessageTitle() {
            return mStrMessageTitle;
        }

        public void setmStrMessageTitle(String mStrMessageTitle) {
            this.mStrMessageTitle = mStrMessageTitle;
        }

        public String getmStrMessageId() {
            return mStrMessageId;
        }

        public void setmStrMessageId(String mStrMessageId) {
            this.mStrMessageId = mStrMessageId;
        }

        public String getmStrDetailUrl() {
            return mStrDetailUrl;
        }

        public void setmStrDetailUrl(String mStrDetailUrl) {
            this.mStrDetailUrl = mStrDetailUrl;
        }
    }
}
