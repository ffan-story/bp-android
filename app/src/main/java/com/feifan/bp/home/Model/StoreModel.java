package com.feifan.bp.home.Model;

import org.json.JSONObject;

/**
 * Created by xuchunlei on 15/6/19.
 */
public class StoreModel extends CenterModel {

    public StoreModel(JSONObject json) {
        id = json.optInt("storeId");
        primaryName = json.optString("storeName");
        logoSrc = json.optString("storePicsrc");
        if(json.isNull("plazaName")) {
            secondaryName = null;
        } else {
            secondaryName = json.optString("plazaName");
        }
    }
}
