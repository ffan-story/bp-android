package com.feifan.bp.password;

import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.NetUtils;

import org.json.JSONObject;

/**
 * Created by maning on 15/7/29.
 */
public class ForgetPasswordRequest2 extends BaseRequest<PasswordModel2> {

    private String phone;
    private String authCode;
    private String keyCode;

    private static final String TAG = ForgetPasswordRequest.class.getSimpleName();
    private static String URL_FORMAT = NetUtils.getUrlFactory().getFFanHostUrl() + "xadmin/forgetpwd?";

    public ForgetPasswordRequest2(String phone, String authCode, String keyCode,
                                  BaseRequestProcessListener<PasswordModel2> listener) {
        super(Method.GET, URL_FORMAT+"phone="+phone+"&authCode="+authCode+"&keyCode="+keyCode,
                null, listener);
    }

    @Override
    protected PasswordModel2 onGetModel(JSONObject json) {
        return new PasswordModel2(json);
    }
}
