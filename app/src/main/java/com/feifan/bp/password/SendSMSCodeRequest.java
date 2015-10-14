package com.feifan.bp.password;

import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.HttpParams;
import com.feifan.bp.net.UrlFactory;

import org.json.JSONObject;

/**
 * Created by maning on 15/7/29.
 */
public class SendSMSCodeRequest extends BaseRequest<PasswordModel> {

    private static final String TAG = SendSMSCodeRequest.class.getSimpleName();
    // private static final String URL = FactorySet.getUrlFactory().getFFanHostUrl() + "msgcenter/v1/smsOutboxes";
    private static final String URL = UrlFactory.sendSMS();

    public SendSMSCodeRequest(Parameters params,
                              BaseRequestProcessListener<PasswordModel> listener) {
        super(Method.POST, URL, params, listener);
    }

    @Override
    protected PasswordModel onGetModel(JSONObject json) {
        return new PasswordModel(json);
    }

    public static class Params extends Parameters {
        @HttpParams(type = HttpParams.Type.BODY)
        private String phone;

        @HttpParams(type = HttpParams.Type.BODY)
        private String content;

        @HttpParams(type = HttpParams.Type.BODY)
        private String templateId;

        public void setContent(String content) {
            this.content = content;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }
    }

}
