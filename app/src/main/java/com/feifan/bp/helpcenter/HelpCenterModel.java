package com.feifan.bp.helpcenter;

import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by congjing
 */
public class HelpCenterModel extends BaseModel {

    private HelpCenterData strHelpCenterData;
    private int totalCount;
    private ArrayList<HelpCenterData> arryListHelpCenterData;

    public HelpCenterModel(JSONObject json) {
        super(json);
    }


    @Override
    protected void parseData(String json) throws JSONException {
        arryListHelpCenterData = new ArrayList<HelpCenterData>();
        JSONObject jsonObject = new JSONObject(json);

        setTotalCount(jsonObject.optInt("totalCount"));
        JSONArray itemsArray = jsonObject.getJSONArray("items");
        for (int i = 0; i < itemsArray.length(); i++) {
            strHelpCenterData = new HelpCenterData();
            try {
                strHelpCenterData.setmStrHelpCenterTitle(itemsArray.getJSONObject(i).optString("title"));
                strHelpCenterData.setmStrHelpCenterId(itemsArray.getJSONObject(i).optString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            arryListHelpCenterData.add(strHelpCenterData);
        }
    }

    public ArrayList<HelpCenterData> getArryListHelpCenterData() {
        return arryListHelpCenterData;
    }

    public void setArryListHelpCenterData(ArrayList<HelpCenterData> arryListHelpCenterData) {
        this.arryListHelpCenterData = arryListHelpCenterData;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public HelpCenterData getStrHelpCenterData() {
        return strHelpCenterData;
    }

    public void setStrHelpCenterData(HelpCenterData strHelpCenterData) {
        this.strHelpCenterData = strHelpCenterData;
    }

    public class HelpCenterData {
        private String mStrHelpCenterId;
        private String mStrHelpCenterTitle;

        public String getmStrHelpCenterId() {
            return mStrHelpCenterId;
        }

        public void setmStrHelpCenterId(String mStrHelpCenterId) {
            this.mStrHelpCenterId = mStrHelpCenterId;
        }

        public String getmStrHelpCenterTitle() {
            return mStrHelpCenterTitle;
        }

        public void setmStrHelpCenterTitle(String mStrHelpCenterTitle) {
            this.mStrHelpCenterTitle = mStrHelpCenterTitle;
        }
    }
}
