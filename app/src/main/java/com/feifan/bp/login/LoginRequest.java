package com.feifan.bp.login;

import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.HttpParams;
import com.feifan.bp.net.UrlFactory;

import org.json.JSONObject;

/**
 * Created by maning on 15/7/29.
 */
public class LoginRequest extends BaseRequest<UserModel> {


    private static final String URL = UrlFactory.getLoginUrl();


    public LoginRequest(Parameters params, OnRequestProcessListener<UserModel> listener) {
        super(Method.POST, URL, params, listener);
    }

    @Override
    protected UserModel onGetModel(JSONObject json) {
        return new UserModel(json);
    }


    public static class Params extends Parameters {

        @HttpParams(type = HttpParams.Type.BODY)
        private String userName;

        @HttpParams(type = HttpParams.Type.BODY)
        private String password;

        @HttpParams(type = HttpParams.Type.BODY)
        private String authRangeType;

        public void setPassword(String password) {
            this.password = password;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setAuthRangeType(String authRangeType) {
            this.authRangeType = authRangeType;
        }
    }
}
