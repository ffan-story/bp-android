package com.feifan.bp.instantevent;


import com.feifan.bp.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * 添加商品类别model
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
        JSONObject jsonObject = new JSONObject(json);
        setTotalCount(jsonObject.optInt("totalCount"));
        JSONArray itemsArray = jsonObject.getJSONArray("list");
        arryListGoodsData =new ArrayList<>();
        for (int i = 0; i < itemsArray.length(); i++) {
            goodsListData = new GoodsListData();
            try {
                goodsListData.mStrGoodsName =  itemsArray.getJSONObject(i).optString("name");
                goodsListData.mStrGoodsId =  itemsArray.getJSONObject(i).optString("code");
                goodsListData.mStrGoodsStatus =  itemsArray.getJSONObject(i).optString("enroll_status");//判断是否可以点击 －－0:可提交-可点击，1:已提交 -不可点击
                goodsListData.mStrGoodsStatusTxt =  itemsArray.getJSONObject(i).optString("status_text");//状态显示的文字
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arryListGoodsData.add(goodsListData);
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
