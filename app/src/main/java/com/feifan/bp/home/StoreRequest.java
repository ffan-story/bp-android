package com.feifan.bp.home;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.feifan.bp.Constants;
import com.feifan.bp.factory.FactorySet;
import com.feifan.bp.home.Model.StoreModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by xuchunlei on 15/6/19.
 */
public class StoreRequest extends Request<StoreModel> {

    private static final String URL_FORMAT = FactorySet.getUrlFactory().getFFanHostUrl() + "v1/cdaservice/stores/%s/detail";

    private Listener<StoreModel> mListener;

    public StoreRequest(String storeId, Listener<StoreModel> listener, ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, storeId), errorListener);
        mListener = listener;
    }


    @Override
    protected Response<StoreModel> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String jsonStr = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            Log.e("xuchunlei", jsonStr);
            JSONObject json = new JSONObject(jsonStr);
            int status = json.optInt("status");
            if(status == Constants.RESPONSE_CODE_SUCCESS) {
                return Response.success(new StoreModel(json.optJSONObject("data")), HttpHeaderParser.parseCacheHeaders(networkResponse));
            }else {

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void deliverResponse(StoreModel storeModel) {
        mListener.onResponse(storeModel);
    }
}
