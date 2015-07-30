package com.feifan.bp.password;

import com.feifan.bp.PlatformState;
import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.HttpParams;
import com.feifan.bp.net.NetUtils;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by maning on 15/7/29.
 */
public class ResetPasswordRequest extends BaseRequest<PasswordModel> {

    private static final String TAG = ResetPasswordRequest.class.getSimpleName();

    private static final String URL = NetUtils.getUrlFactory().getFFanHostUrl() + "xadmin/editPassword";

    public ResetPasswordRequest(Parameters params, BaseRequestProcessListener<PasswordModel> listener) {
        super(Method.POST, URL, params, listener);
    }

    @Override
    protected PasswordModel onGetModel(JSONObject json) {
        return new PasswordModel(json);
    }


    public static class Params extends Parameters {
        @HttpParams(type = HttpParams.Type.BODY)
        private String oldPassword;

        @HttpParams(type = HttpParams.Type.BODY)
        private String newPassword;

        @HttpParams(type = HttpParams.Type.BODY)
        private String uId;

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public void setuId(String uId) {
            this.uId = uId;
        }
    }
}
