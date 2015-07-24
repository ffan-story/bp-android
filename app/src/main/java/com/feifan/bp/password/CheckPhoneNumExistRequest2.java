package com.feifan.bp.password;

import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.NetUtils;

import org.json.JSONObject;

/**
 * Created by maning on 15/7/29.
 */
public class CheckPhoneNumExistRequest2 extends BaseRequest<PasswordModel2> {

    private static final String TAG = CheckPhoneNumExistRequest.class.getSimpleName();

    private static final String URL_FORMAT = NetUtils.getUrlFactory().getFFanHostUrl() +
            "xadmin/verificationphone?phone=%s";

    public CheckPhoneNumExistRequest2(String phone,
                                      BaseRequestProcessListener<PasswordModel2> listener) {
        super(Method.GET, String.format(URL_FORMAT, phone), null, listener);
    }


    @Override
    protected PasswordModel2 onGetModel(JSONObject json) {
        return new PasswordModel2(json);
    }
}
