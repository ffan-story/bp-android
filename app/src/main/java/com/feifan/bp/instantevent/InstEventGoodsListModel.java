package com.feifan.bp.instantevent;


import com.feifan.bp.network.BaseModel;
import com.feifan.bp.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by congjing
 */

public class InstEventGoodsListModel extends BaseModel {

    private GoodsListData goodsListData;
    private int totalCount;

    public ArrayList<GoodsListData> getArryListGoodsData() {
        return arryListGoodsData;
    }

    private ArrayList<GoodsListData> arryListGoodsData ;

    public InstEventGoodsListModel(JSONObject json) {

        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {

        LogUtil.i("congjing","json=="+json);
        JSONObject jsonObject = new JSONObject(json);
        setTotalCount(jsonObject.optInt("totalCount"));
        JSONArray itemsArray = jsonObject.getJSONArray("list");
        arryListGoodsData =new ArrayList<>();
        LogUtil.i("congjing", " itemsArray.length()==" + itemsArray.length());
        for (int i = 0; i < itemsArray.length(); i++) {
            goodsListData = new GoodsListData();
            try {
                goodsListData.mStrGoodsName =  itemsArray.getJSONObject(i).optString("name");
                goodsListData.mStrGoodsId =  itemsArray.getJSONObject(i).optString("code");
                goodsListData.mStrGoodsStatus =  itemsArray.getJSONObject(i).optString("enroll_status");
                goodsListData.mStrGoodsStatusTxt =  itemsArray.getJSONObject(i).optString("approveStatus_text");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            arryListGoodsData.add(goodsListData);
            LogUtil.i("congjing", " arryListGoodsData.length()==" + arryListGoodsData.size());
        }
    }


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public class GoodsListData {
        public String mStrGoodsName;
        public String mStrGoodsId;
        public String mStrGoodsStatus;
        public String mStrGoodsStatusTxt;
    }
}
