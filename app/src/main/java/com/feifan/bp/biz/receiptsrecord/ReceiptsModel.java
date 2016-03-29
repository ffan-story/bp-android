package com.feifan.bp.biz.receiptsrecord;

import android.util.Log;

import com.feifan.bp.base.network.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 首款流水model
 * Created by konta on 2016/3/17.
 */
public class ReceiptsModel extends BaseModel {

    public String totalCount;   //订单总数
    public List<ReceiptsRecord> receiptsList;
    private ReceiptsRecord record;

    public ReceiptsModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        Log.e("ReceiptsModel", "----" + json);
        JSONObject data = new JSONObject(json);
        totalCount = data.optString("totalCount");
        JSONArray datas = data.getJSONArray("list");
        receiptsList = new ArrayList<>();
        if(datas != null && datas.length() > 0){
            for(int i = 0; i <datas.length(); i++){
                record = new ReceiptsRecord();
                record.mPayType = datas.getJSONObject(i).optString("payType");
                record.mRecordNo = datas.getJSONObject(i).optString("orderNo");
                record.mStatus = datas.getJSONObject(i).optString("orderStatus");
                record.mReceiptsTime = datas.getJSONObject(i).optString("payTime");
                record.mReceiptsAmount = datas.getJSONObject(i).optString("orderAmt");
                record.mRealAmount = datas.getJSONObject(i).optString("realPayAmt");
                receiptsList.add(record);
            }
        }
    }

    public class ReceiptsRecord{
        public String mPayType;         // 付款类型
        public String mRecordNo;        // 流水单号
        public String mStatus;          // 状态
        public String mReceiptsTime;    // 收款时间
        public String mReceiptsAmount;  // 收款金额
        public String mRealAmount;      // 可开发票金额
    }

}


