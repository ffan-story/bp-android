package com.feifan.bp.password;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.feifan.bp.Constants;
import com.feifan.bp.LogUtil;
import com.feifan.bp.factory.FactorySet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class SendSMSCodeRequest extends Request<PasswordModel> {

    private static final String TAG = SendSMSCodeRequest.class.getSimpleName();

    private static final String URL = FactorySet.getUrlFactory().getFFanHostUrl() + "msgcenter/v1/smsOutboxes";

    private Map<String, String> mParams;

    private Listener<PasswordModel> mListener;

    public SendSMSCodeRequest(Listener<PasswordModel> listener, ErrorListener errorListener, Map<String, String> params) {
        super(Method.POST, URL, errorListener);
        mParams = params;
        mListener = listener;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected Response<PasswordModel> parseNetworkResponse(NetworkResponse networkResponse) {
        String jsonStr = null;
        try {
            jsonStr = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            JSONObject json = new JSONObject(jsonStr);
            int status = json.optInt("status");
            if(status == Constants.RESPONSE_CODE_SUCCESS) {
                return Response.success(new PasswordModel(json.optJSONObject("data")), HttpHeaderParser.parseCacheHeaders(networkResponse));
            }else {
                LogUtil.w(TAG, "error status:" + jsonStr);
                return Response.error(new VolleyError(json.optString("msg")));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            LogUtil.w(TAG, "Response:" + jsonStr);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void deliverResponse(PasswordModel passwordModel) {
        mListener.onResponse(passwordModel);
    }

}
