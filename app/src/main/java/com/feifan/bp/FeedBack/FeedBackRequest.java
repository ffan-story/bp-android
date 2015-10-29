package com.feifan.bp.FeedBack;

import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.HttpParams;
import com.feifan.bp.net.UrlFactory;

import org.json.JSONObject;

/**
 * Created by tianjun on 2015-10-29.
 */
public class FeedBackRequest extends BaseRequest<FeedBackModel> {
    private static final String URL = UrlFactory.submitFeedBack();

    public FeedBackRequest(Parameters params, OnRequestProcessListener listener) {
        super(Method.POST, URL, params, listener);
    }

    @Override
    protected FeedBackModel onGetModel(JSONObject json) {
        return new FeedBackModel(json);
    }

    public static class Params extends Parameters {

        @HttpParams(type = HttpParams.Type.BODY)
        private String uid;

        @HttpParams(type = HttpParams.Type.BODY)
        private String description;

        public void setUid(String uid) {
            this.uid = uid;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
