package com.feifan.bp.home;


import com.feifan.bp.net.BaseRequest;

import org.json.JSONObject;

/**
 * Created by maning on 15/8/10.
 */
public class UnreadRefundRequest extends BaseRequest<UnreadRefundModel> {

    private static String URL = "";

    public UnreadRefundRequest(Parameters params, OnRequestProcessListener<UnreadRefundModel> listener) {
        super(Method.GET, URL, params, listener);
    }

    @Override
    protected UnreadRefundModel onGetModel(JSONObject json) {
        return new UnreadRefundModel(json);
    }

    public static class Params extends Parameters {

    }
}
