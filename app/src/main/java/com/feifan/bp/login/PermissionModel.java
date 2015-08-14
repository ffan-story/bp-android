package com.feifan.bp.login;

import com.feifan.bp.LogUtil;
import com.feifan.bp.base.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maning on 15/8/6.
 */
public class PermissionModel extends BaseModel {

    private static final String TAG = "PermissionModel";

    private List<String> permissionList;
    private Map<String, String> urlMap;

    public PermissionModel(JSONObject json) {
        super(json, true);
    }

    @Override
    protected void parseData(JSONObject data) {

    }

    @Override
    protected void parseArrayData(JSONArray data) {
        permissionList = new ArrayList<>();
        urlMap = new HashMap<>();
        int length = data.length();
        for (int i = 0; i < length; ++i) {
            JSONObject item = data.optJSONObject(i);
            JSONArray menu = item.optJSONArray("menu");
            for (int j=0; j<menu.length(); ++j) {
                JSONObject m = menu.optJSONObject(j);
                String id = m.optString("id");
                String url = m.optString("url");
                LogUtil.i(TAG, "id=" + id + " url=" + url);
                permissionList.add(id);
                urlMap.put(id, url);
            }


        }
        LogUtil.i("PermissionModel", "list=" + permissionList);
    }

    public List<String> getPermissionList() {
        return permissionList;
    }

    public Map<String, String> getUrlMap() {
        return urlMap;
    }
}
