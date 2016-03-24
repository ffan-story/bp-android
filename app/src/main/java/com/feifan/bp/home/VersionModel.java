package com.feifan.bp.home;

import com.feifan.bp.base.network.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maning on 15/8/7.
 */
public class VersionModel extends BaseModel {

    public static final int UPDATE_NO_UPDATE = -1;//不升级
    public static final int UPDATE_NO_FORCE = 0;//建议更新
    public static final int UPDATE_FORCE = 1;//强制升级

    private int versionCode;
    private String versionDesc;
    private String versionUrl;
    private int mustUpdate;

    public VersionModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
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

    @Override
    public String toString() {
        return super.toString() + ",versionCode=" + versionDesc + ",versionUrl=" + versionUrl
                + ",versionDesc=" + versionDesc + ",mustUpdate=" + mustUpdate;
    }
}
