package com.feifan.bp.login;

import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.NetUtils;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by maning on 15/7/29.
 */
public class LoginRequest extends BaseRequest<UserModel> {

    private static final String TAG = "LoginRequest";

    private static final String URL = NetUtils.getUrlFactory().getFFanHostUrl() + "xadmin/login";

    public LoginRequest(Map<String, String> params, OnRequestProcessListener<UserModel> listener) {
        super(Method.POST, URL, params, listener);
    }

    @Override
    protected UserModel onGetModel(JSONObject json) {
        return new UserModel(json);
    }
}
