package com.feifan.bp.home;

import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.NetUtils;

import org.json.JSONObject;

/**
 * Created by maning on 15/7/28.
 */
public class CheckVersionRequest extends BaseRequest<CheckVersionModel> {

    private static final String URL = NetUtils.getUrlFactory().getFFanHostUrl() + "xadmin/getversioninfo?appType=bpMobile";

    public CheckVersionRequest(
            BaseRequestProcessListener<CheckVersionModel> listener) {
        super(Method.GET, URL, null, listener);
    }
    @Override
    protected CheckVersionModel onGetModel(JSONObject json) {
        return new CheckVersionModel(json);
    }
}
