package com.feifan.bp;

import com.feifan.bp.base.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by maning on 15/8/7.
 */
public class VersionUpdateModel extends BaseModel {

    private int versionCode;
    private String versionDesc;
    private String versionUrl;
    private int mustUpdate;

    public VersionUpdateModel(JSONObject json) {
        super(json, false);
    }
    @Override
    protected void parseArrayData(JSONArray data) {

    }

    @Override
    protected void parseData(JSONObject data) {
        versionCode = data.optInt("versionCode");
        versionDesc = data.optString("versionDesc");
        versionUrl = data.optString("versionUrl");
        mustUpdate = data.optInt("mustUpdate");
    }

    public int getMustUpdate() {
        return mustUpdate;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public String getVersionUrl() {
        return versionUrl;
    }
}
