package com.feifan.bp.home;

import com.feifan.bp.BaseModel;
import com.feifan.bp.Constants;

import org.json.JSONObject;

/**
 * Created by xuchunlei on 15/7/7.
 */
public class VersionModel {

    public int versionCode;
    public String versionUrl;

    public VersionModel(JSONObject json) {
        versionCode = json.optInt("version_code");
        versionUrl = json.optString("version_url");

    }

    @Override
    public String toString() {
        return super.toString() + ",versionCode=" + versionCode + ",versionUrl=" + versionUrl;
    }
}
