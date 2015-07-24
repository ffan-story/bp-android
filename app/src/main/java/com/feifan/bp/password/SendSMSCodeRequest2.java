package com.feifan.bp.password;

import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.NetUtils;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by maning on 15/7/29.
 */
public class SendSMSCodeRequest2 extends BaseRequest<PasswordModel2> {

    private static final String TAG = SendSMSCodeRequest2.class.getSimpleName();
    // private static final String URL = FactorySet.getUrlFactory().getFFanHostUrl() + "msgcenter/v1/smsOutboxes";
    private static final String URL = NetUtils.getUrlFactory().getFFanHostUrl() + "xadmin/phoneSms";

    public SendSMSCodeRequest2(Map<String, String> params,
                               BaseRequestProcessListener<PasswordModel2> listener) {
        super(Method.POST, URL, params, listener);
    }

    @Override
    protected PasswordModel2 onGetModel(JSONObject json) {
        return new PasswordModel2(json);
    }
}
