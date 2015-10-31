package com.feifan.bp.FeedBack;

import com.android.volley.Response;
import com.feifan.bp.PlatformState;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.PostRequest;

/**
 * Created by tianjun on 2015-10-29.
 */
public class FeedBackCtrl{
    public static void submitFeedBack(String uid, String description,
                                      Response.Listener<FeedBackModel> listener) {
        JsonRequest<FeedBackModel> request = new PostRequest<FeedBackModel>(UrlFactory.submitFeedBack(), null)
                .param("uid", uid)
                .param("description", description)
                .targetClass(FeedBackModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
