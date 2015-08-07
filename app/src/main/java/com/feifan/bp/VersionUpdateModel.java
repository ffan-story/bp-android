package com.feifan.bp;

import com.feifan.bp.base.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by maning on 15/8/7.
 */
public class VersionUpdateModel extends BaseModel {

    public static final int UPDATE_NO_UPDATE = -1;
    public static final int UPDATE_NO_FORCE = 0;
    public static final int UPDATE_FORCE = 1;

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
