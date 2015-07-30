package com.feifan.bp.password;

import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.HttpParams;
import com.feifan.bp.net.NetUtils;

import org.json.JSONObject;

/**
 * Created by maning on 15/7/29.
 */
public class ForgetPasswordRequest extends BaseRequest<PasswordModel> {


    private static final String TAG = ForgetPasswordRequest.class.getSimpleName();
    private static String URL_FORMAT = NetUtils.getUrlFactory().getFFanHostUrl() + "xadmin/forgetpwd?";
    private static String URL = NetUtils.getUrlFactory().getFFanHostUrl() + "xadmin/forgetpwd";

    public ForgetPasswordRequest(Parameters parameters,
                                 BaseRequestProcessListener<PasswordModel> listener) {
        super(Method.GET, URL, parameters, listener);
    }

    @Override
    protected PasswordModel onGetModel(JSONObject json) {
        return new PasswordModel(json);
    }

    public static class Params extends Parameters {
        @HttpParams(type = HttpParams.Type.URL)
        private String phone;

        @HttpParams(type = HttpParams.Type.URL)
        private String authCode;

        @HttpParams(type = HttpParams.Type.URL)
        private String keyCode;

        public void setAuthCode(String authCode) {
            this.authCode = authCode;
        }

        public void setKeyCode(String keyCode) {
            this.keyCode = keyCode;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
