package com.feifan.bp.login;

import com.feifan.bp.network.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xuchunlei on 15/6/18.
 */
public class UserModel extends BaseModel {

    public int uid;
    public int agId;
    public String user;
    public String name;
    public String authRange;
    public String authRangeType;
    public String authRangeId;
    public String loginToken;
    public String merchantId;
    public String plazaId;

    public UserModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONObject data = new JSONObject(json);
        uid = data.optInt("uid");
        agId = data.optInt("agId");
        user = data.optString("user");
        name = data.optString("name");
        authRange = data.optString("authRange");
        authRangeType = data.optString("authRangeType");
        authRangeId = data.optString("authRangeId");
        loginToken = data.optString("loginToken");
        merchantId = data.optString("merchantId");
        plazaId = data.optString("plazaId");
    }

    @Override
    public String toString() {
        return "uid=" + uid + " ,agId=" + agId + " ,user=" + user + " ,name=" + name + ",authRange=" + authRange
                + ",authRangeType=" + authRangeType + " ,authRangeId=" + authRangeId + ",loginToken=" + loginToken;
    }
}
