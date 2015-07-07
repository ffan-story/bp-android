package com.feifan.bp.home;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.feifan.bp.factory.FactorySet;

/**
 * Created by xuchunlei on 15/7/7.
 */
public class VersionRequest extends Request<VersionModel> {

    private static final String URL = FactorySet.getUrlFactory().getFFanHostUrl() + "xadmin/getversioninfo";

    private Response.Listener<VersionModel> mListener;

    public VersionRequest(Response.Listener<VersionModel> listener, Response.ErrorListener errorListener) {
        super(Method.GET, URL, errorListener);
//        mParams = params;
        mListener = listener;
//        params.put("appType", "bpMobile");
    }

    @Override
    protected Response<VersionModel> parseNetworkResponse(NetworkResponse networkResponse) {
        return null;
    }

    @Override
    protected void deliverResponse(VersionModel versionModel) {

    }
}
