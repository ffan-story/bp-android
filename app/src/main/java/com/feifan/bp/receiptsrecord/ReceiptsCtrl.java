package com.feifan.bp.receiptsrecord;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.PlatformState;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;

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
                .param("storeId", "11200")
                .build()
                .targetClass(ReceiptsModel.class)
                .listener(listener);
        PlatformState.getInstance().getRequestQueue().add(request);
    }
}
