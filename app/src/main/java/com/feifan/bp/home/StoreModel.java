package com.feifan.bp.home;


import com.feifan.bp.base.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Frank on 15/10/28.
 */
public class StoreModel extends BaseModel {

    private StoreDetailModel storeDetailModel;

    private ArrayList<StoreDetailModel> storeList;

    public StoreModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);

        JSONArray data = new JSONArray(json);
        storeList = new ArrayList<StoreDetailModel>();
        for (int i = 0; i < data.length(); i++) {
            storeDetailModel = new StoreDetailModel();
            try {
                storeDetailModel.setStoreId(data.getJSONObject(i).optString("storeId"));
                storeDetailModel.setStoreName(data.getJSONObject(i).optString("storeName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            storeList.add(storeDetailModel);
        }
    }

    public ArrayList<StoreDetailModel> getStoreList() {
        return storeList;
    }

    public class StoreDetailModel {

        private String storeId;
        private String storeName;

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }
    }
}
