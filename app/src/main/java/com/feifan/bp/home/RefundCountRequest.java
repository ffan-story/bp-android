package com.feifan.bp.home;

import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.HttpParams;
import com.feifan.bp.net.NetUtils;

import org.json.JSONObject;

/**
 * Created by maning on 15/8/13.
 */
public class RefundCountRequest extends BaseRequest<RefundCountModel> {

    private static final String URL = NetUtils.getUrlFactory().refundCount();
    public RefundCountRequest(Parameters params, OnRequestProcessListener<RefundCountModel> listener) {
        super(Method.GET, URL, params, listener);
    }

    @Override
    protected RefundCountModel onGetModel(JSONObject json) {
        return new RefundCountModel(json, false);
    }

    public static class Params extends Parameters {
        @HttpParams(type = HttpParams.Type.URL)
        private String storeId;

        @HttpParams(type = HttpParams.Type.URL)
        private String refundStatus;

        public void setRefundStatus(String refundStatus) {
            this.refundStatus = refundStatus;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }
    }
}
