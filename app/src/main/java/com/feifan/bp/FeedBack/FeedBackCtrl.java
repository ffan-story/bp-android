package com.feifan.bp.FeedBack;

import android.content.Context;

import com.feifan.bp.net.BaseRequest;
import com.feifan.bp.net.BaseRequestProcessListener;
import com.feifan.bp.net.HttpEngine;

/**
 * Created by tianjun on 2015-10-29.
 */
public class FeedBackCtrl{
    public static void submitFeedBack(Context context, String uid, String description,
                             BaseRequestProcessListener<FeedBackModel> listener) {

        FeedBackRequest.Params params = BaseRequest.newParams(FeedBackRequest.Params.class);
        params.setUid(uid);
        params.setDescription(description);
        HttpEngine.Builder.newInstance(context).
                setRequest(new FeedBackRequest(params, listener)).
                build().start();

    }
}
