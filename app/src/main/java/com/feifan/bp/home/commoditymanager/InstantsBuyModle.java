package com.feifan.bp.home.commoditymanager;

import android.util.Log;

import com.feifan.bp.network.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 商品管理 — 闪购商品model
 * Created by konta on 2015/12/25.
 */
public class InstantsBuyModle extends BaseModel{
    public static final String TAG = "InstantsBuyModle";
    /**
     * 闪购商品列表状态：
     *      00：临时保存
     *      01：待审核
     *      02：已通过
     *      04：已驳回
     */
    private static final String INSTANTS_STATUS_TEMPSAVE = "00";
    private static final String INSTANTS_STATUS_AUDIT = "01";
    private static final String INSTANTS_STATUS_THROUGHED = "02";
    private static final String INSTANTS_STATUS_REJECTED = "03";


    CommodityEntry commodityEntry;

    public InstantsBuyModle(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        Log.e(TAG, "we got json =======" + json);
        JSONObject data = new JSONObject(json);
        commodityEntry = new CommodityEntry();
        commodityEntry.tempSave = data.getInt(INSTANTS_STATUS_TEMPSAVE);
        commodityEntry.audit = data.getInt(INSTANTS_STATUS_AUDIT);
        commodityEntry.throughed = data.getInt(INSTANTS_STATUS_THROUGHED);
        commodityEntry.rejected = data.getInt(INSTANTS_STATUS_REJECTED);
    }

    public CommodityEntry getCommodityEntry(){
        return commodityEntry;
    }

    /**
     * 闪购商品
     */
    public class CommodityEntry {
        public int tempSave;
        public int audit;
        public int throughed;
        public int rejected;
    }

}
