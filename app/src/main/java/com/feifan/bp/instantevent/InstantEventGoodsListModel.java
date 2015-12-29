package com.feifan.bp.instantevent;

import java.util.ArrayList;

/**
 * Created by congjing
 */
public class InstantEventGoodsListModel {

    private GoodsListData goodsListData;
    private int totalCount;

    public ArrayList<GoodsListData> arryListGoodsData =new ArrayList<>();

    public InstantEventGoodsListModel(String name, String status, String id) {
        for (int i = 0; i < 70; i++) {
            goodsListData = new GoodsListData();
            goodsListData.mStrGoodsName =  name;
            goodsListData.mStrGoodsId =  id;
           if (i>4){
               goodsListData.mStrGoodsStatus =  "0";
           }else{
               goodsListData.mStrGoodsStatus =  status;
           }
            arryListGoodsData.add(goodsListData);
        }
    }


   /* public InstantEventGoodsListModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
       *//* arryListGoodsData = new ArrayList<GoodsListData>();
        JSONObject jsonObject = new JSONObject(json);

        setTotalCount(jsonObject.optInt("totalCount"));
        JSONArray itemsArray = jsonObject.getJSONArray("items");
        for (int i = 0; i < itemsArray.length(); i++) {
            goodsListData = new GoodsListData();
            try {
                goodsListData.mStrGoodsName =  itemsArray.getJSONObject(i).optString("title");
                goodsListData.mStrGoodsId =  itemsArray.getJSONObject(i).optString("title");
                goodsListData.mStrGoodsStatus =  itemsArray.getJSONObject(i).optString("title");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            arryListGoodsData.add(goodsListData);
        }*//*
    }*/

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
    }
}
