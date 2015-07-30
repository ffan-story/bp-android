package com.feifan.bp.password;

import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.HttpParams;
import com.feifan.bp.net.NetUtils;

import org.json.JSONObject;

/**
 * Created by maning on 15/7/29.
 */
public class CheckPhoneNumExistRequest extends BaseRequest<PasswordModel> {

    private static final String TAG = CheckPhoneNumExistRequest.class.getSimpleName();

    private static final String URL_FORMAT = NetUtils.getUrlFactory().getFFanHostUrl() +
            "xadmin/verificationphone?phone=%s";

    private static final String URL = NetUtils.getUrlFactory().getFFanHostUrl() +
            "xadmin/verificationphone";

    public CheckPhoneNumExistRequest(Parameters parameters,
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

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
