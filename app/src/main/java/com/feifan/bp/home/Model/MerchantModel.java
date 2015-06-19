package com.feifan.bp.home.Model;

import org.json.JSONObject;

/**
 * Created by xuchunlei on 15/6/19.
 */
public class MerchantModel extends CenterModel {

    public MerchantModel(JSONObject json) {
        id = json.optInt("merchantId");
        primaryName = json.optString("merchantName");
        logoSrc = json.optJSONObject("brands").optString("brandLogo");
        secondaryName = json.optString("brandNames");
    }
}
