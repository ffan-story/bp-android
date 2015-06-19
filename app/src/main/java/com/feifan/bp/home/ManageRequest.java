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
import com.feifan.bp.home.Model.MenuListModel;
import com.feifan.bp.home.Model.MenuModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by xuchunlei on 15/6/19.
 */
public class ManageRequest extends Request<MenuListModel> {

    private static final String URL_FORMAT = FactorySet.getUrlFactory().getFFanHostUrl() + "xadmin/getAuthList?agId=%d";

    private Listener<MenuListModel> mListener;

    public ManageRequest(int agId, Listener<MenuListModel> listener, ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, agId), errorListener);
        mListener = listener;
        Log.e("xuchunlei", String.format(URL_FORMAT, agId));
    }

    @Override
    protected Response<MenuListModel> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String jsonStr = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            Log.e("xuchunlei", jsonStr);
            JSONObject json = new JSONObject(jsonStr);
            int status = json.optInt("status");
            if(status == Constants.RESPONSE_CODE_SUCCESS) {
                return Response.success(new MenuListModel(json.optJSONArray("data")), HttpHeaderParser.parseCacheHeaders(networkResponse));
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
    protected void deliverResponse(MenuListModel menuListModel) {
        mListener.onResponse(menuListModel);
    }
}
