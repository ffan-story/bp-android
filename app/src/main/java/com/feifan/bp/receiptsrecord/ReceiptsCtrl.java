package com.feifan.bp.receiptsrecord;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.network.GetRequest;
import com.feifan.bp.base.network.JsonRequest;
import com.feifan.bp.base.network.UrlFactory;

/**
 * Created by konta on 2016/3/17.
 */
public class ReceiptsCtrl {

    public static void getReceiptsRecords(String startDate,String endDate,String page,
                                          Response.Listener listener,
                                          Response.ErrorListener errorListener){
        JsonRequest<ReceiptsModel> request = new GetRequest.Builder<ReceiptsModel>(UrlFactory.getReceiptsRecordsList())
                .errorListener(errorListener)
                .param("startDate",startDate)
                .param("endDate",endDate)
                .param("page",page)
                .param("limit", Constants.LIST_LIMIT)
                .param("storeId", UserProfile.getInstance().getAuthRangeId())
                .build()
                .targetClass(ReceiptsModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
