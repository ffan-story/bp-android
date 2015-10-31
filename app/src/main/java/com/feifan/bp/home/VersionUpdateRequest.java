package com.feifan.bp.home;

import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.HttpParams;
import com.feifan.bp.network.UrlFactory;

import org.json.JSONObject;

/**
 * Created by maning on 15/8/7.
 */
public class VersionUpdateRequest extends BaseRequest<VersionUpdateModel> {

    private static final String URL = UrlFactory.checkVersionUpdate();

    public VersionUpdateRequest(Parameters parameters, BaseRequestProcessListener<VersionUpdateModel> listener) {
        super(Method.POST, URL, parameters, listener);
    }
    @Override
    protected VersionUpdateModel onGetModel(JSONObject json) {
        return new VersionUpdateModel(json);
    }

    public static class Params extends Parameters {

        @HttpParams(type = HttpParams.Type.BODY)
        private String currVersionCode;

        public void setCurrVersionCode(String currVersionCode) {
            this.currVersionCode = currVersionCode;
        }
    }
}
