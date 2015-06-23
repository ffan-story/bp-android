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
import com.feifan.bp.home.Model.MerchantModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by xuchunlei on 15/6/19.
 */
public class MerchantRequest extends Request<MerchantModel> {

    private static final String URL_FORMAT = FactorySet.getUrlFactory().getFFanHostUrl() + "v1/cdaservice/bp/merchants/%s";

    private Listener<MerchantModel> mListener;

    public MerchantRequest(String merchantId, Listener<MerchantModel> listener, ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, merchantId), errorListener);
        mListener = listener;
    }

    @Override
    protected Response<MerchantModel> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String jsonStr = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            JSONObject json = new JSONObject(jsonStr);
            Log.e("xuchunlei", jsonStr);
            int status = json.optInt("status");
            if(status == Constants.RESPONSE_CODE_SUCCESS) {
                return Response.success(new MerchantModel(json.optJSONObject("data")), HttpHeaderParser.parseCacheHeaders(networkResponse));
            }else {
                Log.e("xuchunlei", jsonStr);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void deliverResponse(MerchantModel merchantModel) {
        mListener.onResponse(merchantModel);
    }
}
