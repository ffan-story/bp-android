package com.feifan.bp.login;

import com.feifan.bp.LogUtil;
import com.feifan.bp.base.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maning on 15/8/6.
 */
public class PermissionModel extends BaseModel {

    private List<String> permissionList;

    public PermissionModel(JSONObject json) {
        super(json, true);
    }

    @Override
    protected void parseData(JSONObject data) {

    }

    @Override
    protected void parseArrayData(JSONArray data) {
        permissionList = new ArrayList<>();
        int length = data.length();
        for (int i = 0; i < length; ++i) {
            JSONObject item = data.optJSONObject(i);
            String id = item.optString("id");
            permissionList.add(id);
        }
        LogUtil.i("PermissionModel", "list=" + permissionList);
    }

    public List<String> getPermissionList() {
        return permissionList;
    }
}
