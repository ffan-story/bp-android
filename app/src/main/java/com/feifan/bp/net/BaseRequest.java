package com.feifan.bp.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.feifan.bp.Constants;
import com.feifan.bp.LogUtil;
import com.feifan.bp.base.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by maning on 15/7/28.
 */
public abstract class BaseRequest<T extends BaseModel> extends Request<T> {

    private static final String TAG = "BaseRequest";

    public interface OnRequestProcessListener<T> extends Response.ErrorListener {
        void onErrorResponse(VolleyError error);

        void onResponse(T t);

        void onStart();

        void onFinish();
    }

    private OnRequestProcessListener<T> mOnRequestProcessListener;

    private Map<String, String> mParams;

    public BaseRequest(int method, String url, Map<String, String> params,
                       OnRequestProcessListener<T> listener) {
        super(method, url, listener);
        mParams = params;
        mOnRequestProcessListener = listener;
        if (mOnRequestProcessListener!=null) {
            mOnRequestProcessListener.onStart();
        }
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected void deliverResponse(T t) {
        if (mOnRequestProcessListener!=null) {
            mOnRequestProcessListener.onResponse(t);
        }
    }

    public final void onFinish() {
        if (mOnRequestProcessListener!=null) {
            mOnRequestProcessListener.onFinish();
        }
    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {

        String jsonStr = null;
        String errStr = null;
        try {
            jsonStr = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            LogUtil.i(TAG, jsonStr);
            JSONObject json = new JSONObject(jsonStr);
            T model = onGetModel(json);
            if(model.getStatus() == Constants.RESPONSE_CODE_SUCCESS) {
                return Response.success(model, HttpHeaderParser.parseCacheHeaders(networkResponse));
            }else {
                LogUtil.w(TAG, "error status:" + jsonStr);
                errStr = model.getMsg();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            LogUtil.w(TAG, "Response:" + jsonStr);
            e.printStackTrace();
        }

        return Response.error(new VolleyError(errStr));
    }

    protected abstract T onGetModel(JSONObject json);

}
