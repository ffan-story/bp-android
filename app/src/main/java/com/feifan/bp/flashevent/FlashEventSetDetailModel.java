package com.feifan.bp.flashevent;

import com.feifan.bp.network.BaseModel;
import com.feifan.bp.settings.helpcenter.HelpCenterModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by congjing
 */
public class FlashEventSetDetailModel extends BaseModel {


    private FlashEventSetDetailData mGoodsData;
    private int totalCount;
    private ArrayList<FlashEventSetDetailData> arryFlashEventData;

    public FlashEventSetDetailModel(JSONObject json) {
        super(json);
        for (int i = 0; i < 3; i++) {
            mGoodsData = new FlashEventSetDetailData();
            mGoodsData.mStrGoodsName="绿色植物";
            arryFlashEventData.add(mGoodsData);
        }
    }


    @Override
    protected void parseData(String json) throws JSONException {
//        arryListHelpCenterData = new ArrayList<FlashEventSetDetailData>();
//        JSONObject jsonObject = new JSONObject(json);
//
//        setTotalCount(jsonObject.optInt("totalCount"));
//        JSONArray itemsArray = jsonObject.getJSONArray("items");
//        for (int i = 0; i < itemsArray.length(); i++) {
//            strHelpCenterData = new lashEventSetDetailData();
//            try {
//                strHelpCenterData.setmStrHelpCenterTitle(itemsArray.getJSONObject(i).optString("title"));
//                strHelpCenterData.setmStrHelpCenterId(itemsArray.getJSONObject(i).optString("id"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            arryListHelpCenterData.add(strHelpCenterData);
 //       }
    }



    public class FlashEventSetDetailData {
        public String mStrGoodsName;
        public String mStrGoodsNumber;
        public String mStrGoodsAmount;
        public String mStrGoodsPartakeNumber;
    }
}
