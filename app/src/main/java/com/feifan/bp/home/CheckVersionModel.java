package com.feifan.bp.home;

import com.feifan.bp.base.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by maning on 15/7/28.
 */
public class CheckVersionModel extends BaseModel {

    private int versionCode;
    private String versionUrl;

    public CheckVersionModel(JSONObject json) {
        super(json, false);
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    @Override
    protected void parseData(JSONObject data) {
        versionCode = data.optInt("version_code");
        versionUrl = data.optString("version_url");
    }

    @Override
    protected void parseArrayData(JSONArray data) {

    }
}
